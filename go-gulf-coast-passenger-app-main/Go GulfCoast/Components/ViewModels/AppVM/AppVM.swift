//
//  AppVM.swift
//  GoGulfDriver
//
//  Created by Prabin Phasikawo on 15/01/2024.
//

import Foundation

class AppVM: BaseObservableObject {
    
    @Published var successCheck = false
    
    func checkStatus() {
        self.loading = true
        NetworkManager.shared.GET(from: "passenger/session-check")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.successCheck = true
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.successCheck = false
                    UserDefaults.standard.removeObject(forKey: "accessToken")
                    UserDefaults.standard.removeObject(forKey: "identity")
                }
            } receiveValue: { (response: DefaultResponse) in
            }
            .store(in: &cancellables)
    }
}
