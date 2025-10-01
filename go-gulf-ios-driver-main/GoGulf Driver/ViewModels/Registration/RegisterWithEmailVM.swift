//
//  RegisterWithEmailVM.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 05/09/2024.
//

import Foundation
import Combine
import SwiftUI
import FirebaseAuth

class RegisterWithEmailVM: BaseObservableObject {
    @Published var fullname = ""
    @Published var email = ""
    @Published var mobile = ""
    @Published var password = ""
    @Published var confirmPassword = ""
    @Published var registerResponse: RegisterResponse?
    @Published var loginResponse: LoginResponse?
    @Published var userCurrentCountry: CPData? = nil
    let countries: [CPData] = Bundle.main.decode("CountryNumbers.json")
    @Published private var result: String = ""
    @Published  var choosedAccount = "private"
    @Published var accounts = ["private", "corporate"]
    @Published var defaultResponse: DefaultResponse?
    @Published var profileImage: UIImage = UIImage()
    
    public func validate() {
        guard !self.fullname.isEmpty, !self.email.isEmpty, !self.password.isEmpty, !self.confirmPassword.isEmpty else{
            self.alertTitle = "Missing"
            self.alertMessage = "Please enter all fields"
            self.showAlert.toggle()
            return
        }
        guard self.password == self.confirmPassword else {
            self.alertTitle = "Password Mismatched"
            self.alertMessage = "Password and Confirm password should be same."
            self.showAlert.toggle()
            return
        }
        createAccount()
    }
    
    internal func signInWithCustomToken() {
        Auth.auth().signIn(withCustomToken: self.registerResponse?.data?.firebaseAuthToken ?? "") { (user, error) in
            if let error = error {
                let authError = error as NSError
                let error  = NSError(domain: authError.domain, code: authError.code, userInfo: [NSLocalizedDescriptionKey: error.localizedDescription])
                self.alertMessage = "\(error.localizedDescription)"
                self.alertTitle = "Alert \(authError.code)"
                self.showAlert.toggle()
                self.loading = false
                return
            }
            if let userInfo = user {
                print(userInfo, userInfo.user.uid, userInfo.user.phoneNumber as Any,  "user Infosssss")
                self.setLogin(userInfo.user.uid, userInfo.user.phoneNumber)
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
                    UserDefaults.standard.removeObject(forKey: "lang")
                    UserDefaults.standard.removeObject(forKey: "accessToken")
                    
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultResponse = response
                
                self.appRoot?.currentRoot = .homeScreen
                self.router?.popToRoot()
            }
            .store(in: &cancellables)
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
                        self.appRoot?.currentRoot = .termsScreen(endpoint: "driver_term_of_use", hasToolbar: true)
                        self.router?.popToRoot()
//                        self.appRoot?.currentRoot = .tabs
//                        self.router?.popToRoot()
                        break;
                    default:
                        self.appRoot?.currentRoot = .termsScreen(endpoint: "driver_term_of_use", hasToolbar: true)
                        self.router?.popToRoot()
//                        self.appRoot?.currentRoot = .tabs
//                        self.router?.popToRoot()
                    }
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage = "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: LoginResponse) in
                self.loginResponse = response
                UserDefaults.standard.set(response.data?.ref, forKey: "UID")
                UserDefaults.standard.set(response.data?.auth_token, forKey: "accessToken")
            }
            .store(in: &cancellables)
    }
    
    public func createAccount() {
        let body = (key: "profile_image", image: profileImage == UIImage() ? nil : profileImage)
        let param = ["name": self.fullname, "mobile": "\(userCurrentCountry?.dial_code ?? "")\(mobile)", "email": email, "password": password ]
//        let imageParams = ["profile_image": profileImage]
        self.loading = true
        print(body, param, "tstststststststststststt")
        
        NetworkManager.shared.UPLOAD(
            to: APIEndpoints.register,
            images: [:],
            singleImageWithPrefix: body as? (key: String, image: UIImage),
            params: param
        )
        .sink { completion in
            
            switch completion {
            case .finished:
                print("success")
                self.signInWithCustomToken()
                
            case .failure(let error):
                self.loading = false
                let err = NetworkConnection().handleNetworkError(error)
                self.alertTitle = "\(err.title ?? "")"
                self.alertMessage =  "\(err.message ?? "")"
                self.showAlert = true
                
            }
        } receiveValue: { (response: RegisterResponse) in
            print(response, "resssss")
            self.registerResponse = response
        }
        .store(in: &cancellables)
    }
    
    
    
//    public func createAccount() {
//           let body = RegisterRequest(
//                name: fullname,
//                mobile: "\(userCurrentCountry?.dial_code ?? "")\(mobile)",
//                email: email,
//                password: password
////                passengerType: choosedAccount
//            )
//        
//        self.loading = true
//        NetworkManager.shared.POST(to: APIEndpoints.register, body: body)
//            .sink { completion in
//                self.loading = false
//                switch completion {
//                case .finished:
//                    self.signInWithCustomToken()
//                case .failure(let error):
//                    let err = NetworkConnection().handleNetworkError(error)
//                    self.alertTitle = "\(err.title ?? "")"
//                    self.alertMessage =  "\(err.message ?? "")"
//                    self.showAlert.toggle()
//                }
//            } receiveValue: { (response: RegisterResponse) in
//                self.registerResponse = response
//            }
//            .store(in: &cancellables)
//    }
    
    
    
}

