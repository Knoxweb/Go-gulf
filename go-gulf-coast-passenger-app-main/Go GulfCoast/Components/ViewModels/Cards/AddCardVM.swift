//
//  AddCardVM.swift
//   GoGulf
//
//  Created by Mac on 6/25/22.
//

import Foundation
import Stripe
import SwiftUI
import Combine
import FirebaseFirestore

struct SendStripeToken: Codable, Hashable {
    let cardToken: String?
    
    enum CodingKeys: String, CodingKey {
        case cardToken = "card_token"
    }
}



class PaymentVM: BaseObservableObject {
    @Published var cardNumber = ""
    
    @Published var expiryYear: Int = Calendar.current.component(.year, from: Date())
    @Published var expiryMonth: Int = Calendar.current.component(.month, from: Date())
    @Published var cardExpiry = ""
    @Published var CVC = ""
    @Published var cardHolder = ""
    @Published var cardResponse: DefaultResponse?
    @Published var defaultRes: DefaultResponse?
    @Published var cards: [PaymentModel]?
    @Published var showEditSheet = false
    @Published var showSuccess = false
    private var cardListener: ListenerRegistration?
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var month = ""
    @Published var year = ""
    @Published var currentCountry: CPData?
    
    internal func initialize() {
        Task (priority: .medium){
            await getCard()
        }
    }
    
    func handlePayment() {
        STPAPIClient.shared.publishableKey = "\(Env.stripePubKey)"
        if cardNumber.isEmpty || cardExpiry.isEmpty || CVC.isEmpty {
            self.alertTitle = "Missing"
            self.alertMessage =  "Please fill all fields"
            self.showAlert.toggle()
            return;
        }
        let cardParams = STPCardParams()
        cardParams.number = String(cardNumber)
        cardParams.cvc = String(CVC)
        cardParams.name = cardHolder
        if let index = cardExpiry.firstIndex(of: "/") {
            let month = cardExpiry.prefix(upTo: index)
            let year = cardExpiry.suffix(from: cardExpiry.index(after: index))
            cardParams.expMonth = UInt(month) ?? UInt()
            cardParams.expYear = UInt(year) ?? UInt()
            cardParams.address.country = currentCountry?.code
        }
        loading = true
        STPAPIClient.shared.createToken(withCard: cardParams)  { (paymentMethod, error) in
            self.loading = false
            if let error = error {
                self.alertTitle = "Error"
                self.alertMessage =  "\(error.localizedDescription)"
                self.showAlert.toggle()
            } else if let paymentMethod = paymentMethod {
                print("STPToken Token:  \(paymentMethod), ----- \(cardParams)")
                self.sendStripeToken(paymentMethod)
            }
        }
    }
    
    public func stopListener() {
        cardListener?.remove()
        cardListener = nil
    }
    
    @MainActor
    public func getCard() {
        let profileDocument = parentCollection.collection("cards")
        self.redacting = true
        
        cardListener = profileDocument.addSnapshotListener { [weak self] (querySnapshot, error) in
            self?.redacting = false
            self?.cards = []
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
                        let item = try decoder.decode(PaymentModel.self, from: jsonData)
                        self?.cards?.append(item)
                    } catch {
                        print("Error decoding document: \(error)")
                    }
                }
            } else {
                print("\(profileDocument.path) No documents found schedulesss.")
            }
        }
    }
    
    public func updateCard(_ forCardId: Int?){
        print(cardExpiry, "sdfasdfadsfasdfasdfadsfd")
        if let index = cardExpiry.firstIndex(of: "/") {
            self.month = String(cardExpiry.prefix(upTo: index))
            self.year = String(cardExpiry.suffix(from: cardExpiry.index(after: index)))
        }
        let body = UpdateCardModel(name: self.cardHolder, exp_year: self.year, exp_month: self.month)
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.cardUpdate("\(forCardId ?? 0)"), body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
//                    self.showEditSheet = false
                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
                    self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                    self.showSuccess.toggle()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                print(response, "resssssssss")
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    public func deleteCard(_ forCardId: Int?){
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.deleteCard("\(forCardId ?? 0)"), body: "")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
//                    self.showEditSheet = false
                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
                    self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                    self.showSuccess.toggle()
                    
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
    
    private func sendStripeToken(_ token: STPToken){
        let body = SendStripeToken(cardToken: "\(token)")
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.addCard, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
                    self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                    self.showSuccess.toggle()
                    
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
}
