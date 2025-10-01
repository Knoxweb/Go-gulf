//
//  LoginWithEmail.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 02/09/2024.
//

import SwiftUI

struct LoginWithEmail: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject var VM: PhoneVerifyVM = PhoneVerifyVM()
    @FocusState private var isFocus: Bool
    @FocusState private var focusedField: Field?
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    
    enum Field: Int, Hashable {
       case email
       case password
    }
    init() {
        UINavigationBar.customizeBackButton()
    }
    
    
    var body: some View {
        ScrollView {
            VStack {
                VStack {
                    VStack {
                        Text("Login with email")
                            .font(.system(size: 16))
                            .foregroundStyle(Color.gray)
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.vertical)
                        
                        TextFieldView(field: $VM.email, text: "Email", keyboardType: .emailAddress)
                            .disableAutocorrection(true)
                            .textContentType(.oneTimeCode)
                            .submitLabel(.next)
                            .focused($focusedField, equals: .email)
                            .onSubmit { self.focusNextField($focusedField) }
                        
                        SecuredTextFieldView(field: $VM.password, text: "Password")
                            .submitLabel(.go)
                            .focused($focusedField, equals: .password)
                            .onSubmit {
                                VM.validateEmailLogin()
                            }
                    }
                    VStack (alignment:.leading, spacing: 20){
                        HStack {
                            Button(action: {
                                router.navigateTo(.forgotPassword)
                            }){
                                Text("forgot Password")
                            }
                            Spacer()
                            HStack {
                                Button(action: {
                                    appRoot.currentRoot = .registerWithEmail
                                    router.popToRoot()
                                }){
                                    Text("Don't have an account?")
                                        .foregroundStyle(Color.accentColor)
                                }
                            }
                        }
                    }
                    .font(.system(size: 14))
                }
                .padding()
                
            }
            VStack {
                Button(action: {
                    VM.validateEmailLogin()
                }) {
                    Text("Continue")
                        .outlinedButton()
                }
            }
            .padding()
        }
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert.toggle()
            })
        )
//        .alert(VM.alertTitle ?? "", isPresented: $VM.showAlert) {
//            Button("OK", role: .cancel) { }
//        } message: {
//            Text(VM.alertMessage ?? "")
//        }
        .onAppear(){
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
                focusedField = Field.email
            }
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .toolbarRole(.editor)
        .navigationTitle("Email Login")
        .navigationBarTitleDisplayMode(.inline)
        .modifier(LoadingView(isPresented: $VM.loading))
       
    }
}

#Preview {
    LoginWithEmail()
}


struct SecuredTextFieldView: View {
    @Binding var field: String
    @State var text: LocalizedStringKey?
    @State var placeholder: String?
    @State var caption: String?
    @State var keyboardType: UIKeyboardType?
    @State private var isSecure = true
    @State var localizedText: LocalizedStringKey?
    @State var Localizedplaceholder: LocalizedStringKey?
    
    var body: some View {
        VStack {
            VStack (alignment: .leading, spacing: 8){
                Group {
                    if let text =  text{
                        Text(text)
                    }
                }
                .font(.system(size: 13))
                .foregroundStyle(Color.gray)
                Group {
                    if isSecure {
                        SecureField(Localizedplaceholder ?? "", text: $field)
                            .autocorrectionDisabled()
                            .textContentType(.oneTimeCode)
                            .disableAutocorrection(true)
                            .foregroundStyle(Color.black)
                    }
                    else {
                        TextField(Localizedplaceholder ?? "", text: $field)
                            .autocorrectionDisabled()
                            .textContentType(.oneTimeCode)
                            .disableAutocorrection(true)
                            .foregroundStyle(Color.black)
                    }
                }
                .padding(.horizontal)
                .textfieldModifier()
                .keyboardType(keyboardType  ?? .default)
                .overlay(
                    HStack {
                        Spacer()
                        Button(action: {
                            isSecure.toggle()
                        }) {
                            Image(systemName: isSecure ? "eye.slash" : "eye")
                                .foregroundColor(Color.black)
                                .padding()
                                .frame(height: 50)
                                .accessibilityHidden(true)
                                .opacity(0.8)
                        }
                    }
                )
            }
            Text("\(caption ?? "")")
                .font(.caption)
        }
    }
}


#Preview {
    SecuredTextFieldView(field: .constant(""), text: "Password")
}
