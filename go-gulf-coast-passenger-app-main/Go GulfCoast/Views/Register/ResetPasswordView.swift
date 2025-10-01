//
//  ResetPasswordView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 05/09/2024.
//


import SwiftUI

struct ResetPasswordView: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject var VM: ForgotPasswordVM = ForgotPasswordVM()
    @FocusState private var isFocus: Bool
    @FocusState private var focusedField: Field?
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    let email: String?
    let code: String?
    
    enum Field: Int, Hashable {
        case password
        case confirmPassword
    }
    init(email: String? = nil, code: String? = nil) {
        self.email = email
        self.code = code
        UINavigationBar.customizeBackButton()
    }
    
    
    var body: some View {
        VStack {
            ScrollView {
                VStack {
                    VStack {
                        Text("Reset Password")
                            .font(.system(size: 16))
                            .foregroundStyle(Color.gray)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.vertical)
                        
                        
                        SecuredTextFieldView(field: $VM.password, text: "Password")
                            .submitLabel(.next)
                            .focused($focusedField, equals: .password)
                            .onSubmit { self.focusNextField($focusedField) }
                        
                        SecuredTextFieldView(field: $VM.confirmPassword, text: "Confirm Password")
                            .submitLabel(.done)
                            .focused($focusedField, equals: .confirmPassword)
                            .onSubmit {
                                VM.validatePassword(email: email, code: code)
                            }
                    }
                }
                .padding()
            }
            VStack {
                Button(action: {
                    VM.validatePassword(email: email, code: code)
                }) {
                    Text("Continue")
                        .outlinedButton()
                }
            }
            .padding()
        }
        .onAppear(){
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
                focusedField = Field.password
            }
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .toolbarRole(.editor)
        .navigationTitle("Reset Password")
        .navigationBarTitleDisplayMode(.inline)
        .modifier(LoadingView(isPresented: $VM.loading))
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert.toggle()
            })
        )
        
        .modifier(
            AlertView(isPresented: $VM.successAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, primaryAction: {
                self.router.removeLast(3)
            })
        )
    }
}

#Preview {
    ResetPasswordView()
}
