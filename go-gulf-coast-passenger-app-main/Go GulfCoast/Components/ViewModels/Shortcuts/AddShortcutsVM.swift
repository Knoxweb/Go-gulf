//
//  AddShortcutsVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 24/07/2022.
//

import Foundation
import UIKit
import Combine
import FirebaseFirestore

class ShortcutVM: BaseObservableObject {
    @Published var address = ""
    @Published var icon = "home"
    @Published var lat: Double = 0
    @Published var lng: Double = 0
    @Published var title = "Home"
    
    @Published var openPicker = false
    @Published var id = 1
    @Published var navigate = false
    @Published var showSuccess = false
    let identity = UserDefaults.standard.string(forKey: "identity")
    @Published var defaultRes: DefaultResponse?
    
    
    public func addShortcut() {
        let body = AddAddressModel(name: title.lowercased(), address: address, lat: String(lat), lng: String(lng))
        self.loading = true
        NetworkManager.shared.POST(to:  APIEndpoints.addAddress, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
                    self.alertMessage = "\(self.defaultRes?.message ?? "")"
                    self.showSuccess = true
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    public func updateShortcut(id:Int?) {
        let body = AddAddressModel(name: title.lowercased(), address: address, lat: String(lat), lng: String(lng))
        self.loading = true
        NetworkManager.shared.POST(to:  APIEndpoints.editAddress("\(id ?? 0)"), body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "\(self.defaultRes?.title ?? "")"
                    self.alertMessage = "\(self.defaultRes?.message ?? "")"
                    self.showSuccess = true
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    
}
