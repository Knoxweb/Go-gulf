//
//  TermsVM.swift
//  CTP Limo
//
//  Created by Office on 19/05/2023.
//

import Foundation
import Firebase
import FirebaseFirestore

class TermsVM: BaseObservableObject {
    
    @Published var data = ""
    @Published var name = ""
    @Published var responseData: DefaultResponse?
    @Published var navigate = false
    
    public func getTerm(){
        self.loading.toggle()
        let docRef = db.collection("contents").whereField("user", isEqualTo: "passenger")
        docRef.getDocuments { (querySnapshot, error) in
            guard let querySnapshot = querySnapshot else {
                print("Error fetching document: \(error!)")
                return
            }
            DispatchQueue.main.async {
                self.loading.toggle()
                for document in querySnapshot.documents {
                    let data = document.data() as [String: AnyObject]
                    self.data = data["description"] as? String ?? ""
                    self.name = data["name"] as? String ?? ""
                    print(data, "asdfhjasdfguyasdfuihdsaghfi")
                }
            }
        }
    }
    
    public func readTerms(router: Router, appRoot: AppRootManager){
        self.loading = true
        
        NetworkManager.shared.GET(from: "passenger/accept-terms")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    appRoot.currentRoot = .dashboardScreen
                    router.popToRoot()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    defaultError(title: err.title, msg: err.message)
                }
            } receiveValue: { (response: DefaultResponse) in
                self.responseData = response;
                
            }
            .store(in: &cancellables)
    }
}
