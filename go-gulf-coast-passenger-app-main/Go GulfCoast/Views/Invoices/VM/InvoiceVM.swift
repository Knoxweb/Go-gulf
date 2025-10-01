//
//  InvoiceVM.swift

//
//  Created by Prabin Phasikawo on 28/05/2024.
//

import Foundation
import FirebaseFirestore
import SwiftUI
import PDFKit
import Combine
import Stripe
import Firebase

class InvoiceVM: BaseObservableObject {
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    private var invoiceListener: ListenerRegistration?
    @Published var invoices: [InvoiceModel]?
    @Published public var fromDate = Date.now
    @Published public var toDate = Date.now
    @Published var openPDF = false
    @Published var path = ""
    @Published var selection: PaymentModel?
    @Published var defaultRes: DefaultResponse?
    @Published var showCardSheet = false
    @Published var selectedInvoices: Set<InvoiceModel> = []
    @Published var show = false
    @Published var showShareSheet = false
    @Published var fdfFile = Data()
    @Published var cards: [PaymentModel]? = []
    @Published var activeCard: PaymentModel?
    private var cardListener: ListenerRegistration?
    let refCollection = Firestore.firestore().collection("passengers")
    
    var fileURL: URL?

    public func stopListener() {
        self.invoiceListener?.remove()
        self.invoiceListener = nil
        self.cardListener?.remove()
        self.cardListener = nil
    }
    
    
    
    override init() {
        let calendar = Calendar.current
        self.fromDate = calendar.date(byAdding: .month, value: -1, to: Date()) ?? Date()
    }
    
    func handlePayment(secrete: String?, invoiceId: Int?) {
        let paymentHandler = STPPaymentHandler.shared()
        let paymentIntentParams = STPPaymentIntentParams(clientSecret: secrete ?? "")
        guard secrete != "" else { return }
        self.loading = true
        STPAPIClient.shared.publishableKey = "\(Env.stripePubKey)"
//        print(clientSecret, "secrete ---------------------")
        paymentHandler.confirmPayment(paymentIntentParams, with: self) { (status, paymentIntent, error) in
            self.loading = false
            print(paymentIntent?.stripeId as Any, "stripe IDIDIIDDIIDIDIIIDD")
            
            switch status {
            case .succeeded:
//                self.caputurePayment(invoiceId: invoiceId)
                self.updateInvoiceStatus(invoiceId: invoiceId, paymentStatus: "paid", paymentid: paymentIntent?.stripeId)
                print("Payment succeeded")
                break
            case .failed:
                print(error as Any, status)
                self.updateInvoiceStatus(invoiceId: invoiceId, paymentStatus: "error", paymentid: "")
                print("Payment failed")
                break
            case .canceled:
                self.updateInvoiceStatus(invoiceId: invoiceId, paymentStatus: "error", paymentid: "")
//                self.alertMessage = "\(String(describing: error))"
                print("Payment cancelled")
                break
//            case .requiresConfirmation:
//                // Payment requires additional confirmation
//                break
            @unknown default:
                self.updateInvoiceStatus(invoiceId: invoiceId, paymentStatus: "error", paymentid: "")
//                print(error, status)
                print("other case")
                break
            }
        }
    }
    
    
    func downloadPDF(_ invId: Int?, reference: String?) {
        self.loading = true
        NetworkManager.shared.downloadPDF(from: APIEndpoints.downloadPDF("\(invId ?? 0)"))
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print("PDF download completed")
                case .failure(let error):
                    print(error as Any, "server error")
                    self.alertTitle = "Not Found"
                    self.alertMessage = "No file found"
                    self.showAlert.toggle()
                }
            } receiveValue: { pdfData in
                // Save PDF data to a file
                if let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
                    let fileURL = documentsDirectory.appendingPathComponent("\(reference ?? "").pdf")
                    _ = FileManager.default.temporaryDirectory
                          
                    do {
                        try pdfData.write(to: fileURL)
                        self.path = fileURL.path
                        self.showShareSheet = true
                        self.fileURL = fileURL
                        self.fdfFile = pdfData
                    } catch {
                        print("Error saving PDF file: \(error)")
                    }
                }
            }
            .store(in: &cancellables)
    }
    
//    func multiplePDFDownload() {
//        let body = MultiplePDFRequestModel(invoices: selectedInvoices.compactMap { $0.id })
//        self.loading = true
//            NetworkManager.shared.multiplePDFDownload(to: APIEndpoints.multiplePDFDownload, body: body)
//                .sink { completion in
//                    self.loading = false
//                    switch completion {
//                    case .finished:
//                        print("PDF download completed")
//                    case .failure(let error):
//                        print(error.localizedDescription)
//                    }
//                } receiveValue: { pdfData in
//                    if let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
//                        let fileURL = documentsDirectory.appendingPathComponent("\(reference ?? "").pdf")
//                        _ = FileManager.default.temporaryDirectory
//                              
//                        do {
//                            try pdfData.write(to: fileURL)
//                            self.path = fileURL.path
//                            self.showShareSheet = true
//                            self.fileURL = fileURL
//                            self.fdfFile = pdfData
//                        } catch {
//                            print("Error saving PDF file: \(error)")
//                        }
//                    }
//                }
//                .store(in: &cancellables)
//        }
    
    
    public func updateInvoiceStatus(invoiceId: Int?, paymentStatus: String?, paymentid: String?) {
        let body = UpdateInvoiceModel(payment_status: paymentStatus, payment_id: paymentid)
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.invoiceStatus("\(invoiceId ?? 0)"), body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.defaultRes?.message ?? "")
//                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
//                    self.alertMessage = "\(self.defaultRes?.message ?? "")"
//                    self.showAlert.toggle()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
                
            }
            .store(in: &cancellables)
    }
    
    
    
    public func cardObserver() {
        guard let UID = UserDefaults.standard.string(forKey: "UID") else { return }
        self.loading = true
        let collection = refCollection.document(UID).collection("cards")
        cardListener = collection.addSnapshotListener { [weak self] (querySnapshot, error) in
            self?.loading = false
            guard self != nil else { return }
            self?.cards = []
            if let error = error {
                print("Error getting documents: \(error)")
                return
            }
            guard let querySnapshot = querySnapshot else {
                print("QuerySnapshot is nil.")
                return
            }
            if !querySnapshot.isEmpty {
                var newArray: [PaymentModel] = []
                for document in querySnapshot.documents {
                    do {
                        let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                        let decoder = JSONDecoder()
                        let item = try decoder.decode(PaymentModel.self, from: jsonData)
                        newArray.append(item)
                    } catch {
                        print("Error decoding document: \(error)")
                    }
                }
                self?.cards = newArray
                DispatchQueue.main.async {
                    let activeCards = self?.cards?.filter {
                        $0.is_active == true
                    }
                    self?.activeCard = activeCards?.first
                }
            } else {
                print("No documents found.")
            }
        }
    }
    
    public func retryInvoice(invoiceId: Int?, cardId: Int?) {
        let body = Retryinvoice(card_id: "\(self.selection?.id ?? 0)")
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.retryInvoice("\(invoiceId ?? 0)"), body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.defaultRes?.message ?? "")
//                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
//                    self.alertMessage = "\(self.defaultRes?.message ?? "")"
//                    self.showAlert.toggle()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
                
            }
            .store(in: &cancellables)
    }

    public func getInvoices(startDate: Date, endDate: Date) {
        let profileDocument = parentCollection.collection("invoices")
        self.redacting = true
        invoiceListener = profileDocument
            .whereField("timestamp", isGreaterThanOrEqualTo:  Int(startDate.timeIntervalSince1970))
            .whereField("timestamp", isLessThanOrEqualTo:  Int(endDate.timeIntervalSince1970))
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.redacting = false
                self?.invoices = []
                guard self != nil else { return }
                
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let item = try decoder.decode(InvoiceModel.self, from: jsonData)
                            self?.invoices?.append(item)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                } else {
                    print("\(profileDocument.path) No documents found.")
                }
            }
    }
}
