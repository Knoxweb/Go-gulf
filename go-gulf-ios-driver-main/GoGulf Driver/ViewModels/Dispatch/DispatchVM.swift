//
//  DispatchVM.swift
//  SlyykDriver
//
//  Created by Office on 18/01/2023.
//

import Foundation
import SwiftUI
import FirebaseFirestore
import FirebaseDatabase
import AudioToolbox
import AVFoundation


class DispatchVM: BaseObservableObject {
    @Published var navigate = false
    @Published var JobsModel: [DispatchJobModel] = []
    @Published var scheduledData: [DispatchJobModel] = []
    @Published var currentData: CurrentRideResponse?
    @Published var isOnline = true
    @Published var navigateToCurrentRide = false
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var TimeModel: [TimerModel] = []
    @Published var initialRead = true
    @Published private var vibrator = 1
    let vibration = Timer.publish(every: 2, tolerance: 0.5, on: .main, in: .common).autoconnect()
    @Published var activeStatus = false
    private var StandByListener: ListenerRegistration?
    var locationManager = LocationTracker.shared.locationManager
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    @Published var slideOffset: CGFloat = 0
    @Published var dispatchOffset: CGFloat = 0
    @Published var jobType = ["standby", "schedule"]
    @Published var selection = "standby"
    private var listener: ListenerRegistration?
    private var profileListener: ListenerRegistration?
    @Published var avaialbleJobsList: [AvailableJobModelData] = []
    @Published var resp: DefaultResponse?
    @Published var defaultRes: DefaultResponse?
    @Published var showSuccess = false
    @Published var profileData: FBProfileData?
    @Published var statusChanged = false
    
    public func initialize() {
        Task (priority: .medium){
            await self.dispatchListener()
            await getProfileData()
        }
    }
    
    func stopListener() {
        listener?.remove()
        listener = nil
        self.profileListener?.remove()
        self.profileListener = nil
    }
    
    @MainActor
     func getProfileData() {
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
                self.activeStatus = self.profileData?.status == "online" ? true : false
//                self.statusChanged = self.activeStatus
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
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
    
    
    @MainActor
    func setOnlineStatus() {
        let param = OnlineModel(is_available: activeStatus ? 1 : 0)
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.online, body: param)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
                    self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                    self.showAlert = true
                    self.statusChanged.toggle()
                    
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response;
            }
            .store(in: &cancellables)
    }
    
    
    func acceptJob(id: Int?, type: String?, tabRouter: TabRouter){
        self.loading = true
        let param = CoordinatorModel(lat: userLatitude, lng: userLongitude)
        NetworkManager.shared.POST(to: APIEndpoints.acceptJob(id ?? 0), body: param)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print("\(self.defaultRes as Any)")
//                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
//                    self.alertMessage =  "\(self.defaultRes?.message ?? "")"
//                    self.showSuccess = true
                    
                    if type == "on_demand"{
                        self.appRoot?.currentRoot = .currentRideScreen
                        self.router?.popToRoot()
                    }
                    else if type == "scheduled" {
//                        self.router?.navigateTo(.pickups)
                        tabRouter.selectedTab = .ride
                    }
                    
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response;
            }
            .store(in: &cancellables)
        
    }
    
    
    func rejectJob(id: Int?, type: String?, tabRouter: TabRouter){
        self.loading = true
        let param = CoordinatorModel(lat: userLatitude, lng: userLongitude)
        NetworkManager.shared.POST(to: APIEndpoints.jobReject(id ?? 0), body: param)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print("\(self.defaultRes as Any)")
                    
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response;
            }
            .store(in: &cancellables)
        
    }
    
    
    @MainActor public func dispatchListener() {
        let profileDocument = parentCollection.collection("dispatches")
        self.loading = true
        listener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.loading = false
                guard let self = self else { return }
                self.JobsModel = []
                self.scheduledData = []
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(profileDocument.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var jobs: [DispatchJobModel] = []
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat = "dd MMM yyyy, hh:mm a" 

                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let job = try decoder.decode(DispatchJobModel.self, from: jsonData)
                            jobs.append(job)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }

                    // Filter and sort jobs
                    self.JobsModel = jobs
                        .filter { $0.type == "on_demand" }
                        .sorted {
                            guard let date1 = dateFormatter.date(from: $0.pickup_date_time ?? ""),
                                  let date2 = dateFormatter.date(from: $1.pickup_date_time ?? "") else { return false }
                            return date1 > date2
                        }

                    self.scheduledData = jobs
                        .filter { $0.type == "scheduled" }
                        .sorted {
                            guard let date1 = dateFormatter.date(from: $0.pickup_date_time ?? ""),
                                  let date2 = dateFormatter.date(from: $1.pickup_date_time ?? "") else { return false }
                            return date1 > date2
                        }

                } else {
                    print("No jobs found.")
                }
            }
    }

    
}


struct OnlineModel: Codable {
    let is_available: Int?
}


struct CoordinatorModel: Codable {
    let lat: Double?
    let lng: Double?
}
