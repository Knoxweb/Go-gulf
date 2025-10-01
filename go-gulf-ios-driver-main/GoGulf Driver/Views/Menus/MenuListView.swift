//
//  MenuListView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 12/14/21.
//

import SwiftUI

struct Menu: Identifiable {
    let id = UUID()
    let title: String
    let icon: String
    let type: String
}
private var MenuLists = [
//    Menu(title: "Dashboard", icon: "person.icloud", type: "dashboard"),
//    Menu(title: "Pickups", icon: "calendar", type: "pickup"),
    Menu(title: "Jobs History", icon: "arrowshape.turn.up.backward.2", type: "history"),
    Menu(title: "Driver Documents", icon: "list.bullet.below.rectangle", type: "driver_document"),
    Menu(title: "Vehicle Documents", icon: "car.fill", type: "vehicle_document"),
//    Menu(title: "My Earnings", icon: "dollarsign", type: "earning"),
    Menu(title: "Statements", icon: "square.on.square", type: "statement"),
//    Menu(title: "Notification", icon: "bell", type: "notification"),
    Menu(title: "Support", icon: "message", type: "support"),
    Menu(title: "Account", icon: "gearshape", type: "account"),
]
struct MenuListView: View {
    @State var active = false
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var tabRouter: TabRouter
    @StateObject var menuVM: MenuVM = MenuVM()
//    @Environment(\.presentationMode) var presentationMode
    @State var logoutAlert = false
    @State var deleteAccountAlert = false
    @State var standbyAlert = false
    @State var scrollNav = false
    
    var body: some View {
        VStack (spacing: 0){
            VStack {
                Text("Menu")
                    .fontWeight(.bold)
            }
            .frame(maxWidth: .infinity)
            .padding(.bottom)
            .padding(.horizontal)
            .background(.ultraThinMaterial.opacity(scrollNav ? 1: 0))
            
            
            List {
                 Button(action: {
                     self.router.navigateTo(.historyScreen)
                 }){
                     HStack {
                         Image(systemName: "arrowshape.turn.up.backward.2")
                             .frame(width: 40)
                             .imageScale(.large)
                             .foregroundColor(Color.accentColor)
                         Text("Jobs History")
                             .foregroundStyle(Color.black)
                     }
                     .environment(\.sizeCategory, .medium)
                 }
                 .listRowBackground(Color("Card"))
                
                Button(action: {
                    self.router.navigateTo(.driverDocScreen)
                }){
                    HStack {
                        Image(systemName: "list.bullet.below.rectangle")
                            .frame(width: 40)
                            .imageScale(.large)
                            .foregroundColor(Color.accentColor)
                        Text("Driver Documents")
                            .foregroundStyle(Color.black)
                    }
                    .environment(\.sizeCategory, .medium)
                }
                .listRowBackground(Color("Card"))
                
                Button(action: {
                    self.router.navigateTo(.vehicleDocScreen)
                }){
                    HStack {
                        Image(systemName: "car.fill")
                            .frame(width: 40)
                            .imageScale(.large)
                            .foregroundColor(Color.accentColor)
                        Text("Vehicle Documents")
                            .foregroundStyle(Color.black)
                    }
                    .environment(\.sizeCategory, .medium)
                }
                .listRowBackground(Color("Card"))
                
                Button(action: {
                    self.router.navigateTo(.statementScreen)
                }){
                    HStack {
                        Image(systemName: "square.on.square")
                            .frame(width: 40)
                            .imageScale(.large)
                            .foregroundColor(Color.accentColor)
                        Text("Statements")
                            .foregroundStyle(Color.black)
                    }
                    .environment(\.sizeCategory, .medium)
                }
                .listRowBackground(Color("Card"))
                
                Button(action: {
                    self.router.navigateTo(.support)
                }){
                    HStack {
                        Image(systemName: "message")
                            .frame(width: 40)
                            .imageScale(.large)
                            .foregroundColor(Color.accentColor)
                        Text("Support")
                            .foregroundStyle(Color.black)
                    }
                    .environment(\.sizeCategory, .medium)
                }
                .listRowBackground(Color("Card"))
                
                Button(action: {
                    self.router.navigateTo(.account)
                }){
                    HStack {
                        Image(systemName: "gearshape")
                            .frame(width: 40)
                            .imageScale(.large)
                            .foregroundColor(Color.accentColor)
                        Text("Account")
                            .foregroundStyle(Color.black)
                    }
                    .environment(\.sizeCategory, .medium)
                }
                .listRowBackground(Color("Card"))

               
                Section(){
                    Button(action: {
                        if menuVM.profileData?.is_approved ?? false {
                            tabRouter.selectedTab = .offers
                        }
                        else {
                            standbyAlert = true
                        }
                        
                    }) {
                        HStack {
                            Image(systemName: "circle.dashed")
                                .foregroundColor(.accentColor)
                            Text("Job Board")
                                .foregroundColor(.accentColor)
                        }
                    }
                }
                .listRowBackground(Color("Card"))
                
                Section{
                    Button(action: {
                        logoutAlert = true
                    }) {
                        
                        HStack {
                            Image(systemName: "power")
                                .frame(width: 40)
                                .foregroundColor(.red)
                            Text("Log Out")
                                .foregroundColor(.red)
                        }
                        
                    }
                }
                .listRowBackground(Color("Card"))
                
                Section{
                    Button(action: {
                        deleteAccountAlert = true
                    }) {
                        
                        HStack {
                            Image(systemName: "trash")
                                .frame(width: 40)
                                .foregroundColor(.red)
                            Text("Delete Account")
                                .foregroundColor(.red)
                        }
                        
                    }
                }
                .listRowBackground(Color("Card"))
                .alert(isPresented: $deleteAccountAlert) {
                    Alert(
                        title: Text("Delete Account?"),
                        message: Text("All your bookings and other informations will be permanently removed?"),
                        primaryButton: .destructive(Text("Delete")) {
                            menuVM.deleteAccount()
                        },
                        secondaryButton: .cancel()
                    )
                }
            
            }
            .clearListBackground()
            .simultaneousGesture(
                DragGesture()
                    .onChanged { value in
                        if value.translation.height < 100 {
                            scrollNav = true
                        } else {
                            scrollNav = false
                        }
                    }
                    .onEnded { value in
                        if value.translation.height > 100 {
                            scrollNav = false
                        } else {
                            scrollNav = true
                        }
                    }
            )
            Spacer()
        }
        .alert("Account Not Approved", isPresented: self.$standbyAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text("You need to upload all your documents first and wait for your account approval to accept jobs.")
        }
        .alert(isPresented: $logoutAlert) {
            Alert(
                title: Text("Are you sure?"),
                message: Text("You want to logout?"),
                primaryButton: .destructive(Text("Logout")) {
                    menuVM.logout()
                },
                secondaryButton: .cancel()
            )
        }
        .toolbar(.hidden)
//        .toolbarRole(.editor)
//        .navigationBarTitle("Menu", displayMode: .inline)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .modifier(LoadingView(isPresented: $menuVM.loading))
        
//        .navigationBarTitleDisplayMode(.inline)
        
        .onAppear() {
            UINavigationBar.customizeBackButton()
            menuVM.getProfileData()
        }
        .onDisappear() {
            menuVM.stopListener()
        }
//        .onReceive(menuVM.viewDismissalModePublisher) { shouldDismiss in
//            if shouldDismiss {
//                self.presentationMode.wrappedValue.dismiss()
//            }
//        }
    }
    
}

struct MenuListView_Previews: PreviewProvider {
    static var previews: some View {
        MenuListView()
    }
}
