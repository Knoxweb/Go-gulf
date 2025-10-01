//
//  AddCardVM.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/25/22.
//

import Foundation
import UIKit

class AddCardVM: BaseObservableObject {
    @Published public var responseData: AddCardResponse?
    @Published public var cardYear = "2022"
    @Published public var cardMonth: String = "01"
    @Published public var cardHolder: String = ""
    @Published public var cardNumber: String = ""
    @Published public var CVC: String = ""
    @Published public var cardSuccess = false
    
    func submitCard(){
        self.cardSuccess.toggle()
        let json = AddCardRequest(card_number: self.cardNumber, card_holder_name: self.cardHolder, card_expiry: self.cardMonth + "/" + self.cardYear, card_verification_code: self.CVC)
        print(json, "adddcarfsddddrequesttttttt")
//        NetworkManager.shared.post(urlString: "passenger/add-card", header: nil, encodingData: json) { (RESPONSE_DATA:AddCardResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.responseData = RESPONSE_DATA;
//                if(URL_RESPONSE?.statusCode == 200){
//                    self.cardSuccess.toggle()
//                }
//                else{
//                    self.showAlert(title: String(self.responseData!.title), msg: String(self.responseData!.message))
//                }
//            }
//        }
    }
    
    func showAlert(title: String, msg: String) {
        let alertController = GlobalAlertController(title: String(self.responseData!.title), message: String(self.responseData!.message), preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: nil))
        alertController.presentGlobally(animated: true, completion: nil)
    }
}
