//
//  PhoneVerifyViewModel.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 2/7/22.
//

import Foundation
import Firebase
import FirebaseAuth
import Combine
import UIKit

class PhoneVerifyVM: BaseObservableObject {
    @Published var phoneNumber = ""
    @Published var phone = ""
    @Published var phoneCC = UInt64()
    @Published var navigate = false
    @Published var ID = ""
    @Published var email = ""
    @Published var password = ""
    @Published var registerResponse: RegisterResponse?
    @Published var loginResponse: LoginResponse?
    
    func validatePhoneNumber() -> Bool{
        if phoneNumber.isEmpty {
            self.alertTitle = "Error"
            self.alertMessage =  "Phone Number Can't Be Empty"
            self.showAlert = true
            return false
        }
        else if phoneCC == 0{
            self.alertTitle = "Invalid Number"
            self.alertMessage =  "Please Enter Valid Number"
            self.showAlert = true
            return false
        }
        return true
    }
    
    func showAlert(title: String, msg: String) {
        let alertController = GlobalAlertController(title: title, message: msg, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "Ok", style: .destructive, handler: nil))
        alertController.presentGlobally(animated: true, completion: nil)
    }
    
    func submitForm(country: CPData?, router: Router){
        if self.phoneNumber.isEmpty {
            defaultError(title: "Error", msg: "Phone Number Can't Be Empty")
            return;
        }
        
        let phone = "\(country?.dial_code ?? "0") \(self.phoneNumber)"
        print(phone, "dsfsdfdsfdsfdsfdsdffsdfsdsdf")
        self.loading = true
        PhoneAuthProvider.provider().verifyPhoneNumber(phone, uiDelegate: nil) { (ID, err) in
            self.loading = false
            print(err as Any, "ERORRRRRR")
            if err != nil {
                if err?.localizedDescription == "We have blocked all requests from this device due to unusual activity. Try again later."{
                    self.alertTitle = "Account Blocked"
                    self.alertMessage =  "Due to unusual activity please try again later"
                    self.showAlert = true
                }
                else {
                    self.alertTitle = "Invalid"
                    self.alertMessage =  "\(err?.localizedDescription ?? "Invalid phone number")"
                    self.showAlert = true
                }
                return
            }
            self.ID = ID!
            router.navigateTo(.otpScreen(show: self.navigate, ID: self.ID, phoneCC: "\(country?.dial_code ?? "")", phoneNumber: self.phoneNumber))
        }
    }
    
    public func validateEmailLogin() {
        guard !email.isEmpty, !password.isEmpty else {
            self.alertTitle = "Missing"
            self.alertMessage = "Please enter all fields"
            self.showAlert = true
            return
        }
        loginWithEmail()
    }
    
    
    private func loginWithEmail() {
        let body = LoginEmailRequest(
            email: email,
            password: password
        )
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.emailLogin, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.signInWithCustomToken()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage = "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: RegisterResponse) in
                self.registerResponse = response
            }
            .store(in: &cancellables)
    }
    
    internal func signInWithCustomToken() {
        self.loading = true
        Auth.auth().signIn(withCustomToken: self.registerResponse?.data?.firebaseAuthToken ?? "") { (user, error) in
            self.loading = false
            if let error = error {
                let authError = error as NSError
                let error  = NSError(domain: authError.domain, code: authError.code, userInfo: [NSLocalizedDescriptionKey: error.localizedDescription])
                self.alertMessage = "\(error.localizedDescription)"
                self.alertTitle = "Alert \(authError.code)"
                self.showAlert = true
                return
            }
            if let userInfo = user {
                print(userInfo, userInfo.user.uid, userInfo.user.phoneNumber as Any,  "user Infosssss")
                self.setLogin(userInfo.user.uid, userInfo.user.phoneNumber)
            }
        }
    }
    
    internal func setLogin(_ uid: String, _ mobile: String?) {
        let pushManager = PushNotificationManager()
        pushManager.registerForPushNotifications()
        let body = LoginRequest(uid: uid, mobile: mobile, voip_token: "\(UserDefaults.standard.string(forKey: "VOIPToken") ?? "")", device_token: "\(UserDefaults.standard.string(forKey: "fcmToken") ?? "")")
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.login, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    let status = self.loginResponse?.data?.profile_status
                    switch status {
                    case "new":
                        self.appRoot?.currentRoot = .registerScreen
                        self.router?.popToRoot()
                        break;
                    case "completed":
                        self.appRoot?.currentRoot = .tabs
                        self.router?.popToRoot()
                        break;
                    default:
                        self.appRoot?.currentRoot = .tabs
                        self.router?.popToRoot()
                    }
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage = "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: LoginResponse) in
                self.loginResponse = response
                UserDefaults.standard.set(response.data?.ref, forKey: "UID")
                UserDefaults.standard.set(response.data?.auth_token, forKey: "accessToken")
            }
            .store(in: &cancellables)
    }
    
}


struct LoginEmailRequest:Hashable,  Codable {
    let email: String
    let password: String
}
