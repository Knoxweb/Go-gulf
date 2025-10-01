//
//  OTPTextFieldView.swift
//  SwiftProject
//
//  Created by Mac on 12/11/21.
//

import SwiftUI
import Firebase
import FirebaseAuth

class OTPVM: BaseObservableObject {
    @Published var loginResponse: LoginResponse?
    @Published var navigate = false
    @Published var otpResponse: ForgotPasswordResponse?
    @Published var navigateToRegister = false
    @Published var navigateToDashboard = false
    @Published var UID = ""
    @Published var pageUrl = ""
    @Published var token = ""
    @Published var firebaseGeneratedId = ""
    @Published var showResendLabel = true
    @Published private var secondsRemaining = 60
    @Published private var timer: Timer?
    
    @Published var otpField = "" {
        didSet {
            isNextTypedArr = Array(repeating: false, count: 6)
            guard otpField.count <= 6,
                  otpField.last?.isNumber ?? true else {
                otpField = oldValue
                return
            }
            if otpField.count < 6 {
                isNextTypedArr[otpField.count] = true
            }
        }
    }
    var otp1: String {
        guard otpField.count >= 1 else {
            return ""
        }
        return String(Array(otpField)[0])
    }
    var otp2: String {
        guard otpField.count >= 2 else {
            return ""
        }
        return String(Array(otpField)[1])
    }
    var otp3: String {
        guard otpField.count >= 3 else {
            return ""
        }
        return String(Array(otpField)[2])
    }
    var otp4: String {
        guard otpField.count >= 4 else {
            return ""
        }
        return String(Array(otpField)[3])
    }
    
    var otp5: String {
        guard otpField.count >= 5 else {
            return ""
        }
        return String(Array(otpField)[4])
    }
    
    var otp6: String {
        guard otpField.count >= 6 else {
            return ""
        }
        return String(Array(otpField)[5])
    }
    
    @Published var isNextTypedArr = Array(repeating: false, count: 6)
    
    @Published var borderColor: Color = .black
    
    @Published var isEditing = false {
        didSet {
            isNextTypedArr = Array(repeating: false, count: 6)
            if isEditing && otpField.count < 6 {
                isNextTypedArr[otpField.count] = true
            }
        }
    }
    
    
    
    
    func resendCode(phoneCC: String?, phoneNumber: String?){
        let phone = "\(phoneCC ?? "0") \(phoneNumber ?? "")"
        self.loading = true
        PhoneAuthProvider.provider().verifyPhoneNumber(phone, uiDelegate: nil) { (ID, err) in
            self.loading = false
            if err != nil{
                return
            }
            self.firebaseGeneratedId = ID!
            self.otpField = ""
        }
    }
    
    func startTimer() {
        self.showResendLabel = false
        self.secondsRemaining = 120
        timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
            if self.secondsRemaining > 0 {
                self.secondsRemaining -= 1
            } else {
                self.stopTimer()
                self.showResendLabel = true
            }
        }
    }

    func stopTimer() {
        timer?.invalidate()
        timer = nil
    }

    func formattedTime() -> String {
        let minutes = secondsRemaining / 60
        let seconds = secondsRemaining % 60
        return String(format: "%02d:%02d", minutes, seconds)
    }
    
    
    func validateOTP() -> Bool{
        if otpField.count < 6 {
            showAlert(title: "Error", msg: "Please Enter 6 Digit Number")
            return false
        }
        return true
    }
    
    func submitOTPCode(router: Router, appRoot: AppRootManager, ID: String, phoneCC: String, phoneNumber: String){
        if validateOTP() {
            self.loading = true
            let credential =  PhoneAuthProvider.provider().credential(withVerificationID: ID, verificationCode: otpField)
            Auth.auth().signIn(with: credential) { (res, err) in
                if err != nil{
                    self.loading = false
                    self.showAlert(title: "Invalid", msg: (err?.localizedDescription)!)
                    return;
                }
                self.UID = res!.user.uid
                self.checkUser(uid: self.UID, phoneCC: phoneCC, phoneNumber: phoneNumber, router: router, appRoot: appRoot)
                UserDefaults.standard.set(true, forKey: "status")
                NotificationCenter.default.post(name: NSNotification.Name("statusChange"), object: nil)
                
            }
        }
    }
    func checkUser(uid: String, phoneCC: String, phoneNumber:String, router: Router, appRoot: AppRootManager){
        let pushManager = PushNotificationManager()
        pushManager.registerForPushNotifications()
        let body = LoginRequest(
            uid: uid,
            mobile: "\(phoneCC)\(phoneNumber.replacingOccurrences(of: " ", with: ""))",
            voip_token: "\(UserDefaults.standard.string(forKey: "VOIPToken") ?? "")",
            device_token: "\(UserDefaults.standard.string(forKey: "fcmToken") ?? "")"
        )
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
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: LoginResponse) in
                self.loginResponse = response
                print(self.loginResponse, "ressss")
                UserDefaults.standard.set(response.data?.ref, forKey: "UID")
                UserDefaults.standard.set(response.data?.auth_token, forKey: "accessToken")
            }
            .store(in: &cancellables)
        
    }
    
    
    func forgotOTPCheck(email: String?){
        guard !self.otpField.isEmpty else{
            self.alertTitle = "Enter OTP"
            self.alertMessage = "Please enter otp code"
            self.showAlert = true
            return
        }
        self.loading = true
        let body = OTPCheckRequest(email: email, code: otpField)
        NetworkManager.shared.POST(to: APIEndpoints.otpCheck, body: body)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    if self.otpResponse?.data?.status ?? true {
                        self.router?.navigateTo(.resetPassword(email: email ?? "", code: self.otpField))
                    }
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage = "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: ForgotPasswordResponse) in
                self.otpResponse = response
            }
            .store(in: &cancellables)
    }
    
    
    
    func showAlert(title: String, msg: String) {
        let alertController = GlobalAlertController(title: title, message: msg, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "Ok", style: .destructive, handler: nil))
        alertController.presentGlobally(animated: true, completion: nil)
    }
    
}
