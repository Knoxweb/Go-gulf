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
        let docRef = db.collection("contents").whereField("user", isEqualTo: "driver")
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
        self.loading.toggle()
//        NetworkManager.shared.post(urlString: "driver/accept-terms", header: nil, encodingData: "") { (RESPONSE_DATA: DefaultResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async {
//                self.loading.toggle()
//                self.responseData = RESPONSE_DATA;
//                let title = self.responseData?.title
//                let message = self.responseData?.message
//                print(self.responseData as Any, "Output")
//                self.loading.toggle()
//                if(URL_RESPONSE?.statusCode == 200){
//                    appRoot.currentRoot = .dashboardScreen
//                    router.popToRoot()
//                }
//                else{
//                    defaultError(title: title, msg: message)
//                }
//            }
//        }
    }
}
