//
//  SupportVM.swift
// SlyykDriver
//
//  Created by Office on 12/07/2022.
//

import Foundation
import FirebaseFirestore

class SupportVM: BaseObservableObject {
    //    @Published var bookingId = ""
    //    @Published var Message = ""
    //    @Published var Subject = ""
    @Published var fields: Array<Any>?
    @Published var list: [SupportList] = []
    
    
    //    init(){
    //        self.getSupportList()
    //    }
    //
    func getSupportList() {
        let docRef = db.collection("support_contents").document("lists")
        docRef.getDocument { (document, error) in
            guard error == nil else {
                print("error", error ?? "")
                return
            }
            
            if let document = document, document.exists {
                let data = document.data()
                if let data = data {
                    DispatchQueue.main.async {
                        self.list = data.map { d -> SupportList in
                            let json = d.value as! [String: AnyObject]
                            let title = json["title"] as! String
                            let icon =  json["icon"] as! String
                            let id =  Int(d.key)
                            let fields = json["fields"] as? Array<Any>
                            self.fields = fields
                            return SupportList(id: id, title: title, icon: icon)
                        }
                    }
                    print("fddfdfdfd", self.list)
                }
            }
        }
    }
}
