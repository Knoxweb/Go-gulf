//
//  ScheduleListVM.swift
// SlyykDriver
//
//  Created by Office on 19/07/2022.
//

import Foundation
import UIKit
import Firebase

class ScheduleListVM: BaseObservableObject {
    @Published var myJobsList: [CurrentBookingModel] = []
    @Published var defaultRes: DefaultResponse?
    
    private var MyPickupsListener: ListenerRegistration?
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    var locationManager = LocationTracker.shared.locationManager
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    
    
    func stopListener() {
        MyPickupsListener?.remove()
        MyPickupsListener = nil
        
    }
    
    @MainActor public func dispatchListener() {
        let profileDocument = parentCollection.collection("my_jobs")
        self.loading = true
        MyPickupsListener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.loading = false
                guard let self = self else { return }
                self.myJobsList = []
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(profileDocument.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var jobs: [CurrentBookingModel] = []
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat = "dd MMM yyyy, hh:mm a"

                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let job = try decoder.decode(CurrentBookingModel.self, from: jsonData)
                            jobs.append(job)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                    self.myJobsList = jobs

                } else {
                    print("No jobs found.")
                }
            }
    }
    
    
    func startJob(id: Int?){
        self.loading = true
        let param = CoordinatorModel(lat: userLatitude, lng: userLongitude)
        NetworkManager.shared.POST(to: APIEndpoints.startJob(id ?? 0), body: param)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print("\(self.defaultRes as Any)")
                        self.appRoot?.currentRoot = .currentRideScreen
                        self.router?.popToRoot()
                    
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

}

