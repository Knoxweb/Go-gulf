//
//  homeUIView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 10/2/21.

import SwiftUI
import Firebase

struct homeUIView: View {
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    
    
    var body: some View {
            VStack (content: ({
                ZStack {
                    VStack {
                        Spacer()
                        Image("HomeLogo")
                            .resizable(resizingMode: .stretch)
                            .aspectRatio(contentMode: .fit)
                            .padding(.all, 100)
                        Spacer()
                    }
                    VStack {
                        Spacer()
                        Group {
                            Button(action: {
                                router.navigateTo(.phoneNumberScreen)
                            }) {
                                Text("Get Started")
                                    .fullWithButton()
                            }
                        }
                        .padding(.bottom)
                    }
                }
            }))
            .onAppear() {
                UINavigationBar.customizeBackButton()
            }
            .padding()
            .background(
                Image("theme-bg")
                    .resizable()
                    .scaledToFill()
                    .ignoresSafeArea()
            )
            .frame(maxWidth: .infinity)
            .frame(maxHeight: .infinity)
            
            .navigationBarBackButtonHidden(true)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
    }
}

struct homeUIView_Previews: PreviewProvider {
    static var previews: some View {
        homeUIView()
    }
}
