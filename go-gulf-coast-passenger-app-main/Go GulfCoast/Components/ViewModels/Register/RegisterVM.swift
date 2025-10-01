//
//  RegisterVM.swift
//  GoGulf
//
//  Created by Mac on 3/10/22.
//

import SwiftUI
import Firebase
import FirebaseAuth

class RegisterVM: BaseObservableObject {
    @Published var responseData:RegisterResponse?
    @Published var defaultRes: DefaultResponse?
    @Published var fullname: String = String()
    @Published var email: String = String()
    @Published var password: String = String()
    @Published var isPresentingErrorAlert: Bool = false
    @Published var navigate: Bool = false
    @Published var loader: Bool = false
    @Published var token: String = ""
    @Published var profileImage: UIImage = UIImage()
    @Published var base64Image = ""
    @Published var confirmPassword = ""
    
    
    public func validate() {
        guard !self.fullname.isEmpty, !self.email.isEmpty, !self.password.isEmpty, !self.confirmPassword.isEmpty else{
            self.alertTitle = "Missing"
            self.alertMessage = "Please enter all fields"
            self.showAlert = true
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
        Auth.auth().signIn(withCustomToken: self.responseData?.data?.firebaseAuthToken ?? "") { (user, error) in
            if let error = error {
                let authError = error as NSError
                let error  = NSError(domain: authError.domain, code: authError.code, userInfo: [NSLocalizedDescriptionKey: error.localizedDescription])
                self.alertMessage = "\(error.localizedDescription)"
                self.alertTitle = "Alert \(authError.code)"
                self.showAlert.toggle()
                return
            }
            if let userInfo = user {
                print(userInfo, userInfo.user.uid, userInfo.user.phoneNumber as Any,  "user Infosssss")
//                self.setLogin(userInfo.user.uid, userInfo.user.phoneNumber)
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
                self.defaultRes = response
                
                self.appRoot?.currentRoot = .homeScreen
                self.router?.popToRoot()
            }
            .store(in: &cancellables)
    }
    
    
    public func createAccount() {
        let body = (key: "profile_image", image: profileImage == UIImage() ? nil : profileImage)
        let param = ["name": self.fullname, "email": email, "password": password ]
//        let imageParams = ["profile_image": profileImage]
        self.loading = true
        
        NetworkManager.shared.UPLOAD(
            to: APIEndpoints.firstUpdate,
            images: [:],
            singleImageWithPrefix: body as? (key: String, image: UIImage),
            params: param
        )
        .sink { completion in
            self.loading = false
            switch completion {
            case .finished:
                print("success")
                
            case .failure(let error):
                let err = NetworkConnection().handleNetworkError(error)
                self.alertTitle = "\(err.title ?? "")"
                self.alertMessage =  "\(err.message ?? "")"
                self.showAlert = true
            }
        } receiveValue: { (response: DefaultResponse) in
            print(response, "resssss")
            self.appRoot?.currentRoot = .termsScreen(endpoint: "passenger_term_of_use", hasToolbar: true)
            self.router?.popToRoot()
//            self.appRoot?.currentRoot = .tabs
//            self.router?.popToRoot()
//            self.responseData = response
        }
        .store(in: &cancellables)
    }
    
    
}

