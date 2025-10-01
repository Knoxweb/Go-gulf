//
//  ProfileDetailView.swift
//  SwiftProject
//
//  Created by Mac on 12/16/21.
//

import SwiftUI

struct ProfileDetailView: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject var dashVM = DashboardVM()
    @StateObject var VM: ProfileUpdateVM = ProfileUpdateVM()
    @State var name: String?
    @State var email: String?
    @State var phone: String?
    
    
    enum Field: Int, Hashable {
        case fullName
        case email
    }
    
    
    var body: some View {
        VStack {
            VStack(spacing: 10) {
                TextFieldView(field: $VM.name, text: "Full Name", placeholder: "")
                
                TextFieldView(field: $VM.email, text: "Email", placeholder: "")
                
                TextFieldView(field: $VM.phone, text: "Phone", placeholder: "")
                    .disabled(true)
                Spacer()
            }
            .padding()
            .onAppear() {
                dashVM.initialize()
                VM.name = name ?? ""
                VM.email = email ?? ""
                VM.phone = phone ?? ""
            }
        }
        .frame(maxHeight: .infinity)
        .frame(maxWidth: .infinity)
        .toolbarRole(.editor)
        .background(Color.linearGradient.ignoresSafeArea())
        .modifier(LoadingView(isPresented: $VM.loading))
        .navigationBarItems(trailing: Button(action: {
            VM.updateProfile()
        }) {
            Text("Done").bold()
                .environment(\.sizeCategory, .medium)
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
    
}
