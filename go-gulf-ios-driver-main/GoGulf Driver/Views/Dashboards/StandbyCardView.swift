//
//  StandbyCardView.swift
//  SlyykDriver
//
//  Created by Office on 22/03/2023.
//

import SwiftUI
import Firebase
import AudioToolbox
import AVFoundation

struct JobReceivedCard: View{
    @State var jobCount = 0
    let db = Firestore.firestore()
    @State private var StandByListener: ListenerRegistration?
    @State var initialRead = true
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    
    let ref = Database.database().reference()
    let identity = UserDefaults.standard.string(forKey: "identity")
    
    
    var body: some View{
        VStack(alignment: .leading){
            Button(action: {
                router.navigateTo(.dispatchScreen)
            }) {
                HStack{
                    Text("Stanby Job Request")
                        .font(.body)
                    Spacer()
                    HStack{
                        VStack{
                            Text("\(self.jobCount)")
                                .font(.title2)
                                .foregroundColor(.black)
                        }
                        .frame(width: 40, height: 40)
                        .background(Color("AccentColor"))
                        .cornerRadius(100)
                    }
                }
                .cardStyleModifier()
            }
        }
        .padding(.horizontal)
        .onAppear() {
            self.standbyJobCount()
        }
        .onDisappear(){
            self.stopListener()
        }
    }
    
    
    func stopListener() {
        StandByListener?.remove()
        StandByListener = nil
    }
    
    
    private func standbyJobCount(){
        if(UserDefaults.standard.string(forKey: "accessToken") != nil && UserDefaults.standard.string(forKey: "accessToken") != ""){
            self.initialRead = true
            let collectionRef = db.collection("drivers").document("\(UserDefaults.standard.string(forKey: "identity") ?? "")").collection("assign_jobs")
            StandByListener = collectionRef.addSnapshotListener { (snapshot, error) in
                guard (snapshot?.documents) != nil else {
                    print("Error fetching documents: \(error!)")
                    return
                }
                print("countntntntewrgyetfdysdgtyu")
                self.jobCount = 0
                
                DispatchQueue.main.async {
                    self.jobCount = Int(snapshot?.count ?? 0);
                    if !self.initialRead {
                        print("passed condition", self.initialRead)
                        snapshot?.documentChanges.forEach { change in
                            if change.type == .added {
                                AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
                                AudioServicesPlayAlertSound(UInt32(1009))
                                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                                    AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
                                    AudioServicesPlayAlertSound(UInt32(1009))
                                }
                                DispatchQueue.main.asyncAfter(deadline: .now() + 4.0) {
                                    AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
                                    AudioServicesPlayAlertSound(UInt32(1009))
                                }
                            }
                        }
                        
                    }
                    
                    self.initialRead = false
                }
            }
        }
    }
}
