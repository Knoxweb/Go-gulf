//
//  SplashVM.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 04/09/2024.
//

import Foundation
import Combine
import SwiftUI

class SplashVM: BaseObservableObject {
    @Published var statusResponse: StatusResponse?
//    @EnvironmentObject var tabRouter: TabRouter
    
    public func checkStatus(tabRouter: TabRouter) {
        self.loading = true
        NetworkManager.shared.GET(from: APIEndpoints.status)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    let status = self.statusResponse?.data?.profileStatus
                    switch status {
                    case "new":
                        self.appRoot?.currentRoot = .registerScreen
                        self.router?.popToRoot()
                        break;
                    case "completed":
                        if self.statusResponse?.data?.is_approved ?? false {
                            self.appRoot?.currentRoot = .tabs
                            self.router?.popToRoot()
                        }
                        else {
                            self.appRoot?.currentRoot = .tabs
                            self.router?.popToRoot()
                        }
                        break;
                    default:
                        self.appRoot?.currentRoot = .tabs
                        self.router?.popToRoot()
                    }

                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    if err.title == "Unauthenticated" {
                        UserDefaults.standard.removeObject(forKey: "walkthrough")
                        UserDefaults.standard.removeObject(forKey: "accessToken")
                        UserDefaults.standard.removeObject(forKey: "UID")
                    }
                    self.appRoot?.currentRoot = .homeScreen
                    self.router?.popToRoot()
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage = "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: StatusResponse) in
                self.statusResponse = response
            }
            .store(in: &cancellables)
    }
}


