//
//  ScheduleJobVM.swift
// SlyykDriverDriver
//
//  Created by Office on 26/07/2022.
//


import Foundation
import UIKit
import Combine
import FirebaseFirestore
import FirebaseDatabase

class ScheduleJobVM: BaseObservableObject {
    @Published var jobAcceptResponse: CurrentRideResponse?
    @Published var jobRejectResponse: AcceptJobResponse?
    @Published var navigate = false
    var locationManager = LocationTracker.shared.locationManager
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    

    func acceptJob(bookingId: String){
        self.loading = true
        let json = AcceptJobRequest(booking_id: bookingId, type: "scheduled", lat: userLatitude, lng: userLongitude)
//        NetworkManager.shared.post(urlString: "driver/accept-job", header: nil, encodingData: json) { (RESPONSE_DATA:CurrentRideResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.jobAcceptResponse = RESPONSE_DATA;
//                if(URL_RESPONSE?.statusCode == 200){
//                    let alertController = GlobalAlertController(title: String(self.jobAcceptResponse?.title ?? ""), message: String(self.jobAcceptResponse?.message ?? ""), preferredStyle: .alert)
//                    alertController.addAction(UIAlertAction(title: "ok", style: .default, handler: {_ in
//                            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
//                                self.navigate = true
//                                self.loading = false
//                            }
//                        }))
//                        alertController.presentGlobally(animated: true, completion: nil)
//                }
//                else{
//                    self.showAlert(title: self.jobAcceptResponse?.title ?? "", msg: self.jobAcceptResponse?.message ?? "", type: "error")
//                }
//            }
//        }
    }
    
    func rejectJob(bookingId: String){
        let json = AcceptJobRequest(booking_id: bookingId, type: "available_pickups", lat: userLatitude, lng: userLongitude)
        self.loading = true
//        NetworkManager.shared.post(urlString: "driver/reject-job", header: nil, encodingData: json) { (RESPONSE_DATA:AcceptJobResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.jobRejectResponse = RESPONSE_DATA
//                print(self.jobRejectResponse as Any, "rejected job")
//                if(URL_RESPONSE?.statusCode == 200){
//                    self.showAlert(title: self.jobRejectResponse?.title ?? "", msg: self.jobRejectResponse?.message ?? "", type: "success")
//                }
//                else{
//                    self.showAlert(title: self.jobRejectResponse?.title ?? "", msg: self.jobRejectResponse?.message ?? "", type: "error")
//                }
//            }
//        }
    }
    
    
    func showAlert(title: String, msg: String, type: String) {
        let alertController = GlobalAlertController(title: String(title), message: String(msg), preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "ok", style: type == "success" ? .default : .destructive, handler: {_ in
            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                self.shouldDismissView = true
                self.loading = false
            }
        }))
        alertController.presentGlobally(animated: true, completion: nil)
    }
}

