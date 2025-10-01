//
//  AccountUIView.swift
//  SwiftProject
//
//  Created by Mac on 12/15/21.
//

import SwiftUI

struct AccountUIView: View {
    @StateObject var VM = AccountVM()
    @StateObject private var dashVM: DashboardVM = DashboardVM()
    @EnvironmentObject var router: Router
    
    @State var showCardSheet = false
    var body: some View {
        ZStack {
            VStack(){
                Form{
                    Section{
                        List{
                            HStack {
                                Text("Full Name")
                                Spacer()
                                Text(dashVM.profileData?.name ?? "")
                                    .foregroundColor(Color.gray)
                            }
                            HStack {
                                Text("Email")
                                Spacer()
                                Text(dashVM.profileData?.email ?? "")
                                    .foregroundColor(Color.gray)
                            }
                            HStack {
                                Text("Phone")
                                Spacer()
                                Text(dashVM.profileData?.mobile ?? "")
                                    .foregroundColor(Color.gray)
                            }
                        }
                        .listRowBackground(Color("Card"))
                        .onTapGesture {
                            router.navigateTo(.profileEditScreen(name: dashVM.profileData?.name, email: dashVM.profileData?.email, phone: dashVM.profileData?.mobile))
                        }
                    }
                    
                    Section(header: Text("Card")){
                        Button(action: {
                            router.navigateTo(.cardsScreen)
                        }) {
                            if(VM.activeCard != nil){
                                
                                    HStack {
                                        Text(VM.activeCard?.card_masked ?? "")
                                            .foregroundColor(Color.gray)
                                        Spacer()
                                    }
                            }
                            else {
                                    Text("Add Card")
                                
                            }
                        }
                    }
                    .listRowBackground(Color("Card"))
                }
                .clearListBackground()
            }
            .toolbarRole(.editor)
            .navigationBarTitle("Account", displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .onAppear(){
                dashVM.initialize()
                VM.getCards()
            }
            if VM.loading {
                ActivityIndicator()
            }
        }
        .disabled(VM.loading)
    }
}

struct AccountUIView_Previews: PreviewProvider {
    static var previews: some View {
        AccountUIView()
    }
}
