//
//  BookingsListVM.swift
// SlyykDriver
//
//  Created by Office on 13/07/2022.
//

import Foundation
import UIKit
import Firebase

class BookingsListVM: BaseObservableObject {
    @Published var selection = "Completed"
    @Published var bookingData: [CurrentBookingModel]?
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    private var listener: ListenerRegistration?
    
    
    public func initialize() {
        Task(priority: .medium) {
            await getBookingList()
        }
    }
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    @MainActor
    public func getBookingList() {
        let profileDocument = parentCollection.collection("jobs")
        self.loading = true
        listener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.loading = false
                guard let self = self else { return }
                self.bookingData = []
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(profileDocument.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var fleets: [CurrentBookingModel] = []
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let fleet = try decoder.decode(CurrentBookingModel.self, from: jsonData)
                            fleets.append(fleet)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                    self.bookingData = fleets
                } else {
                    print("No cards found.")
                }
            }
        
    }

}

