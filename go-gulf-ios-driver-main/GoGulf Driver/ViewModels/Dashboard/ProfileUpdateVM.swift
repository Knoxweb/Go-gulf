//
//  ProfileUpdateVM.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 23/09/2024.
//


import Foundation

class ProfileUpdateVM: BaseObservableObject {
    @Published var responseData:RegisterResponse?
    @Published var defaultRes: DefaultResponse?
    @Published var name = ""
    @Published var email = ""
    @Published var phone = ""
    @Published var showSuccess = false
    
    public func updateProfile() {
        let body = ProfileUpdateModel(name: self.name, email: self.email)
        self.loading = true
        NetworkManager.shared.POST(to:  APIEndpoints.update, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
                    self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                    self.showSuccess.toggle()
                    
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


struct ProfileUpdateModel: Codable, Hashable {
    let name: String?
    let email: String?
}
