//
//  ForgotPasswordUI.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 05/09/2024.
//

import SwiftUI


struct ForGotPasswordView: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject var VM: ForgotPasswordVM = ForgotPasswordVM()
    @FocusState private var isFocus: Bool
    @FocusState private var focusedField: Field?
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    
    enum Field: Int, Hashable {
       case email
    }
    
    init() {
        UINavigationBar.customizeBackButton()
    }
    
    var body: some View {
        VStack {
            ScrollView {
                VStack {
                    VStack {
                        Text("Enter Email")
                            .font(.system(size: 16))
                            .foregroundStyle(Color.gray)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.vertical)
                        
                        TextFieldView(field: $VM.email, text: "Email", keyboardType: .emailAddress)
                            .submitLabel(.send)
                            .textContentType(.oneTimeCode)
                            .focused($focusedField, equals: .email)
                            .onSubmit {
                                VM.validate()
                            }
                    }
                }
                .padding()
            }
            VStack {
                Button(action: {
                    VM.validate()
                }) {
                    Text("Continue")
                        .fullWithButton()
                }
            }
            .padding()
        }
        .onAppear(){
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
                focusedField = Field.email
            }
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .toolbarRole(.editor)
        .navigationTitle("Forgot Password")
        .navigationBarTitleDisplayMode(.inline)
        .modifier(LoadingView(isPresented: $VM.loading))
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert.toggle()
            })
        )
        
        .modifier(
            AlertView(isPresented: $VM.successAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, primaryAction: {
                router.navigateTo(.emailOTP(email: VM.email))
            })
        )
    }
}

#Preview {
    LoginWithEmail()
}
