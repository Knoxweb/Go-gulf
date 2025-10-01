//
//  VehicleListVM.swift
// SlyykDriverDriver
//
//  Created by Office on 29/06/2022.
//

import Foundation
import UIKit
import Firebase

class vehicleListedVM: BaseObservableObject {
    @Published var defaultResponse: DefaultResponse?
    @Published var alertMsg = ""
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    private var listener: ListenerRegistration?
    @Published var fleetData: [FBFleetList]?
    @Published var defaultRes: DefaultResponse?
    @Published var showSuccess:Bool = false
    
    
    public func initialize() {
        Task(priority: .medium) {
            await getFleetList()
        }
    }
    
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    @MainActor
    public func getFleetList() {
        let profileDocument = parentCollection.collection("fleets")
        self.loading = true
        listener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.loading = false
                guard let self = self else { return }
                self.fleetData = []
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(profileDocument.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var fleets: [FBFleetList] = []
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let fleet = try decoder.decode(FBFleetList.self, from: jsonData)
                            fleets.append(fleet)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                    self.fleetData = fleets
                } else {
                    print("No cards found.")
                }
            }
        
    }
    
    func updateFleetStatus(id: Int?, status: Bool){
        self.loading = true
        let body = FleetActiveStatus(status: status ? 1 : 0)
        NetworkManager.shared.POST(to: APIEndpoints.fleetStatus("\(id ?? 0)"), body: body)
        .sink { completion in
            self.loading = false
            switch completion {
            case .finished:
                print(self.defaultRes?.message as Any, "Message")
//                self.alertTitle = "\(self.defaultRes?.title ?? "")"
//                self.alertMessage =  "\(self.defaultRes?.message ?? "")"
//                self.showAlert.toggle()
                
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


struct FleetActiveStatus: Codable {
    let status: Int?
}
