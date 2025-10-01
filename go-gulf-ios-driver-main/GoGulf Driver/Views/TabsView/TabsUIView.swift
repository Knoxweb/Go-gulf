//
//  TabsUIView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 12/14/21.
//

import SwiftUI

//struct CurrentTabKey: EnvironmentKey {
//    static var defaultValue: Binding<TabsUIView.Tab> = .constant(.profile)
//
//}

//extension EnvironmentValues {
//    var currentTab: Binding<TabsUIView.Tab> {
//        get { self[CurrentTabKey.self] }
//        set { self[CurrentTabKey.self] = newValue }
//    }
//}

//struct TabsUIView: View {
//    enum Tab {
//        case profile
//        case bookings
//        case bookNow
//        case card
//        case menu
//    }
//    @State private var selectedTab: Tab = .profile
//
    
    
//    var body: some View {
//        TabView(selection: $selectedTab){
//            NavigationView {
//                DashboardUIView()
//            }
//            .badge(3)
//            .tabItem {
//                Label("Home", systemImage: selectedTab == .profile ? "house.fill" : "house")
//                    .environment(\.symbolVariants, .none)
//            }
//
//            .tag(Tab.profile)
//            .environment(\.currentTab, $selectedTab)
//            .navigationBarHidden(true)
//            .navigationBarBackButtonHidden(true)
//
//
//            NavigationView{
//                BookingHistoryUIView ()
//            }
//            .tabItem {
//                Label("Bookings", systemImage: selectedTab == .bookings ? "square.3.stack.3d.top.filled" : "square.3.stack.3d")
//                    .environment(\.symbolVariants, .none)
//            }
//
//            .tag(Tab.bookings)
//            .environment(\.currentTab, $selectedTab)
//            //                .hideNavigationBarIfAvailable()
//            .navigationBarHidden(true)
//
//
//            NavigationView{
////                BookingUIView ()
//            }
//            .tabItem {
//                Label("Ride Now", systemImage: selectedTab == .bookNow ? "car.fill" : "car")
//                    .environment(\.symbolVariants, .none)
//            }
//
//            .tag(Tab.bookNow)
//            .environment(\.currentTab, $selectedTab)
//            //                .hideNavigationBarIfAvailable()
//            .navigationBarHidden(true)
//
//
//            NavigationView{
//                CardDetailView ()
//            }
//            .tabItem {
//                Label("Payments", systemImage: selectedTab == .card ? "creditcard.fill" : "creditcard")
//                    .environment(\.symbolVariants, .none)
//            }
//
//            .tag(Tab.card)
//            .environment(\.currentTab, $selectedTab)
//            //                .hideNavigationBarIfAvailable()
//            .navigationBarHidden(true)
//
//
//            NavigationView{
//                MenuListView()
//            }
//            .tabItem {
//                Label("More", systemImage: selectedTab == .menu ? "ellipsis.circle.fill" : "ellipsis.circle")
//                    .environment(\.symbolVariants, .none)
//            }
//
//            .tag(Tab.menu)
//            .environment(\.currentTab, $selectedTab)
//            //                .hideNavigationBarIfAvailable()
//            .navigationBarHidden(true)
//        }
//        //        .navigationBarBackButtonHidden(true)
//        //        .hideNavigationBarIfAvailable()
//    }
//}
//
//struct TabsUIView_Previews: PreviewProvider {
//    static var previews: some View {
//        TabsUIView()
//    }
//}

