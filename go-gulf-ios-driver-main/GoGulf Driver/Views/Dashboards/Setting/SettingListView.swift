//
//  SettingListView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 12/15/21.
//

import SwiftUI

struct SettingListView: View {
    @StateObject var viewModel = SettingsVM()
    @State var showSheet = false
    @State var showingAlert = false
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
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
                            }
                        }
                        NavigationLink(destination: LegalView(endpoint: "driver_privacy_policy")) {
                            HStack {
                                Image(systemName: "doc.text")
                                    .frame(width: 40)
                                    .imageScale(.large)
                                    .foregroundColor(Color.accentColor)
                                Text("Privacy")
                            }
                        }
                        
                        NavigationLink(destination: LegalView(endpoint: "driver_term_of_use")) {
                            HStack {
                                Image(systemName: "doc")
                                    .frame(width: 40)
                                    .imageScale(.large)
                                    .foregroundColor(Color.accentColor)
                                Text("Terms and Conditions")
                            }
                        }
                    }
                    .listRowBackground(Color("Card"))
                }
                .clearListBackground()
            }
            .toolbarRole(.editor)
            .navigationBarTitle("Setting", displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationDestination(isPresented: $viewModel.navigateToHome) {
                homeUIView()
            }
            .alert(isPresented: $showingAlert) {
                Alert(
                    title: Text("Are you sure?"),
                    message: Text("You want to delete Account?"),
                    primaryButton: .destructive(Text("Delete")) {
                        viewModel.confirmDelete(router: router, appRoot: appRootManager)
                    },
                    secondaryButton: .cancel()
                )
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

