//
//  SettingsVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 07/07/2022.
//

import Foundation
import FirebaseDatabase
import UIKit

class SettingsVM: BaseObservableObject {
    @Published var responseData: DefaultResponse?
    @Published var navigateToHome = false
    


    
    func logOut(router: Router, appRoot: AppRootManager){
        self.loading = true
        
        NetworkManager.shared.GET(from: "passenger/logout")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.responseData as Any, "responsedaaataaaaa")
                    UserDefaults.standard.removeObject(forKey: "accessToken")
                    UserDefaults.standard.removeObject(forKey: "identity")
                    appRoot.currentRoot = .homeScreen
                    router.popToRoot()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    DispatchQueue.main.async {
                        let alertController = GlobalAlertController(title: String(err.title ?? ""), message: String(err.message ?? ""), preferredStyle: .alert)
                        alertController.addAction(UIAlertAction(title: "ok", style: .default, handler: { _ in
//                            self.navigate = true
                        }))
                        alertController.presentGlobally(animated: true, completion: nil)
                    }
                }
            } receiveValue: { (response: DefaultResponse) in
                self.responseData = response
            }
            .store(in: &cancellables)
        
    }
    
    
    func confirmDelete(router: Router, appRoot: AppRootManager){
        self.loading = true
        NetworkManager.shared.POST(to: "passenger/account-delete", body: "")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    Database.database().reference().child("notifications").child(UserDefaults.standard.string(forKey: "identity") ?? "").child("current")
                    UserDefaults.standard.removeObject(forKey: "accessToken")
                    UserDefaults.standard.removeObject(forKey: "identity")
                    appRoot.currentRoot = .homeScreen
                    router.popToRoot()
                case .failure(let error):
                    DispatchQueue.main.async {
                        let alertController = GlobalAlertController(title: String(self.responseData?.title ?? ""), message: String(self.responseData?.message ?? ""), preferredStyle: .alert)
                        alertController.addAction(UIAlertAction(title: "ok", style: .default, handler: { _ in
                            
                        }))
                        alertController.presentGlobally(animated: true, completion: nil)
                    }
                }
            } receiveValue: { (response: DefaultResponse) in
                self.responseData = response
            }
            .store(in: &cancellables)
    }
    
}
