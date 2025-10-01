//
//  AccountUIView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 12/15/21.
//

import SwiftUI

struct AccountUIView: View {
    @StateObject var viewModel = AccountVM()
    @StateObject private var dashVM: DashboardVM = DashboardVM()
    @EnvironmentObject var router: Router
    @State var showProfile = false
    
    var body: some View {
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
                        self.showProfile = true
//                        router.navigateTo(.profileEditScreen(name: dashVM.profileData?.name, email: dashVM.profileData?.email, phone: dashVM.profileData?.mobile))
                        
                    }
                    
                }
                
                Section(header: Text("Bank")){
                    Button(action: {
                        viewModel.showSheet = true
                    }) {
                        
                        HStack {
                            Image(systemName: "building.columns")
                                .frame(width: 50)
                            Text(viewModel.bankData?.account_number ?? "")
                        }
                        
                    }
                }
                .listRowBackground(Color("Card"))
            }
            .clearListBackground()
        }
        .toolbarRole(.editor)
        .sheet(isPresented: self.$showProfile) {
            NavigationView {
                ProfileDetailView(name: dashVM.profileData?.name, email: dashVM.profileData?.email, phone: dashVM.profileData?.mobile)
            }
        }
        .sheet(isPresented: $viewModel.showSheet) {
            NavigationView {
                AddEditBankAccount(showBankAccountSheet: $viewModel.showSheet, bankName: viewModel.bankData?.bank_name, holderName: viewModel.bankData?.account_holder_name, accountHolder: viewModel.bankData?.account_number, bsbNumber: viewModel.bankData?.sort_code)
            }
        }
     
        .navigationBarTitle("Account", displayMode: .inline)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .onAppear(){
            dashVM.initialize()
            viewModel.initialize()
        }
        .onDisappear() {
            viewModel.stopListener()
        }
    }
    
}

struct AccountUIView_Previews: PreviewProvider {
    static var previews: some View {
        AccountUIView()
    }
}
