//
//  SettingListView.swift
//  SwiftProject
//
//  Created by Mac on 12/15/21.
//

import SwiftUI

struct SettingListView: View {
    @StateObject var viewModel = SettingsVM()
    @State var showingAlert = false
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    
    var body: some View {
        ZStack {
            VStack {
                Form{
                    Section{
                        NavigationLink(destination: AccountUIView()) {
                            HStack {
                                Image(systemName: "person")
                                    .frame(width: 40)
                                    .imageScale(.large)
                                    .foregroundColor(Color.accentColor)
                                Text("Profile")
                                    .foregroundStyle(Color.black)
                            }
                        }
                        NavigationLink(destination: LegalView(endpoint: "passenger_privacy_policy")) {
                            HStack {
                                Image(systemName: "doc.text")
                                    .frame(width: 40)
                                    .imageScale(.large)
                                    .foregroundColor(Color.accentColor)
                                Text("Privacy")
                                    .foregroundStyle(Color.black)
                            }
                        }
                        
                        NavigationLink(destination: LegalView(endpoint: "passenger_term_of_use")) {
                            HStack {
                                Image(systemName: "doc")
                                    .frame(width: 40)
                                    .imageScale(.large)
                                    .foregroundColor(Color.accentColor)
                                Text("Terms and Conditions")
                                    .foregroundStyle(Color.black)
                            }
                        }
                    }
                    .listRowBackground(Color("Card"))
                    
                    
                }
                .clearListBackground()
            }
            .toolbarRole(.editor)
            .environment(\.sizeCategory, .medium)
            .navigationBarTitle("Account", displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationBarBackButtonHidden(viewModel.loading)
           
            .navigationDestination(isPresented: $viewModel.navigateToHome) {
                homeUIView()
            }
            if viewModel.loading {
                ActivityIndicator()
            }
        }
        .disabled(viewModel.loading)
    }
}


struct SettingListView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            SettingListView()
        }
    }
}

