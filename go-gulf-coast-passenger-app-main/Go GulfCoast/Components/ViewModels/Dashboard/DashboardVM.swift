//
//  DashboardVM.swift
//   GoGulf
//
//  Created by Mac on 6/19/22.
//

import Foundation
import UIKit
import SwiftUI
import FirebaseFirestore

class DashboardVM: BaseObservableObject {
    
    @Published var defaultRes: DefaultResponse?
    @Published var showCardSheet = false
    @Published var disableClose = true
    @Published var base64Image = ""
    @Published var image: UIImage?
    @Published var navigateToLandingPage = false
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var defaultResponse: DefaultResponse?
    private var listener: ListenerRegistration?
    private var upcomingListener: ListenerRegistration?
    private var shortcutListener: ListenerRegistration?
    @Published var profileData: FBProfileData?
    @Published var profileImage: UIImage = UIImage()
    @Published var addrData: [AddressModel]?
    @Published var choosedAddr: AddAddressModel?
    @Published var showSuccess = false
    private var currentRideListener: ListenerRegistration?
    @Published var currentData: CurrentBookingModel?
    @Published var upcomingData: CurrentBookingModel?
    
    public func initialize() {
        Task(priority: .medium) {
            await getProfileData()
            await getAddresses()
            await getCurrentBooking()
            await getUpcomingRide()
        }
    }
    
    public func stopListener() {
        self.currentRideListener?.remove()
        self.currentRideListener = nil
        self.listener?.remove()
        self.listener = nil
        self.shortcutListener?.remove()
        self.shortcutListener = nil
        self.upcomingListener?.remove()
        self.upcomingListener = nil
    }
    
    @MainActor
    private func getProfileData() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("profile")
        print(documentReference.path, "documentReference")
        
        listener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            self?.profileData = nil
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
    
    @MainActor
    private func getUpcomingRide() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("upcoming_booking")
        print(documentReference.path, "documentReference")
        
        upcomingListener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            self?.upcomingData = nil
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
                self.upcomingData = try JSONDecoder().decode(CurrentBookingModel.self, from: data)
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    
    @MainActor
    private func getCurrentBooking() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("current_booking")
        print(documentReference.path, "documentReference")
        
        currentRideListener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            self?.currentData = nil
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
                self.currentData = try JSONDecoder().decode(CurrentBookingModel.self, from: data)
            } catch {
                print("Error decoding profile document: Dashboard \(error)")
            }
        }
    }
    
    @MainActor
    public func getAddresses() {
        let profileDocument = parentCollection.collection("addresses")
        self.loading = true
        shortcutListener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.loading = false
                guard let self = self else { return }
                self.addrData = []
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(profileDocument.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var address: [AddressModel] = []
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let addr = try decoder.decode(AddressModel.self, from: jsonData)
                            address.append(addr)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                    self.addrData = address
                } else {
                    print("No cards found.")
                }
            }
        
    }
    
    public func deleteAddr(addrId: Int?) {
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.deleteAddress("\(addrId ?? 0)"), body: "")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
                    self.alertMessage = "\(self.defaultRes?.message ?? "")"
                    self.showSuccess = true
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    
    
    public func updateProfilePic() {
        let body = (key: "profile_image", image: profileImage)
        self.loading = true
        let param = ["name": "\(self.profileData?.name ?? "")", "email": "\(self.profileData?.email ?? "")"]
        
        NetworkManager.shared.UPLOAD(
            to: APIEndpoints.update,
            images: nil,
            singleImageWithPrefix: body,
            params: param
        )
        .sink { completion in
            self.loading = false
            print(completion, "")
            switch completion {
            case .finished:
                self.alertTitle = "\(self.defaultRes?.title ?? "")"
                self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                self.showAlert.toggle()
                
            case .failure(let error):
                let err = NetworkConnection().handleNetworkError(error)
                self.alertTitle = "\(err.title ?? "")"
                self.alertMessage =  "\(err.message ?? "")"
                self.showAlert.toggle()
            }
        } receiveValue: { (response: DefaultResponse) in
            self.defaultRes = response
            print(response, "resfsdfdsfdsfsdfsdfsdf")
        }
        .store(in: &cancellables)
    }
    
}
