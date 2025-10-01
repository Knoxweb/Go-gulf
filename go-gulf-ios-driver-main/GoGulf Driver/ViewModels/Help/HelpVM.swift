//
//  HelpVM.swift
// SlyykDriver
//
//  Created by Office on 07/07/2022.
//

import Foundation
import FirebaseFirestore

class HelpVM: BaseObservableObject {
    
    @Published var helpData: HelpModel?
    @Published var legalData: LegalModel?
    
    internal func getHotlines() {
        let documentReference = Firestore.firestore().collection("api").document("hotlines")
        self.redacting = true
        print(documentReference.path, "documentReference")
        
        documentReference.getDocument { [weak self] documentSnapshot, error in
            self?.redacting = false
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
                self.helpData = try JSONDecoder().decode(HelpModel.self, from: data)
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    internal func getLegal(_ endpoint: String) {
        let documentReference = Firestore.firestore().collection("api").document(endpoint)
        self.loading = true
        print(documentReference.path, "documentReference")
        
        documentReference.getDocument { [weak self] documentSnapshot, error in
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
                self.legalData = try JSONDecoder().decode(LegalModel.self, from: data)
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
}
