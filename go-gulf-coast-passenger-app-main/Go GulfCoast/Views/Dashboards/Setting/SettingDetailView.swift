//
//  SettingDetailView.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 10/10/2022.
//

import SwiftUI
import Firebase
import FirebaseFirestore

struct SettingDetailView: View {
//    @State var settingDetail: settingDetailModel?
    var body: some View {
            ScrollView{
                VStack(alignment: .leading) {
                    HStack{
                        Spacer()
                        Image("HomeLogo")
                            .resizable(resizingMode: .stretch)
                            .aspectRatio(contentMode: .fit)
//                            .padding(.all, 60)
                            .frame(width: 150, height: 100)
                        Spacer()
                           
                    }
                    .padding(.top)
                    .padding(.horizontal)
                    
                    VStack (alignment: .leading, spacing: 12){
                        VStack (alignment: .leading, spacing: 12){
                            Text("INTRODUCTION")
                                .font(.title3).fontWeight(.bold).padding(.bottom, 2).padding(.top, 15).foregroundColor(Color("AccentColor"))
                            
                            Text("This privacy policy describes how GoGulf Pvt. Ltd. ('GoGulf', 'we', 'us' or 'our') collects and processes personal information about you when you visit our website and use our mobile app. It also explains how we use and protect your information, along with your rights.")
                        }
                    }
                }
                .foregroundColor(.black)
                .opacity(0.75)
                .padding(.horizontal)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .navigationBarTitle("Privacy Policy", displayMode: .inline)
                
            }
              
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
    }
    
//    func getDetail(type: String){
//        let db = Firestore.firestore()
//        db.collection("contents").whereField("user", isEqualTo: "passenger")
//            .getDocuments { [self] (querySnapshot, error) in
//                guard (querySnapshot?.documents) != nil else {
//                    print("No documents")
//                    return
//                }
//                for document in querySnapshot!.documents {
//                    DispatchQueue.main.async {
//                        let docId = document.documentID
//                        let data = document.data()
//                        let title = data["title"] as? String ?? ""
//                        let user = data["user"] as? String ?? ""
//                        let name = data["name"] as? String ?? ""
//                        let description = data["description"] as? String ?? ""
//                        
//                        print(description, "datatatatattaattataatatatatatatatatata")
//                        self.settingDetail = settingDetailModel(docId: docId, title: title, user: user, name: name, description: description)
//                    }
//                }
//            }
//    }
}

struct SettingDetailView_Previews: PreviewProvider {
    static var previews: some View {
        //        NavigationView {
        SettingDetailView()
        //        }
    }
}
