//
//  MenuVM.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 10/09/2024.
//

import Foundation


class MenuVM: BaseObservableObject {
    @Published var defaultRes: DefaultResponse?
    
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
