//
//  MenuListView.swift
//  SwiftProject
//
//  Created by Mac on 12/14/21.
//

import SwiftUI

struct Menu: Identifiable {
    let id = UUID()
    let title: String
    let icon: String
    let type: String
    var destinationView: AnyView = AnyView(ScheduledBookingsView())
}
private var MenuLists = [
//    Menu(title: "Dashboard", icon: "person.icloud", type: "dashboard"),
//    Menu(title: "Book Now", icon: "car.fill", type: "bookNow"),
//    Menu(title: "My Bookings", icon: "calendar", type: "schedule"),
    Menu(title: "Booking History", icon: "arrowshape.turn.up.backward.2", type: "history"),
//    Menu(title: "Notification", icon: "bell", type: "notification"),
    Menu(title: "Invoice", icon: "list.bullet.rectangle.portrait", type: "invoice"),
//    Menu(title: "Invoice", icon: "list.bullet.rectangle.portrait", destinationView: AnyView(InvoicesView())),
    //    Menu(title: "Help", icon: "questionmark.circle", destinationView: AnyView(HelpUIView())),
    Menu(title: "Support", icon: "message", type: "support"),
    Menu(title: "Account", icon: "gearshape", type: "account"),
]

struct MenuListView: View {
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    @Environment(\.presentationMode) var presentationMode
    @StateObject var menuVM: MenuVM = MenuVM()
    @State var logoutAlert = false
    @State var deleteAccountAlert = false
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
                        self.router.navigateTo(.bookingHistoryScreen)
                    }){
                        HStack {
                            Image(systemName: "arrowshape.turn.up.backward.2")
                                .frame(width: 40)
                                .imageScale(.large)
                                .foregroundColor(Color.accentColor)
                            Text("Booking History")
                                .foregroundStyle(Color.black)
                        }
                        .environment(\.sizeCategory, .medium)
                    }
                    .listRowBackground(Color("Card"))
                    
                    Button(action: {
                        self.router.navigateTo(.invoicesScreen)
                    }){
                        HStack {
                            Image(systemName: "list.bullet.rectangle.portrait")
                                .frame(width: 40)
                                .imageScale(.large)
                                .foregroundColor(Color.accentColor)
                            Text("Invoice")
                                .foregroundStyle(Color.black)
                        }
                        .environment(\.sizeCategory, .medium)
                    }
                    .listRowBackground(Color("Card"))
                    
                    Button(action: {
                        self.router.navigateTo(.supportScreen)
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
                        self.router.navigateTo(.accountScreen)
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
                   
                    Section{
                        Button(action: {
                            logoutAlert.toggle()
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
                
                Spacer()
            }
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
        .toolbar(.hidden)
//            .toolbarRole(.editor)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//            .navigationBarTitle("Menu", displayMode: .inline)
            .modifier(LoadingView(isPresented: $menuVM.loading))
            .onAppear() {
                UINavigationBar.customizeBackButton()
            }
            .onReceive(menuVM.viewDismissalModePublisher) { shouldDismiss in
                if shouldDismiss {
                    self.presentationMode.wrappedValue.dismiss()
                }
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
    }

}

struct MenuListView_Previews: PreviewProvider {
    static var previews: some View {
        MenuListView()
    }
}
