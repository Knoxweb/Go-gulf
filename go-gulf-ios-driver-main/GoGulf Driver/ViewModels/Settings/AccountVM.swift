//
//  AccountVM.swift
// SlyykDriver
//
//  Created by Office on 07/07/2022.
//

import Foundation
import UIKit
import Firebase

class AccountVM: BaseObservableObject {
    @Published var responseData: ProfileResponse?
    
    @Published var showingSheet = false
    @Published var showSheet = false
    private var listener: ListenerRegistration?
    @Published var name = ""
    @Published var email = ""
    @Published var phone = ""
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var bankData: BankResponseData?
    
    public func initialize() {
        Task(priority: .medium) {
            await getBank()
        }
    }
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    @MainActor
    func getBank(){
        self.loading = true
        let documentReference = parentCollection.collection("data").document("bank_account")
        print(documentReference.path, "documentReference")
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
                self.bankData = try JSONDecoder().decode(BankResponseData.self, from: data)
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
}
