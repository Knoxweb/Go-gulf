//
//  SettingsVM.swift
// SlyykDriver
//
//  Created by Office on 07/07/2022.
//

import Foundation
import FirebaseDatabase
import UIKit

class SettingsVM: BaseObservableObject {
    @Published var responseData: DefaultResponse?
    @Published var navigateToHome = false
    
    
    func logOut(router: Router, appRoot: AppRootManager){
        self.loading = true
//        NetworkManager.shared.get(urlString: "driver/logout", header: nil) { (RESPONSE_DATA: DefaultResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.responseData = RESPONSE_DATA;
//                self.loading = false
//                if(URL_RESPONSE?.statusCode == 200){
//                    UserDefaults.standard.removeObject(forKey: "accessToken")
//                    UserDefaults.standard.removeObject(forKey: "identity")
//                    UserDefaults.standard.removeObject(forKey: "country")
//                    appRoot.currentRoot = .homeScreen
//                    router.popToRoot()
//                }
//                else{
//                    DispatchQueue.main.async {
//                        let alertController = GlobalAlertController(title: String(self.responseData?.title ?? ""), message: String(self.responseData?.message ?? ""), preferredStyle: .alert)
//                        alertController.addAction(UIAlertAction(title: "ok", style: .default, handler: { _ in
////                            self.navigate = true
//                        }))
//                        alertController.presentGlobally(animated: true, completion: nil)
//                    }
//                }
//            }
//        }
    }
    
    func confirmDelete(router: Router, appRoot: AppRootManager){
        self.loading = true
//        NetworkManager.shared.post(urlString: "driver/account-delete", header: nil, encodingData: "") { (RESPONSE_DATA:DefaultResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.responseData = RESPONSE_DATA;
//                self.loading = false
//                if(URL_RESPONSE?.statusCode == 200){
//                    UserDefaults.standard.removeObject(forKey: "accessToken")
//                    UserDefaults.standard.removeObject(forKey: "identity")
//                    UserDefaults.standard.removeObject(forKey: "country")
//                    appRoot.currentRoot = .homeScreen
//                    router.popToRoot()
//                }
//                else{
//                    DispatchQueue.main.async {
//                        let alertController = GlobalAlertController(title: String(self.responseData!.title ?? ""), message: String(self.responseData!.message ?? ""), preferredStyle: .alert)
//                        alertController.addAction(UIAlertAction(title: "ok", style: .default, handler: { _ in
//                            
//                        }))
//                        alertController.presentGlobally(animated: true, completion: nil)
//                    }
//                }
//            }
//        }
    }
}
