//
//  AccountVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 07/07/2022.
//

import Foundation
import UIKit
import FirebaseFirestore

class AccountVM: BaseObservableObject {
    @Published var responseData: ProfileResponse?
    @Published var showingSheet = false
    @Published var name = ""
    @Published var email = ""
    @Published var phone = ""
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var defaultResponse: DefaultResponse?
    private var listener: ListenerRegistration?
    @Published var profileData: FBProfileData?
    @Published var cardData: [CardModel]?
    @Published var activeCard: CardModel?
    @Published var defaultRes: DefaultResponse?
    
    public func getCards() {
        let profileDocument = parentCollection.collection("cards")
        self.loading = true
        listener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.loading = false
                guard let self = self else { return }
                self.cardData = []
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(profileDocument.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var cards: [CardModel] = []
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let card = try decoder.decode(CardModel.self, from: jsonData)
                            cards.append(card)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                    self.cardData = cards
                    self.activeCard = cards.filter{ $0.is_active ?? true }.first
                } else {
                    print("No cards found.")
                }
            }
        
    }
    
    
    public func deleteCard(cardId: Int?) {
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.deleteCard("\(cardId ?? 0)"), body: "")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print("")
                    
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
    
}
