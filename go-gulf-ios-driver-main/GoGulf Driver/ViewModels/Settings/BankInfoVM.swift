//
//  BankInfoVM.swift
// SlyykDriverDriver
//
//  Created by Office on 07/08/2022.
//

import Foundation
import UIKit
import Combine

class BankInfoVM: BaseObservableObject {
    @Published var bankName = ""
    @Published var holdersName = ""
    @Published var accountNumber = ""
    @Published var bsbNumber = ""
    
    @Published var bsbError = false
    @Published var dismissSheet = false
    @Published var defaultRes: DefaultResponse?
    @Published var showSuccess = false
    @Published var alertMsg = ""

    
    func submitBankInfo(){
        let json = BankInfoRequest(bank_name: self.bankName, bank_account_holder_name: self.holdersName, bank_account_no: self.accountNumber, bank_sort_code: self.bsbNumber)
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.bank, body: json)
        .sink { completion in
            self.loading = false
            switch completion {
            case .finished:
                print("success")
                self.alertTitle = "\(self.defaultRes?.title ?? "")"
                self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                self.showSuccess = true
                
                
            case .failure(let error):
                let err = NetworkConnection().handleNetworkError(error)
                self.alertTitle = "\(err.title ?? "")"
                self.alertMessage =  "\(err.message ?? "")"
                self.showAlert = true
            }
        } receiveValue: { (response: DefaultResponse) in
            print(response, "resssss")
            self.defaultRes = response
            self.shouldDismissView = true
        }
        .store(in: &cancellables)
    }
}
