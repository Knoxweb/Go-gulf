//
//  AppVM.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 15/01/2024.
//

import Foundation

class AppVM: BaseObservableObject {
    
    @Published var successCheck = false
    
    func checkStatus() {
        self.loading = true
//        NetworkManager.shared.get(urlString: "driver/session-check", header: "\(UserDefaults.standard.string(forKey: "accessToken") ?? "")") { (RESPONSE_DATA:DefaultResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.loading = false
//                print(URL_RESPONSE?.statusCode ?? "", "status check status codeeeeeeeeeeee")
//                if(URL_RESPONSE?.statusCode == 200){
//                    self.successCheck = true
//                }
//                else{
//                    self.successCheck = false
//                    UserDefaults.standard.removeObject(forKey: "accessToken")
//                    UserDefaults.standard.removeObject(forKey: "identity")
//                }
//            }
//        }
    }
}
