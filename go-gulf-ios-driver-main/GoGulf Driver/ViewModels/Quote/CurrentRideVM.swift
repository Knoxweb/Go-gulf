//
//  CurrentRideVM.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 29/06/2022.
//

import Foundation
import UIKit
import Firebase
import FirebaseDatabase

class CurrentRideVM: BaseObservableObject {
    @Published var currentData: CurrentBookingModel?
    @Published var endTripData: EndTripResponse?
    @Published var defaultRes: DefaultResponse?
    @Published var navigateToRating = false
    @Published var title = ""
    @Published var fromLat: Double = 0
    @Published var fromLng: Double = 0
    @Published var toLat: Double = 0
    @Published var toLng: Double = 0
    @Published var buttonLabel = ""
    @Published var mode = ""
    @Published var noShowAlert = false
    @Published var showNoShowButton = false
    @Published var navigateToDashboard = false
    
    @Published var showGoogleNavigation = true
    @Published var showPickupDateTime = true
    @Published var noShowUpButton: Bool = false
    @Published var onLoaded = true
    @Published var showOTPAlert = false
    @Published var NfromLat:Double = 0
    @Published var NfromLng:Double = 0
    @Published var NtoLat:Double = 0
    @Published var NtoLng:Double = 0
    @Published var bookingId = ""
    @Published var otpCode = ""
    @Published var isLoading = false
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    private var listener: ListenerRegistration?
    
    @Published var locationManager = LocationManager()
    var userLatitude: Double {
        return locationManager.lastLocation?.coordinate.latitude ?? -33.868820
    }
    
    var userLongitude: Double {
        return locationManager.lastLocation?.coordinate.longitude ?? 151.209290
    }
    
    @Published var otpField = "" {
        didSet {
            isNextTypedArr = Array(repeating: false, count: 4)
            guard otpField.count <= 4,
                  otpField.last?.isNumber ?? true else {
                otpField = oldValue
                return
            }
            if otpField.count < 4 {
                isNextTypedArr[otpField.count] = true
            }
        }
    }
    var otp1: String {
        guard otpField.count >= 1 else {
            return ""
        }
        return String(Array(otpField)[0])
    }
    var otp2: String {
        guard otpField.count >= 2 else {
            return ""
        }
        return String(Array(otpField)[1])
    }
    var otp3: String {
        guard otpField.count >= 3 else {
            return ""
        }
        return String(Array(otpField)[2])
    }
    var otp4: String {
        guard otpField.count >= 4 else {
            return ""
        }
        return String(Array(otpField)[3])
    }
    
    @Published var isNextTypedArr = Array(repeating: false, count: 4)
    
    
    @Published var isEditing = false {
        didSet {
            isNextTypedArr = Array(repeating: false, count: 4)
            if isEditing && otpField.count < 4 {
                isNextTypedArr[otpField.count] = true
            }
        }
    }
    
    
    public func initialize() {
        Task (priority: .medium){
            await getCurrentBooking()
        }
    }
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    
    internal func updatePosition() {
        let body = CoordinatesModel(lat: "\(userLatitude)", lng: "\(userLongitude)")
        NetworkManager.shared.POST(to: APIEndpoints.udpatePosition, body: body)
            .sink { completion in
                switch completion {
                case .finished:
                    print(self.defaultRes?.message ?? "")
                case .failure(let error):
                    print(error.localizedDescription)
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    
    public func openWazeWithDirections(toLat: Double, toLng: Double) {
           guard let url = URL(string: "https://www.waze.com/ul?ll=\(toLat),\(toLng)&navigate=yes") else {
               return
           }
           if UIApplication.shared.canOpenURL(url) {
               UIApplication.shared.open(url, options: [:], completionHandler: nil)
           } else {
               print("Waze is not installed.")
           }
       }
    
    
    @MainActor
    public func getCurrentBooking() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("current_job")
        
        listener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
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
                self.currentData = try JSONDecoder().decode(CurrentBookingModel.self, from: data)
                print("listennninnnnnn")
                 if self.currentData?.current_status == "dod" {
                    self.noShowUpButton = true
                }
                else {
                    self.noShowUpButton = false
                }
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    internal func noShowUp(for id: Int?, tabRouter: TabRouter) {
        self.loading = true
        let body = CoordinatesModel(lat: "\(userLatitude)", lng: "\(userLongitude)")
        NetworkManager.shared.POST(to: APIEndpoints.noShow(id ?? 0), body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.defaultRes?.message ?? "")
                    self.appRoot?.currentRoot = .tabs
//                    tabRouter.selectedTab = .offers
                    self.router?.popToRoot()
                case .failure(let error):
                    self.noShowAlert = false
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    
    //    internal func updatePosition() {
    //        let body = CoordinatesModel(lat: "\(userLatitude)", lng: "\(userLongitude)")
    //        NetworkManager.shared.POST(to: APIEndpoints.udpatePosition, body: body)
    //            .sink { completion in
    //                switch completion {
    //                case .finished:
    //                    print(self.defaultRes?.message ?? "")
    //                case .failure(let error):
    //                    print(error.localizedDescription)
    //                }
    //            } receiveValue: { (response: DefaultResponse) in
    //                self.defaultRes = response
    //            }
    //            .store(in: &cancellables)
    //    }
    //
    
    internal func DOD(for jobId: Int?) {
        let body = CoordinatesModel(lat: "\(userLatitude)", lng: "\(userLongitude)")
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.DOD(jobId ?? 0), body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.defaultRes?.message ?? "")
                    //                    self.showOTPAlert = true
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    
    internal func POB(for jobId: Int?) {
        let body = CoordinatesModelWithOTP(lat: "\(userLatitude)", lng: "\(userLongitude)", otp_code: otpField)
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.POB(jobId ?? 0), body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.defaultRes?.message ?? "")
                    self.showOTPAlert = false
                case .failure(let error):
                    self.showOTPAlert = false
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    
    public func endTrip(for jobId: Int?) {
        let body = CoordinatesModel(lat: "\(userLatitude)", lng: "\(userLongitude)")
        self.loading = true
        self.isLoading = true
        NetworkManager.shared.POST(to: APIEndpoints.EndTrip(jobId ?? 0), body: body)
            .sink { completion in
                switch completion {
                case .finished:
                    self.loading = false
                    self.isLoading = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.05) {
                        print("RIDIIDIDIIEIEIE endndndndndn")
                        self.appRoot?.currentRoot = .ratingScreen(bookingId: "\(Int(self.currentData?.id ?? 0))")
                        self.router?.popToRoot()
                    }
                    
                case .failure(let error):
                    self.loading = false
                    self.isLoading = true
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    
    func navigateOnGoogleMap(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double) {
        let urlGoogleMap : URL = URL(string: "comgooglemaps://?saddr=\(fromLat),\(fromLng)&daddr=\(toLat),\(toLng)&directionsmode=driving&mode=driving&directionsmode=navigation&dir_action=navigate")!
        
        if UIApplication.shared.canOpenURL(urlGoogleMap) {
            UIApplication.shared.open(urlGoogleMap, options: [:], completionHandler: nil)
            
        } else {
            let urlString = URL(string:"comgooglemaps://?saddr=\(fromLat),\(fromLng)&daddr=\(toLat),\(toLng)&directionsmode=driving&mode=driving&directionsmode=navigation&dir_action=navigate")
            UIApplication.shared.open(urlString!, options: [:], completionHandler: nil)
        }
    }
}


struct CurrentRideRequestMode: Codable {
    let lat, lng: Double?
    let ride_otp: String?
}
