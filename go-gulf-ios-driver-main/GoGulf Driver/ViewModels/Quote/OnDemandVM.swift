//
//  CurrentRideVM.swift
// SlyykDriverDriver
//
//  Created by Prabin Phasikawo on 6/26/22.
//

import Foundation
import UIKit
import FirebaseFirestore
import FirebaseDatabase

class OnDemandVM: BaseObservableObject {
    @Published var jobAcceptResponse: CurrentRideResponse?
    @Published var jobRejectResponse: AcceptJobResponse?
    @Published var navigate = false
    
    @Published var navigateToCurrentRide = false
    
    var locationManager = LocationTracker.shared.locationManager
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    
    
    func acceptJob(bookingId: String){
        //        self.getAssignJobs(bookingId: bookingId)
        print("bookingidddddddddd", bookingId)
        self.loading = true
        let json = AcceptJobRequest(booking_id: bookingId, type: "on_demand", lat: userLatitude, lng: userLongitude)
//        NetworkManager.shared.post(urlString: "driver/accept-job", header: nil, encodingData: json) { (RESPONSE_DATA:CurrentRideResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.jobAcceptResponse = RESPONSE_DATA;
//                self.loading = false
//                if(URL_RESPONSE?.statusCode == 200){
//                    self.getAssignJobs(bookingId: bookingId)
//                }
//                else{
//                    let alertController = GlobalAlertController(title: String(self.jobAcceptResponse?.title ?? ""), message: String(self.jobAcceptResponse?.message ?? ""), preferredStyle: .alert)
//                    alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: {_ in
//                        self.navigate = true
//                    }))
//                    alertController.presentGlobally(animated: true, completion: nil)
//                }
//            }
//        }
    }
    
    func getAssignJobs(bookingId: String) {
        let db = Firestore.firestore()
        let docRef = db.collection("assign_jobs").whereField("booking_id", isEqualTo: bookingId as Any)
        docRef.getDocuments { (document, error) in
            guard error == nil else {
                print("error", error ?? "")
                return
            }
            for document in document!.documents {
                print(document.data(), "doc daaataaaaaaaaaaaa")
                document.reference.delete()
            }
            DispatchQueue.main.async {
                self.navigateToCurrentRide = true
                return;
            }
        }
    }
    
    func rejectJob(bookingId: String, docId: String, auto: Bool){
        let json = RejectJobRequest(booking_id: bookingId, type: "on_demand", auto: auto)
        self.loading = true
//        NetworkManager.shared.post(urlString: "driver/reject-job", header: nil, encodingData: json) { (RESPONSE_DATA:AcceptJobResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.jobRejectResponse = RESPONSE_DATA
//                print(self.jobRejectResponse as Any, "rejected job")
//                self.removeFBDocument(docId: docId)
//                self.navigate = true
//                self.loading = false
//                if(URL_RESPONSE?.statusCode == 200){
//                    
//                }
//                if(!auto) {
//                    let alertController = GlobalAlertController(title: String(self.jobRejectResponse?.title ?? ""), message: String(self.jobRejectResponse?.message ?? ""), preferredStyle: .alert)
//                    alertController.addAction(UIAlertAction(title: "ok", style: .default, handler: {_ in
//                        
//                    }))
//                    alertController.presentGlobally(animated: true, completion: nil)
//                }  
//            }
//        }
    }
    
    func removeFBDocument(docId: String) {
        db.collection("assign_jobs").document(docId).delete() { err in
            if let err = err {
                print("Error removing document: \(err)")
            }
            else {
            }
        }
    }
    
    
    
    func showAlert(title: String, msg: String) {
        let alertController = GlobalAlertController(title: title, message: msg, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: nil))
        alertController.presentGlobally(animated: true, completion: nil)
    }
}
