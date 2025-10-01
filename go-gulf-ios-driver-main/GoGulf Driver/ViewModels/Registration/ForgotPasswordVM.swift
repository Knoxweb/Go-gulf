//
//  ForgotPasswordVM.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 05/09/2024.
//

import SwiftUI
import Foundation
import Combine

struct ForgotPasswordRequest:Hashable,  Codable {
    let email: String?
}

struct ResetPasswordRequest:Hashable,  Codable {
    let password: String?
    let code: String?
    let email: String?
}

class ForgotPasswordVM: BaseObservableObject {
    @Published var successAlert = false
    @Published var email = ""
    @Published var password = ""
    @Published var confirmPassword = ""
    @Published var response: ForgotPasswordResponse?
    
    func validate() {
        guard !email.isEmpty else {
            self.alertTitle = "Missing"
            self.alertMessage = "Please enter email address"
            self.showAlert = true
            return;
        }
        sendEmailOTP()
    }
    
    
    func validatePassword(email: String?, code: String?) {
        guard self.password == self.confirmPassword else {
            self.alertTitle = "Password Mismatched"
            self.alertMessage = "Password and Confirm password should be same."
            self.showAlert.toggle()
            return
        }
        self.resetNewPassword(email: email, code: code)
    }
    
    private func sendEmailOTP() {
        let pushManager = PushNotificationManager()
        pushManager.registerForPushNotifications()
        let body = ForgotPasswordRequest(email: email)
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.forgotPassword, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "Success"
                    self.alertMessage = "\(self.response?.data?.message ?? "")"
                    self.showAlert.toggle()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage = "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: ForgotPasswordResponse) in
                self.response = response
            }
            .store(in: &cancellables)
    }
    
    
    private func resetNewPassword(email: String?, code: String?) {
        let body = ResetPasswordRequest(password: password, code: code, email: email)
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.forgotPasswordReset, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.alertTitle = "Success"
                    self.alertMessage = "\(self.response?.data?.message ?? "")"
                    self.showAlert.toggle()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage = "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: ForgotPasswordResponse) in
                self.response = response
            }
            .store(in: &cancellables)
    }
}
