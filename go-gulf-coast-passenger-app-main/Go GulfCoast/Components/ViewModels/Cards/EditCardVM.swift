//
//  EditCardVM.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 09/09/2024.
//

import Foundation

class EditCardVM: BaseObservableObject {
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
    @Published var month = ""
    @Published var year = ""
    @Published var currentCountry: CPData?
    @Published var active = false
    @Published var showSuccess = false
    
    
    public func updateCard(_ forCardId: Int?){
//        print(cardExpiry, "sdfasdfadsfasdfasdfadsfd")
//        if let index = cardExpiry.firstIndex(of: "/") {
//            self.month = String(cardExpiry.prefix(upTo: index))
//            self.year = String(cardExpiry.suffix(from: cardExpiry.index(after: index)))
//        }
//        let body = UpdateCardModel(name: self.cardHolder, exp_year: self.year, exp_month: self.month)
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.activateCard("\(forCardId ?? 0)"), body: "")
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
}
