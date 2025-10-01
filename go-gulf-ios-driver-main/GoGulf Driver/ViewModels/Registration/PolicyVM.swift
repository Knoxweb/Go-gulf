//
//  PolicyVM.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 25/08/2023.
//
import Foundation
import Firebase

class PolicyVM: BaseObservableObject {
    
    @Published var data = ""
    @Published var name = ""
    
    @Published var responseData: DefaultResponse?
    @Published var navigate = false
    
    public func getTerm(){
        self.loading.toggle()
        let docRef = db.collection("policy_contents").whereField("user", isEqualTo: "driver")
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
}
