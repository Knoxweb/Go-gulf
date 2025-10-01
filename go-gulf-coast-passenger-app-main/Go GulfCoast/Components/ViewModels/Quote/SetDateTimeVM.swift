//
//  SetDateTime.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//

import Foundation
import UIKit

class SetDateTimeVM: BaseObservableObject {
    @Published var responseData: QuoteResponse?
    @Published public var date = Date.now
    @Published public var bookingType: String = "on_demand"
    @Published public var currentDate = Date.now
    @Published public var bookingDate = Date.now
    @Published public var returnDate = Date.now
    @Published public var returnBookingDate = Date.now
    @Published public var makeReturnJourney = false
//    @Published public var scheduled = false
    @Published public var returnBooking = false
    @Published public var passenger: Int = 1
    @Published public var pet: Int = 0
    @Published public var wheelChair: Int = 0
    @Published public var navigate = false
    @Published var choosedFleet: FleetList?
    @Published public var expanded: Bool = false
    
    @Published public var discount = ["-20", "-10", "0", "10", "20"]
    @Published public var discountSelected = "0"
    
    
    
        func submitQuoteRequest(pickup: String, pLat: String, pLng: String, dropoff: String, dLat: String, dLng: String){
            let body = QuoteRequest(
                drop_address_lat: dLat,
                pickup_date: getFormattedDate(from: self.date),
                pickup_address_lat: pLat,
                drop_address_lng: dLng,
                drop_address: dropoff,
                pickup_address: pickup,
                pickup_time: get24HourFormatTime(from: self.date),
                trip_type: self.bookingType,
                pickup_address_lng: pLng,
                exp_discount: Int(self.discountSelected),
                passenger_count: self.passenger
            )
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.calculateFare, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print("")
                    self.router?.navigateTo(.vehicleSelection(data: self.responseData, passenger: self.passenger))
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage = "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: QuoteResponse) in
                self.responseData = response
                print(self.responseData as Any, "responsnsnnsnsenen")
            }
            .store(in: &cancellables)
    }
//
//    
//    func submitQuoteRequest(pickup: String, pLat: Double, pLng: Double, via: String, vLat: Double, vLng: Double, dropoff: String, dLat: Double, dLng: Double, appRouter: Router){
//        self.loading = true
//        let json = QuoteRequest(booking_type: self.bookingType, return_pickup_datetime: "\(self.returnDate)", pickup_datetime: "\(self.date)", from_location: pickup, from_lat: pLat, from_lng: pLng, to_location: dropoff, to_lat: dLat, to_lng: dLng, country: "\(Locale.current)", discount: self.discountSelected)
//        
//        NetworkManager.shared.POST(to: "quote", body: json)
//            .sink { completion in
//                self.loading = false
//                switch completion {
//                case .finished:
//                    appRouter.navigateTo(.vehicleSelection(data: self.responseData))
//                case .failure(let error):
//                    let err = NetworkConnection().handleNetworkError(error)
//                    let alertController = GlobalAlertController(title: String(err.title ?? ""), message: String(err.message ?? ""), preferredStyle: .alert)
//                    alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: nil))
//                    alertController.presentGlobally(animated: true, completion: nil)
//                }
//            } receiveValue: { (response: QuoteResponse) in
//                self.responseData = response;
//            }
//            .store(in: &cancellables)
//    }
}
