//
//  MainView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 19/09/2024.
//

import SwiftUI

struct MainView: View {
    @State var currentTab: String = "home"
    
    @State var showMenu: Bool = false
    
    
    init() {
        UITabBar.appearance().isTranslucent = true
    }
    
    
    var body: some View {
        ZStack {
            SideMenu(currentTab: $currentTab)
            CustomTabView(currentTab: $currentTab, showMenu: $showMenu)
                .cornerRadius(showMenu ? 25 : 0)
                .rotation3DEffect(.init(degrees: showMenu ? -15 : 0), axis: (x: 0, y: 1, z: 0), anchor: .trailing)
                .offset(x: showMenu ? getRect().width / 2 : 0)
                .ignoresSafeArea()
        }
        .preferredColorScheme(.dark)
    }
}


struct SideMenu: View {
    @Binding var currentTab: String
    @Namespace var animation
    var body: some View {
        VStack {
            HStack (spacing: 15){
                Image(.car)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 45, height: 45)
                    .clipShape(Circle())
                
                Text("menu")
                    .font(.title2.bold())
                    .foregroundStyle(.black)
            }
            .padding()
            .frame(maxWidth: .infinity, alignment: .leading)
            
            VStack(alignment: .leading, spacing: 25) {
                CustomTabButton(icon: "theatermasks.fill", title: "Home")
                CustomTabButton(icon: "theatermasks.fill", title: "Discover")
                CustomTabButton(icon: "theatermasks.fill", title: "Devices")
                CustomTabButton(icon: "theatermasks.fill", title: "Profile")
                CustomTabButton(icon: "theatermasks.fill", title: "Home")
                CustomTabButton(icon: "theatermasks.fill", title: "Home")
            }
            .padding()
            .padding(.top, 60)
            .frame(width: getRect().width / 2, alignment: .leading)
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding(.leading, 10)
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .background(Color.linearGradient.ignoresSafeArea())
    }
    
    @ViewBuilder
    func CustomTabButton(icon: String, title: String) -> some View {
        Button {
            withAnimation {
                currentTab = title
            }
        } label: {
            HStack (spacing: 12){
                Image(systemName: icon)
                    .font(.title3)
                    .frame(width: currentTab == title ? 48 : nil, height: 48)
                    .foregroundStyle(currentTab == title ? Color.accentColor : Color.black)
                    .background(
                        ZStack {
                            if currentTab == title {
                                Color.white
                                    .clipShape(Circle())
                                    .matchedGeometryEffect(id:  "TABCIRCLE", in: animation)
                            }
                        }
                    )
                
                Text(title)
                    .font(.callout)
                    .fontWeight(.semibold)
                    .foregroundStyle(.black)
            }
            .padding(.trailing, 18)
            .background(
                ZStack {
                    if currentTab == title {
                        Color.accentColor.clipShape(.capsule)
                         .matchedGeometryEffect(id:  "TABCIRCLE", in: animation)
                    }
                }
            )
        }
        .offset(x: currentTab == title ? 15 : 0)
    }
}


extension View {
    func getRect() -> CGRect {
        return UIScreen.main.bounds
    }
}


extension View {
    func getSafeArea() -> UIEdgeInsets {
        guard let screen = UIApplication.shared.connectedScenes.first as? UIWindowScene else {
            return .zero
        }
        
        guard let safeArea = screen.windows.first?.safeAreaInsets
        else {
            return .zero
        }
        return safeArea
    }
}
