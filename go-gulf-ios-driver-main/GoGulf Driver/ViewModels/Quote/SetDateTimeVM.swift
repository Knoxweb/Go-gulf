//
//  SetDateTime.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/24/22.
//

import Foundation
import UIKit

class SetDateTimeVM: BaseObservableObject {
    @Published var responseData: QuoteResponse?
    @Published public var date = Date.now
    @Published public var bookingType: String = "on_demand"
    @Published public var currentDate = Date.now
    @Published public var bookingDate = Date.now
    @Published public var returnBookingDate = Date.now
    @Published public var makeReturnJourney = false
    @Published public var scheduled = false
    @Published public var returnBooking = false
    
    @Published public var navigate = false
    
    
    func submitQuoteRequest(pickup: String, pLat: Double, pLng: Double, via: String, vLat: Double, vLng: Double, dropoff: String, dLat: Double, dLng: Double){
        let json = QuoteRequest(booking_type: self.bookingType, pickup_datetime: "\(self.date)", from_location: pickup, from_lat: pLat, from_lng: pLng, to_location: dropoff, to_lat: dLat, to_lng: dLng)
        print(json, "jsosnnsonsinsisnisnins");
//        NetworkManager.shared.post(urlString: "quote", header: nil, encodingData: json) { (RESPONSE_DATA:QuoteResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.responseData = RESPONSE_DATA;
//                print(self.responseData as Any, "quoteResponse -- - -- - - - - -")
//                if(URL_RESPONSE?.statusCode == 200){
//                    self.navigate.toggle()
//                }
//                else{
//                    let alertController = GlobalAlertController(title: String(self.responseData!.title), message: String(self.responseData!.message), preferredStyle: .alert)
//                    alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: nil))
//                    alertController.presentGlobally(animated: true, completion: nil)
//                }
//            }
//        }
    }
}
