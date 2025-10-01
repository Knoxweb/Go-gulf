//
//  ProfileDetailView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 12/16/21.
//

import SwiftUI

struct ProfileDetailView: View {
    @Environment(\.presentationMode) var presentationMode    
    @StateObject var dashVM = DashboardVM()
    @StateObject var VM: ProfileUpdateVM = ProfileUpdateVM()
    @State var name: String?
    @State var email: String?
    @State var phone: String?
    
    var body: some View {
        ZStack {
        NavigationView {
            Form{
                Section(header: Text("Full Name")) {
                    TextField("", text: $VM.name)
                }
                .listRowBackground(Color("FormField"))
                
                Section(header: Text("Email")) {
                    TextField("", text: $VM.email)
                        .keyboardType(.emailAddress)
                }
                .listRowBackground(Color("FormField"))
                
                Section(header: Text("Phone")) {
                    TextField("", text: $VM.phone)
                        .opacity(0.2)
                        .disabled(true)
                }
                .listRowBackground(Color("FormField"))
            }
            .clearListBackground()
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationBarTitle("Profile Edit", displayMode: .inline)
//            .interactiveDismissDisabled(true)
            .navigationBarItems(trailing: Button(action: {
                VM.updateProfile()
//                self.showSheetView = false
            }) {
                Text("Done").bold()
            })
            .navigationBarItems(leading: Button(action: {
                self.presentationMode.wrappedValue.dismiss()
            }) {
                Text("Cancel").bold().foregroundColor(.gray)
            })
            .modifier(
                AlertView(isPresented: $VM.showSuccess, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, primaryAction: {
                    self.presentationMode.wrappedValue.dismiss()
                })
            )
            
            .modifier(
                AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                    VM.showAlert.toggle()
                })
            )
        }
            
        .onAppear() {
            dashVM.initialize()
            VM.name = name ?? ""
            VM.email = email ?? ""
            VM.phone = phone ?? ""
        }
        .environment(\.sizeCategory, .medium)
        if VM.loading {
            ActivityIndicator()
        }
    }
    .disabled(VM.loading)
    }
}

