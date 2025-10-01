//
//  walkthrough.swift
//  Slyyk
//
//  Created by Mac on 4/25/22.
//

import SwiftUI

struct WalkthroughScreen: Identifiable {
    let id: Int
    let img: String
    let title: String
    let text: String
}

private var slideParam = [
    WalkthroughScreen(
        id: 1,
        img: "walkthrough1",
        title: "Upload Your Documents",
        text: "Provide your driver's licence and vehicle details to complete your registration"
    ),
    WalkthroughScreen(
        id: 2,
        img: "walkthrough2",
        title: "Instant Payment",
        text: "Get instant payment in your bank account"
    ),
    WalkthroughScreen(
        id: 3,
        img: "walkthrough3",
        title: "Work When You Want",
        text: "Have the flexibility to choose your own hours"
    ),
]

var totalPages = 3

struct Walkthrough: View{
    @AppStorage("currentPage") var currentPage = 1
    
    var body: some View{
        
        VStack{
            ForEach(slideParam) { item in
                if currentPage == item.id {
                    ScreenView(image: item.img, title: item.title, text: item.text)
                        .transition(.opacity)
                }
            }
        }
        
        .navigationBarHidden(true)
        .ignoresSafeArea(.all)
        
    }
}

struct ScreenView: View {
    var image: String
    var title: String
    var text: String
    @AppStorage("currentPage") var currentPage = 1
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    
    var body: some View {
        VStack(alignment: .center){
            Spacer()
            Image("\(image)")
                .padding(.bottom, 40)
            Text("\(title)")
                .font(.system(size: 20))
                .fontWeight(.bold)
                .padding()
                .multilineTextAlignment(.center)
                .padding(.leading, 10)
                .frame(maxWidth: .infinity, alignment: .center)
            
            Text("\(text)")
                .multilineTextAlignment(.center)
                .padding(.horizontal)
            Spacer()
            HStack {
                Spacer()
                Button(action: {
                    withAnimation(.default){
                        if currentPage < totalPages {
                            currentPage += 1
                        }
                        else{
                            appRootManager.currentRoot = .homeScreen
                            router.popToRoot()
                        }
                    }
                }) {
                    Text(currentPage < 3 ? "Next" : "Finish")
                        .foregroundStyle(Color("ThemeColor"))
                        .padding(.vertical, 8)
                        .padding(.horizontal, 30)
                        .background(Color.accentColor)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                }
                .padding()
            }
            .padding(.bottom, 30)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
        .ignoresSafeArea(.all)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .onAppear(){
            UserDefaults.standard.set("true", forKey: "isWalkthoughSeen")
        }
    }
}




struct walkthrough_Previews: PreviewProvider {
    static var previews: some View {
        Walkthrough()
    }
}
