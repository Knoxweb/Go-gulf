//
//  quoteConfirmVM.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//

import Foundation
import UIKit
import Firebase


class QuoteConfirmVM: BaseObservableObject {
    @Published var cardSelection: PaymentModel?
    @Published public var expanded: Bool = true
    @Published var successAlert = false
    @Published public var navigateScheduledView = false
    //    Add Note
    @Published public var showSheetView = false
    @Published public var addNoteSheet = false
    @Published public var addNote = ""
    //    Add Extras
    @Published public var infant = 0
    @Published public var child = 0
    @Published public var booster = 0
    @Published public var addExtras = false
    //    FlightsInfo
    @Published public var flightInfo:Bool = false
    @Published public var flightName = ""
    @Published public var flightNumber = ""
    //    CapacityView
    @Published public var capacity:Bool = false
    @Published public var passenger: Int = 1
    @Published public var pet: Int = 0
    @Published public var wheelChair: Int = 0
    //    Lead Passenger View
    @Published public var leadPassenger:Bool = false
    @Published public var fullname = ""
    @Published public var phone = ""
    @Published public var navigateToJR = false
    @Published public var navigateToBookingPage = false
    @Published var navigateToCard = false
    @Published var cardVal = ""
    @Published var cardNumber = ""
    @Published var showSuccess = false
//    let parentCollection = Firestore.firestore().collection("passengers")
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var quoteData: QuoteResponseData?
    @Published var quoteResponse: QuoteConfirmResponse?
    private var cardListener: ListenerRegistration?
    @Published var cards: [PaymentModel]? = []
    @Published var activeCard: PaymentModel?
    @Published var defaultRes: DefaultResponse?
    private var listener: ListenerRegistration?
    @Published var discountApplied = false
    @Published var isLoading = false
    @Published var promoCode  = ""
    @Published var discountedAmount:Double  = 0
    @Published var promoResponse: DiscountCodeResponse?
    
    
    
    func initialize(tabRouter: TabRouter) {
        Task(priority: .medium) {
            await cardObserver()
            await checkBookingrequest(tabRouter: tabRouter)
        }
    }
    
    func stopListener() {
        self.cardListener?.remove()
        self.cardListener = nil
        self.listener?.remove()
        self.listener = nil
    }
    
    
    @MainActor
    private func checkBookingrequest(tabRouter: TabRouter) {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("request")
        print(documentReference.path, "documentReference")
        
        listener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            guard let self = self else { return }
            if let error = error {
                print("Error fetching profile document: \(error)")
                return
            }
            guard let document = documentSnapshot else {
                print("Profile document does not exist")
                return
            }
            do {
                guard document.exists else {
                    throw NSError(domain: "Firestore", code: -1, userInfo: [NSLocalizedDescriptionKey: "Document does not exist"])
                }
                let data = try JSONSerialization.data(withJSONObject: document.data() ?? [:], options: [])
                self.quoteData = try JSONDecoder().decode(QuoteResponseData.self, from: data)
                if self.quoteData?.status == "pending" {
                    self.router?.navigateTo(.requestingScreen(quoteData: self.quoteData))
                }
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    func confirmQuoteSubmit(quoteFleetId: Int, tabRouter: TabRouter){
        
        let param = ConfirmQuoteRequest(
            description: self.addNote,
            flight_number: self.flightNumber,
            lead_passenger_phone: "",
            passenger_count: "\(self.passenger)",
            pet_count: "\(self.pet)",
            wheelchair_count: "\(self.wheelChair)",
            passenger_card_id: "\(self.activeCard?.id ?? 0)"
        )
        if param.passenger_card_id == "0" {
            self.alertTitle = "Card Missing"
            self.alertMessage =  "Please add card to continue"
            self.showAlert = true
            return
        }
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.confirmBooking("\(quoteFleetId)"), body: param)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.defaultRes as Any, "QUOTE RESPONSE")
                    if self.quoteResponse?.data?.type == "scheduled" {
                        self.alertTitle = "\(self.quoteResponse?.title ?? "")"
                        self.alertMessage =  "\(self.quoteResponse?.message ?? "")"
                        self.successAlert = true
                    }
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: QuoteConfirmResponse) in
                self.quoteResponse = response;
            }
            .store(in: &cancellables)
    }
    
    public func checkDiscount(quoteId: String?, fleet: FleetList?) {
        let request = DiscountCodeCheckModel(quote_id: quoteId ?? "", fleet_id: "\(fleet?.id ?? 0)", discount_code:  promoCode)
        self.loading = true
        
        NetworkManager.shared.POST(to:  "quote/check-discount-code", body: request)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print("Success")
                    self.discountApplied = true
                    if let fare = fleet?.fare,
                       let fareDouble = Double(fare),
                       let discount = self.promoResponse?.data?.discount_amount {
                        self.discountedAmount = fareDouble - discount
                    } else {
                        self.discountedAmount = 0
                    }
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    defaultError(title: err.title, msg: err.message)
                }
            } receiveValue: { (response: DiscountCodeResponse) in
                self.promoResponse = response;
            }
            .store(in: &cancellables)
    }
    
    
    @MainActor
    private func cardObserver() {
//        guard let UID = UserDefaults.standard.string(forKey: "UID") else { return }
        self.loading = true
        let collection = parentCollection.collection("cards")
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
}
