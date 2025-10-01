//
//  SupportFormVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 13/07/2022.
//

import Foundation
import FirebaseFirestore
import UIKit

class SupportFormVM: BaseObservableObject {
    @Published var bookingId = ""
    @Published var message = ""
    @Published var subject = ""
    @Published var navigate = false
    
    @Published var goBack = false
    @Published var responseData: DefaultResponse?
    @Published var selection = "Feedback"
    @Published var showSuccess = false
    
    func submitForm(){
//        if !validateInput() {return}
        self.loading = true
        let json = SupportFormRequest(subject: self.subject, booking_id: self.bookingId, comment: self.message, type: self.selection.lowercased().replacingOccurrences(of: " ", with: "_"))
        NetworkManager.shared.POST(to: APIEndpoints.support, body: json)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "\(self.responseData?.title ?? "")"
                    self.alertMessage =  "\(self.responseData?.message ?? "")"
                    self.showSuccess.toggle()
                    
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.responseData = response;
                
            }
            .store(in: &cancellables)
        
    }
    
    func validateInput() -> Bool{
        if self.bookingId.isEmpty || self.message.isEmpty {
            let alertController = GlobalAlertController(title: "Missing", message: "Please fill all the fields", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: nil))
            alertController.presentGlobally(animated: true, completion: nil)
            return false
        }
        return true
    }
    
}
