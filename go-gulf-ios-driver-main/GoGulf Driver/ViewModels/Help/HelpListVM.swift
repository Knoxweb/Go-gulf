//
//  HelpListVM.swift
// SlyykDriver
//
//  Created by Office on 13/07/2022.
//

import Foundation
import FirebaseFirestore

class HelpListVM: BaseObservableObject {
//    @Published var helpList: [Help]?
//    let db = Firestore.firestore()
//
    
//    func getHelpDetail(categoryId: Int) {
//        db.collection("help_contents").whereField("help_category_id", isEqualTo: categoryId)
//            .addSnapshotListener { [self] (querySnapshot, error) in
//                guard (querySnapshot?.documents) != nil else {
//                    print("No documents")
//                    return
//                }
//                DispatchQueue.main.async {
//                    self.helpList = querySnapshot?.documents.map { queryDocumentSnapshot -> Help in
//                        let data = queryDocumentSnapshot.data()
//                        let title = data["title"] as? String ?? ""
//                        let icon = data["icon"] as? String ?? ""
//                        let id = data["id"] as? Int ?? 0
//                        return Help(id: id, title: title, icon: icon)
//                    }
//                }
//            }
//    }
}
