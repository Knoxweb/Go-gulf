//
//  MenuVM.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 11/09/2024.
//

import Foundation
import Firebase

class MenuVM: BaseObservableObject {
    @Published var defaultRes: DefaultResponse?
    private var listener: ListenerRegistration?
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var profileData: FBProfileData?
    
    
    public func deleteAccount() {
        let body = LogoutModel(device_token: "\(UserDefaults.standard.string(forKey: "fcmToken") ?? "")", voip_token: "\(UserDefaults.standard.string(forKey: "VOIPToken") ?? "")")
        self.loading = true
        NetworkManager.shared.POST(to:  APIEndpoints.deleteAccount, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    UserDefaults.standard.removeObject(forKey: "UID")
                    UserDefaults.standard.removeObject(forKey: "accessToken")
                    self.shouldDismissView = true
                    self.appRoot?.currentRoot = .homeScreen
                    self.router?.popToRoot()
                    
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
    
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    
    
    @MainActor
    public func getProfileData() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("profile")
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
                self.profileData = try JSONDecoder().decode(FBProfileData.self, from: data)
                
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    
    
    public func logout() {
        let body = LogoutModel(device_token: "\(UserDefaults.standard.string(forKey: "fcmToken") ?? "")", voip_token: "\(UserDefaults.standard.string(forKey: "VOIPToken") ?? "")")
        self.loading = true
        NetworkManager.shared.POST(to:  APIEndpoints.logOut, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    UserDefaults.standard.removeObject(forKey: "UID")
                    UserDefaults.standard.removeObject(forKey: "accessToken")
                    self.shouldDismissView = true
                    self.appRoot?.currentRoot = .homeScreen
                    self.router?.popToRoot()
                    
                    
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
