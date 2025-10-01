//
//  BookingVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 29/06/2022.
//

import Foundation
import UIKit
import Firebase
import CoreLocation


struct shortcutModel {
    var docId: String?
    var id: Int?
    var title: String?
    var icon: String?
    var address: String?
    var lat: Double?
    var lng: Double?
}

class BookingVM: BaseObservableObject {
    @Published var fcmResponse: FCMTokenResponse?
    @Published var nearbyDriverResponse: NearbyDriverResponse?
    @Published var navigate = false
    @Published var navigateTo = false
    @Published var pageUrl = ""
    @Published var bookingId = ""
    @Published var navigateToLandingPage = false
    @Published var showRating = false
    @Published var timerManager = TimerManager()
    private var listener: ListenerRegistration?
    private var discountListener: ListenerRegistration?
    private var currentRideListener: ListenerRegistration?
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    let token = (UserDefaults.standard.string(forKey: "accessToken") ?? "");
    let identity = UserDefaults.standard.string(forKey: "identity")
    @Published var quoteData: QuoteResponseData?
    @Published var currentData: CurrentBookingModel?
    @Published var discount: DiscountPromo?
    
    public func initialize() {
        Task(priority: .medium) {
            await checkBookingrequest()
            await getCurrentBooking()
            await getDiscountOffer()
        }
    }
    
    func updateToken(){
        let token = UserDefaults.standard.string(forKey: "fcmToken")
        let json = UpdateFCMToken(device_type: "iphone", device_token: token ?? "")
        
        NetworkManager.shared.POST(to: "passenger/device-token", body: json)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print("success")
                case .failure(let error):
                    print("error", error)
                }
            } receiveValue: { (response: FCMTokenResponse) in
                self.fcmResponse = response;
            }
            .store(in: &cancellables)
    }
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
        self.currentRideListener = nil
        self.currentRideListener?.remove()
        self.discountListener?.remove()
        self.discountListener = nil
    }
    
    
    @MainActor
    private func checkBookingrequest() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("request")
        print(documentReference.path, "documentReference")
        
        listener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            self?.quoteData = nil
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
                self.quoteData = try JSONDecoder().decode(QuoteResponseData.self, from: data)
                switch self.quoteData?.status {
                case "pending" :
                    self.router?.navigateTo(.requestingScreen(quoteData: self.quoteData))

                default:
                    print("here")
                }
            } catch {
                print("Error decoding profile document: BookNow \(error)")
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
//                self.appRoot?.currentRoot = .currentRideScreen
//                self.router?.popToRoot()
            } catch {
                print("Error decoding profile document: Dashboard \(error)")
            }
        }
    }
    
    @MainActor
    private func getDiscountOffer() {
        self.loading = true
        let documentReference = Firestore.firestore().collection("discount_promotions")
        
        self.loading = true
        discountListener = documentReference
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.loading = false
                guard let self = self else { return }
                self.discount = nil
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(documentReference.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var jobs: [DiscountPromo] = []
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat = "dd MMM yyyy, hh:mm a"
                    
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let job = try decoder.decode(DiscountPromo.self, from: jsonData)
                            jobs.append(job)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                    self.discount = jobs.first ?? nil
                } else {
                    print("No jobs found.")
                }
            }
    }
}
