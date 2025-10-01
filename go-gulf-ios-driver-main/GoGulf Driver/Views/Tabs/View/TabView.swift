//
//  TabView.swift
//  Slyyk Driver
//
//  Created by Prabin Phasikawo on 26/03/2024.
//

import SwiftUI

enum Tab {
    case offers, ride, profile, notification, menu
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
                    DispatchView()
                    .toolbar(.hidden)
//                }
                .tabItem {
                    Image(systemName: "hand.tap")
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .frame(height: 50)
                        .foregroundColor(self.tabRouter.selectedTab == .offers ? Color.accentColor : Color.black.opacity(0.5))
                    withAnimation() {
                        Text("Offers")
                            .foregroundColor(self.tabRouter.selectedTab == .offers ? Color.accentColor : Color.black.opacity(0.5))
                    }
                }
                .tag(Tab.offers)
                
//                NavigationView() {
                    ScheduledBookingsView()
                        .toolbar(.hidden)
//                }
                .tabItem {
                    Image(systemName: "calendar.badge.checkmark")
                        .renderingMode(.template)
                        .resizable()
                        .scaledToFit()
                        .frame(width: 40, height: 40)
                    
                    withAnimation() {
                        Text("Pickups")
                            .foregroundColor(self.tabRouter.selectedTab == .ride ? Color.accentColor : Color.black.opacity(0.5))
                    }
                }
                .tag(Tab.ride)
                
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
        .modifier(
            AlertView(
                isPresented: $VM.showLocationAlert,
                icon: .homeLogo,
                primaryButtonText: .constant("Open Settings"),
                title: .constant("Enable location"),
                desc: .constant("We need access to your location to be able to use this service. We collects location data to track your location when your app is opened or in background. Please set your location access permission to 'ALWAYS' in setting"),
                backdropDismiss: false,
                outlineButton: true,
                hasDismissButton: true,
                primaryAction: {
                    self.openSetting()
                }
            )
        )
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
        .onReceive(timer) { input in
            VM.updatePosition()
        }
        .onDisappear() {
//            self.stopTimer()
            VM.stopListener()
        }
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
            VM.updatePosition()
            VM.FBInitialize(tabRouter: tabRouter)
//            _ = try? isUpdateAvailable { (update, error) in
//                print("update Available ----", update, error)
//                if error != nil {
//                } else if update ?? false {
//                    self.showUdpateAlert = true
//                }
//            }
            VM.showLocationAlert = !VM.enableLocationServices()
            UITabBar.appearance().unselectedItemTintColor = .systemGray
            UITabBar.appearance().backgroundColor = UIColor(Color("Linear2"))
            UITabBarItem.appearance().badgeColor = UIColor(Color.accentColor)
        })
    }
    
    func updateApp(){
        if let url = URL(string: "itms-apps://itunes.apple.com/app/id1585805676") {
            UIApplication.shared.open(url)
        }
    }
    
    func openSetting() {
            if let BUNDLE_IDENTIFIER = Bundle.main.bundleIdentifier,
               let url = URL(string: "\(UIApplication.openSettingsURLString)&path=LOCATION/\(BUNDLE_IDENTIFIER)") {
                DispatchQueue.main.async {
                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
                }
            }
    }
    
//    func stopTimer() {
//        self.timer.upstream.connect().cancel()
//    }
    
}

class TabRouter: ObservableObject {
    static let shared = TabRouter()
    @Published var selectedTab = Tab.offers
}

#Preview {
    TabScreenView()
}
