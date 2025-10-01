//
//  AutoCompleteVM.swift
//   GoGulf
//
//  Created by Mac on 6/23/22.
//

import Foundation
import UIKit
import Firebase

class AutoCompleteVM: BaseObservableObject {
    @Published var responseData: AutoCompleteResponse?
    @Published var quote: QuoteRequest?
    
    @Published public var pickup = ""
    @Published public var via = ""
    @Published public var dropoff = ""
    @Published public var type = ""
    @Published public var address = ""
    @Published public var lat: Double = 0
    @Published public var lng: Double = 0
    @Published public var pLat: Double = 0
    @Published public var pLng: Double = 0
    @Published public var vLat: Double = 0
    @Published public var vLng: Double = 0
    @Published public var dLat: Double = 0
    @Published public var dLng: Double = 0
    
    @Published public var navigate = false
    @Published public var viaToggle = false
    @Published public var data = []
    @Published public var shouldOpenPlacePicker = false
    @Published var addrData: [AddressModel]?
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    private var listener: ListenerRegistration?
    
    
    public func initialize() {
        Task(priority: .medium) {
            await getAddresses()
        }
    }
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    @MainActor
    public func getAddresses() {
        let profileDocument = parentCollection.collection("addresses")
        self.loading = true
        listener = profileDocument
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
    
}
