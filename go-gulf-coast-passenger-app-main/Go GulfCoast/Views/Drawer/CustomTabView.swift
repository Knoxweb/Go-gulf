//
//  CustomTabView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 19/09/2024.
//

import SwiftUI

struct CustomTabView: View {
    @Binding var currentTab: String
    @Binding var showMenu: Bool
 
    var body: some View {
        VStack {
            
//            TabView(selection: $currentTab) {
//                NavigationView {
//                    DashboardUIView(showMenu: $showMenu)
//                        .tag("Home")
//                        .onTapGesture {
//                            showMenu = false
//                        }
//                }
//                
//                NotificationsView()
//                    .tag("Discover")
//                
//                NavigationView {
//                    BookNowUIView(showMenu: $showMenu)
//                        .tag("Devices")
//                        .onTapGesture {
//                            showMenu = false
//                        }
//                }
//                
//                NavigationView {
//                    BookNowUIView(showMenu: $showMenu)
//                        .tag("Profile")
//                        .onTapGesture {
//                            showMenu = false
//                        }
//                }
//            }
        }
        .disabled(showMenu)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .overlay(
            Button {
                withAnimation(.spring()) {
                    showMenu = false
                }
            }label: {
                Image(systemName: "xmark")
                    .font(.title2)
                    .foregroundStyle(Color.black)
            }
            .opacity(showMenu ? 1 : 0)
            .padding()
            .padding(.top)
            ,alignment: .topLeading
        )
        .background(
            Color.black
        )
    }
}
