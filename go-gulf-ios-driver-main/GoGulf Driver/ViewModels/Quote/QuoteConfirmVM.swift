//
//  quoteConfirmVM.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/24/22.
//

import Foundation
import UIKit

import Firebase

class QuoteConfirmVM: BaseObservableObject {
    @Published var responseData: QuoteConfirmResponse?
    @Published public var expanded: Bool = false
    
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
//    Flights Info
    @Published public var flightInfo:Bool = false
    @Published public var flightName = ""
    @Published public var flightNumber = ""
//    Capacity View
    @Published public var capacity:Bool = false
    @Published public var passenger: Int = 1
    @Published public var pet: Int = 0
    @Published public var wheelChair: Int = 0
//    Lead Passenger View
    @Published public var leadPassenger:Bool = false
    @Published public var fullname = ""
    @Published public var phone = ""
    @Published public var navigate = false
    
    func confirmQuoteSubmit(quoteId: String, fleetId: Int){
        let jsonData = ConfirmQuoteRequest(quote_id: quoteId, fleet_id: fleetId, driver_note: self.addNote)
//        NetworkManager.shared.post(urlString: "quote/request-job", header: nil, encodingData: jsonData) { (RESPONSE_DATA:QuoteConfirmResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.responseData = RESPONSE_DATA;
//                print(self.responseData as Any, "quoteConfirm")
//                if(URL_RESPONSE?.statusCode == 200){
//                    self.showAlertMessage(title: String(self.responseData!.title), msg: String(self.responseData!.message))
//                    self.showAlert.toggle()
//                }
//                else{
//                    self.showAlertMessage(title: String(self.responseData!.title), msg: String(self.responseData!.message))
//                }
//            }
//        }
    }
    
    func showAlertMessage(title: String, msg: String) {
        let alertController = GlobalAlertController(title: String(self.responseData!.title), message: String(self.responseData!.message), preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: {_ in
            self.navigate.toggle()
        }))
        alertController.presentGlobally(animated: true, completion: nil)
    }
}
