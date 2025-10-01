//
//  TabVM.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 03/10/2024.
//


import SwiftUI
import CoreLocation
import FirebaseFirestore

class TabVM: BaseObservableObject {
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    var currentRideListener: ListenerRegistration?
//    @Published var currentRides: [FBRidesModel]?
//    @Published var currentData: FBRidesModel?
    @Published var defaultRes: DefaultResponse?
    @Published var notiCounts: NotificationCounts?
    @Published var showLocationAlert = false
    var notificationListener: ListenerRegistration?
    private var profileListener: ListenerRegistration?
    @Published var activeStatus = false
    @Published var profileData: FBProfileData?
    let locationManager = LocationTracker.shared.locationManager
    var userLatitude: Double {
        return locationManager.location?.coordinate.latitude ?? 0
    }
    var userLongitude: Double {
        return locationManager.location?.coordinate.longitude ?? 0
    }

    
    public func FBInitialize(){
        Task (priority: .medium) {
            await getProfileData()
        }
        getNotificationCounts()
    }
    
    public func stopListener() {
        currentRideListener?.remove()
        currentRideListener = nil
        self.profileListener?.remove()
        self.profileListener = nil
    }
    
    @MainActor
    internal func getProfileData() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("profile")
        print(documentReference.path, "documentReference")
        
        profileListener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
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
                self.profileData = try JSONDecoder().decode(FBProfileData.self, from: data)
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    
    func enableLocationServices() -> Bool {
        var permission = false
        let authorizationStatus: CLAuthorizationStatus
        if #available(iOS 14, *) {
            authorizationStatus = locationManager.authorizationStatus
        } else {
            authorizationStatus = CLLocationManager.authorizationStatus()
        }
        switch authorizationStatus {
        case .notDetermined:
            // Request when-in-use authorization initially
            print("Request when-in-use authorization initially")
        case .restricted, .denied:
            // Disable location features
            print("Fail permission to get current location of user Disable location features")
        case .authorizedWhenInUse:
            print("Enable basic location features when in use")
        case .authorizedAlways:
            permission = true
            LocationTracker.shared.enableMyAlwaysFeatures()
        @unknown default:
            break
        }
        return permission
    }

    private func getNotificationCounts() {
        let documentReference = parentCollection.collection("data").document("counts")
        print(documentReference.path, "documentReference")
        
        notificationListener =  documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
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
                self.notiCounts = try JSONDecoder().decode(NotificationCounts.self, from: data)
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }

}
