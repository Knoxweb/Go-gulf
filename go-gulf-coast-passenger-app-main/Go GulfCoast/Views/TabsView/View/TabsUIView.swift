//
//  TabView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 26/03/2024.
//

import SwiftUI

enum Tab {
    case ride, schedule, profile, notification, menu
}

struct TabScreenView: View {
    @StateObject var VM: TabVM = TabVM()
    @EnvironmentObject var tabRouter: TabRouter
    //    @EnvironmentObject var globalState: GlobalState
    @EnvironmentObject var router: Router
    @StateObject var viewModel = BaseObservableObject()
    @Environment(\.scenePhase) var scenePhase
    let timer = Timer.publish(every: 15, on: .main, in: .common).autoconnect()
    @EnvironmentObject var globalState: GlobalState
    @State var showUdpateAlert = false
    
    var body: some View {
        VStack {
            TabView (selection: $tabRouter.selectedTab){
//                NavigationView {
                    BookNowUIView()
                    .toolbar(.hidden)
//                }
                .tabItem {
                    Image(systemName: "car")
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .frame(height: 50)
                    withAnimation() {
                        Text("Book Now")
                            .foregroundColor(self.tabRouter.selectedTab == .ride ? Color.accentColor : Color.black.opacity(0.5))
                    }
                }
                .tag(Tab.ride)
                
//                NavigationView() {
                    ScheduledBookingsView()
                    .toolbar(.hidden)
//                }
                .tabItem {
                    Image(systemName: "calendar")
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .frame(width: 40, height: 40)
                        .foregroundColor(self.tabRouter.selectedTab == .schedule ? Color.accentColor : Color.black.opacity(0.5))
                    
                    withAnimation() {
                        Text("My Bookings")
                            .foregroundColor(self.tabRouter.selectedTab == .schedule ? Color.accentColor : Color.black.opacity(0.5))
                    }
                }
                .tag(Tab.schedule)
                
//                NavigationView() {
                    DashboardUIView()
                    .toolbar(.hidden)
//                }
                .tabItem {
                    Image(systemName: "person.crop.circle")
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .frame(width: 40, height: 40)
                        .foregroundColor(tabRouter.selectedTab == .profile ? Color.accentColor : Color.black.opacity(0.5))
                    withAnimation() {
                        Text("Profile")
                            .foregroundColor(self.tabRouter.selectedTab == .profile ? Color.accentColor : Color.black.opacity(0.5))
                    }
                }
                .tag(Tab.profile)
                
                
                
//                NavigationView() {
                    NotificationsView()
                    .toolbar(.hidden)
//                }
                .badge(VM.notiCounts?.notification ?? 0)
                .foregroundStyle(Color("ThemeColor"))
                .tabItem {
                    Image(systemName: "bell")
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .frame(width: 40, height: 40)
                        .foregroundColor(tabRouter.selectedTab == .notification ? Color.accentColor : Color.black.opacity(0.5))
                    
                    withAnimation() {
                        Text("Notifications")
                            .foregroundColor(tabRouter.selectedTab == .notification ? Color.accentColor : Color.black.opacity(0.5))
                    }
                }
                .tag(Tab.notification)
                
//                NavigationView() {
                    MenuListView()
                    .toolbar(.hidden)
//                }
                .tabItem {
                    Image("Menus\(tabRouter.selectedTab == .menu ? "Fill" : "")")
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFill()
                        .frame(width: 40)
                        .foregroundColor(tabRouter.selectedTab == .menu ? Color.accentColor : Color.black.opacity(0.5))
                    withAnimation() {
                        Text("Menu")
                            .foregroundColor(tabRouter.selectedTab == .menu ? Color.accentColor : Color.black.opacity(0.5))
                    }
                }
                .tag(Tab.menu)
            }
        }
        .onDisappear() {
            VM.stopListener()
        }
        .modifier(
            AlertView(
                isPresented: self.$showUdpateAlert,
                icon: .updateIcon,
                primaryButtonText: .constant("Go to Appstore"),
                secondaryButtonText: "Not now",
                title: .constant("Update available"),
                desc: .constant("We added lot of features to make your experience even better. Please, update your application."),
                backdropDismiss: false,
                outlineButton: false,
                hasDismissButton: true,
                primaryAction: {
                    self.updateApp()
                },
                secondaryAction: {
                    self.showUdpateAlert = false
                }
            )
        )
        .onChange(of: scenePhase) { newPhase in
            if newPhase == .inactive {
                return;
            } else if newPhase == .background {
                return;
            }
            else{
                if VM.enableLocationServices() {
                    VM.showLocationAlert = false
                }
                else {
                    VM.showLocationAlert = true
                }
                return;
            }
        }
        .background(Color("Linear2"))
        .onAppear(perform: {
            VM.FBInitialize()
            print("update Available ----", isUpdateAvailable)
//            _ = try? isUpdateAvailable { (update, error) in
//                if error != nil {
//                } else if update ?? false {
//                    self.showUdpateAlert = true
//                    print("update Available ----")
//                }
//            }
            UITabBar.appearance().unselectedItemTintColor = .systemGray
            UITabBar.appearance().backgroundColor = UIColor(Color("Linear2"))
            UITabBarItem.appearance().badgeColor = UIColor(Color.accentColor)
        })
    }
    
    func updateApp(){
        if let url = URL(string: "itms-apps://itunes.apple.com/app/id1585798685") {
            UIApplication.shared.open(url)
        }
    }
    
//    func stopTimer() {
//        self.timer.upstream.connect().cancel()
//    }
    
}

class TabRouter: ObservableObject {
    static let shared = TabRouter()
    @Published var selectedTab = Tab.ride
}

#Preview {
    TabScreenView()
}
