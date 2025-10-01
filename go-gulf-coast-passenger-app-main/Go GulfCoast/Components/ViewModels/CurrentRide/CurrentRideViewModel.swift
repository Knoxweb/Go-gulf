//
//  CurrentRideViewModel.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 06/07/2022.
//

import Foundation
import Firebase
import UIKit
//import SwiftyJSON


class CurrentRideVM: BaseObservableObject {
    @Published var navigateToRating = false
    @Published var navigateToBooking = false
    
    @Published var defaultRes: DefaultResponse?
    @Published var firebaseResponse: FirebaseCurrentResponse?
    @Published var response: DriverCoordinatesResponse?
    @Published var navigateToPaymentOption = false
    @Published var title = ""
    @Published var mode = ""
    @Published var timerManager = TimerManager()
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var defaultResponse: DefaultResponse?
    private var listener: ListenerRegistration?
    private var OTPListener: ListenerRegistration?
    @Published var currentData: CurrentBookingModel?
    @Published var showOTPAlert = false
    
    @Published var otpData: OTPCodeModel?
    
    public func initialize() {
        Task(priority: .medium) {
            await getCurrentBooking()
            await checkOTP()
        }
    }
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
        self.OTPListener?.remove()
        self.OTPListener = nil
    }
    
    func navigateOnGoogleMap(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double) {
        let urlGoogleMap : URL = URL(string: "comgooglemaps://?saddr=\(fromLat),\(fromLng)&daddr=\(toLat),\(toLng)&directionsmode=driving")!
        
        if UIApplication.shared.canOpenURL(urlGoogleMap) {
            UIApplication.shared.open(urlGoogleMap, options: [:], completionHandler: nil)
            
        } else {
            let urlString = URL(string:"http://maps.google.com/?saddr=\(fromLat),\(fromLng)&daddr=\(toLat),\(toLng)&directionsmode=driving")
            UIApplication.shared.open(urlString!, options: [:], completionHandler: nil)
        }
    }
    
    @MainActor
    private func getCurrentBooking() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("current_booking")
        
        listener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            guard let self = self else { return }
            if let error = error {
                print("Error fetching profile document: \(error)")
                return
            }
            guard let document = documentSnapshot else {
                print("Profile document does not exist")
                self.appRoot?.currentRoot = .bookNowScreen
                self.router?.popToRoot()
                return
            }
            do {
                guard document.exists else {
                    self.appRoot?.currentRoot = .bookNowScreen
                    self.router?.popToRoot()
                    throw NSError(domain: "Firestore", code: -1, userInfo: [NSLocalizedDescriptionKey: "Document does not exist"])
                }
                let data = try JSONSerialization.data(withJSONObject: document.data() ?? [:], options: [])
                self.currentData = try JSONDecoder().decode(CurrentBookingModel.self, from: data)
                if self.currentData?.current_status == "completed" {
                    self.appRoot?.currentRoot = .ratingScreen(bookingId: "\(Int(self.currentData?.id ?? 0))")
                    self.router?.popToRoot()
//                    self.router?.navigateTo(.ratingScreen(bookingId: "\(Int(self.currentData?.id ?? 0))"))
                }
                else if self.currentData?.current_status == "dod" {
                    self.showOTPAlert = true
                }
                else {
                    self.showOTPAlert = false
                }
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    @MainActor
    private func checkOTP() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("pob_otp")
        
        OTPListener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
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
                self.otpData = try JSONDecoder().decode(OTPCodeModel.self, from: data)
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
}
