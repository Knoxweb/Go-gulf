//
//  JobStatsCard.swift
//  SlyykDriver
//
//  Created by Office on 22/03/2023.
//
import SwiftUI
import FirebaseFirestore
import Firebase
import AudioToolbox

struct JobsStatsCard: View{
    let ref = Database.database().reference()
    let db = Firestore.firestore()
    @State private var MyPickupsListener: ListenerRegistration?
    @State private var AvailableJobsListener: ListenerRegistration?
    @State var initialAvailableRead = true
    let identity = UserDefaults.standard.string(forKey: "identity")
    @State var availablePickup = 0
    @State var myPickup = 0
    var body: some View{
        VStack {
            HStack{
                NavigationLink(destination: ScheduledBookingsView()){
                    HStack{
                        Spacer()
                        VStack(alignment: .center, spacing: 8){
                            HStack{
                                Text("\(self.availablePickup)")
                                    .font(.system(size: 25))
                                    .fontWeight(.bold)
                                Image(systemName: "checklist.unchecked")
                                //                                    .foregroundColor(.white.opacity(0.3))
                                    .font(.system(size: 25))
                                    .foregroundColor(.accentColor)
                            }
                            .foregroundColor(Color("AccentColor"))
                            Text("Available Pickups")
                                .font(.system(size: 14))
                                .foregroundColor(Color.gray)
                        }
                        Spacer()
                    }
                }
                Spacer()
                Divider()
                Spacer()
                NavigationLink(destination: ScheduledBookingsView()){
                    HStack{
                        Spacer()
                        VStack(alignment: .center, spacing: 8){
                            HStack{
                                Text("\(self.myPickup)")
                                    .font(.system(size: 25))
                                    .fontWeight(.bold)
                                Image(systemName: "checklist.checked")
                                    .font(.system(size: 25))
                                    .foregroundColor(.accentColor)
                            }
                            .foregroundColor(Color("AccentColor"))
                            Text("My Pickups")
                                .font(.system(size: 14))
                                .foregroundColor(Color.gray)
                        }
                        Spacer()
                    }
                    .padding(.leading)
                }
            }
            .cardStyleModifier()
        }
        .padding(.horizontal)
        .onAppear(){
            if(UserDefaults.standard.string(forKey: "accessToken") != nil && UserDefaults.standard.string(forKey: "accessToken") != ""){
                self.initMyJobListener()
                self.initAvailableJobListener()
            }
        }
        .onDisappear() {
            self.MyPickupsListener?.remove()
            self.MyPickupsListener = nil
            self.AvailableJobsListener?.remove()
            self.AvailableJobsListener = nil
        }
    }
    
    
    
    func initMyJobListener(){
        if(UserDefaults.standard.string(forKey: "accessToken") != nil && UserDefaults.standard.string(forKey: "accessToken") != ""){
            let collectionRef = db.collection("drivers").document("\(UserDefaults.standard.string(forKey: "identity") ?? "")").collection("my_pickups")
            self.MyPickupsListener = collectionRef.addSnapshotListener { (snapshot, error) in
                guard (snapshot?.documents) != nil else {
                    print("Error fetching documents: \(error!)")
                    return
                }
                self.myPickup = 0
                
                DispatchQueue.main.async {
                    self.myPickup = Int(snapshot?.count ?? 0)
                }
            }
        }
    }
    
    func initAvailableJobListener(){
        if(UserDefaults.standard.string(forKey: "accessToken") != nil && UserDefaults.standard.string(forKey: "accessToken") != ""){
            self.initialAvailableRead = true
            let collectionRef = db.collection("drivers").document("\(UserDefaults.standard.string(forKey: "identity") ?? "")").collection("available_jobs")
            AvailableJobsListener = collectionRef.addSnapshotListener { (snapshot, error) in
                guard (snapshot?.documents) != nil else {
                    print("Error fetching documents: \(error!)")
                    return
                }
                self.availablePickup = 0
                
                DispatchQueue.main.async {
                    self.availablePickup = Int(snapshot?.count ?? 0)
                    if !self.initialAvailableRead {
                        snapshot?.documentChanges.forEach { change in
                            if change.type == .added {
                                print(self.initialAvailableRead, "initialized addeddeddedd")
//                                AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
//                                AudioServicesPlayAlertSound(UInt32(1009))
//                                DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
//                                    AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
//                                    AudioServicesPlayAlertSound(UInt32(1009))
//                                }
//                                DispatchQueue.main.asyncAfter(deadline: .now() + 4.0) {
//                                    AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
//                                    AudioServicesPlayAlertSound(UInt32(1009))
//                                }
                            }
                        }
                    }
                    self.initialAvailableRead = false
                }
            }
        }
    }
}
