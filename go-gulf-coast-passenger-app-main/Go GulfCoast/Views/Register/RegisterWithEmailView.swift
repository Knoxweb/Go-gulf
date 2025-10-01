//
//  RegisterWithEmail.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 05/09/2024.
//


import Foundation
import SwiftUI

struct RegisterEmailView: View {
    @StateObject var VM: RegisterWithEmailVM = RegisterWithEmailVM()
    @EnvironmentObject private var router: Router
    @Environment(\.presentationMode) var presentationMode
    
    @EnvironmentObject var appRoot: AppRootManager
    @FocusState private var focusedField: Field?
    @State var filePath: String?
    @State var uiImage: UIImage?
    @State var image: Image = Image("default")
    @State var shouldPresentImagePicker = false
    @State var shouldPresentActionScheet = false
    @State var shouldPresentCamera = false
    
    enum Field: Int, Hashable {
        case fullName
        case email
        case mobile
        case password
        case confirmPassword
    }
    init() {
        UINavigationBar.customizeBackButton()
    }
    
    
    var body: some View {
        VStack {
            ScrollView {
                VStack (spacing: 20){
                    HStack {
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 100, height: 100)
                                .clipShape(Circle())
                                .shadow(radius: 4)
                                .onTapGesture { self.shouldPresentActionScheet = true }
                        
                        
                        VStack(alignment: .leading) {
                            Text("Upload Photo")
                                .font(.title3)
                            Text("Upload picture to identify you")
                                .font(.caption)
                                .foregroundColor(Color.gray)
                        }
                        Spacer()
                    }
                    
                    TextFieldView(field: $VM.fullname, text: "Full Name", placeholder: "")
                        .submitLabel(.next)
                        .focused($focusedField, equals: .fullName)
                        .onSubmit { self.focusNextField($focusedField) }
                    
                    TextFieldView(field: $VM.email, text: "Email", placeholder: "", keyboardType: .emailAddress)
                        .submitLabel(.next)
                        .focused($focusedField, equals: .email)
                        .onSubmit { self.focusNextField($focusedField) }
                    CountryPickerView(title: "Mobile", currentCountry: $VM.userCurrentCountry, setFocus: false, phoneNumber: $VM.mobile)
                        .focused($focusedField, equals: .mobile)
                        .submitLabel(.next)
                        .onSubmit { self.focusNextField($focusedField) }
                    
                    SecuredTextFieldView(field: $VM.password, text: "Password")
                        .submitLabel(.next)
                        .focused($focusedField, equals: .password)
                        .onSubmit { self.focusNextField($focusedField) }
                    
                    SecuredTextFieldView(field: $VM.confirmPassword, text: "Confirm Password")
                        .submitLabel(.done)
                        .focused($focusedField, equals: .confirmPassword)
                        .onSubmit {
                            VM.validate()
                        }
                }
                .padding()
                VStack {
                    Button(action: {VM.validate()}) {
                        Text("Create an account")
                            .fullWithButton()
                    }
                }
            }
        }
        .onAppear(){
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.5) {
                self.focusedField = .fullName
            }
        }
        .toolbar {
            ToolbarItem(placement: .cancellationAction) {
                Button(action: {
                    VM.logout()
                }) {
                    Image(.arrowLeft)
                        .foregroundStyle(Color.black)
                }
            }
            
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .toolbarRole(.editor)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .sheet(isPresented: $shouldPresentImagePicker, onDismiss: loadImage) {
            UImagePickerView(sourceType: self.shouldPresentCamera ? .camera : .photoLibrary,  image: self.$image, isPresented: self.$shouldPresentImagePicker, uiImage: self.$uiImage,  filePath: self.$filePath)
        }.actionSheet(isPresented: $shouldPresentActionScheet) { () -> ActionSheet in
            ActionSheet(title: Text("Upload Picture"), buttons: [ActionSheet.Button.default(Text("Take Photo"), action: {
                self.shouldPresentImagePicker = true
                self.shouldPresentCamera = true
            }), ActionSheet.Button.default(Text("Choose Photo"), action: {
                self.shouldPresentImagePicker = true
                self.shouldPresentCamera = false
            }), ActionSheet.Button.cancel()])
        }
        
        .toolbarRole(.editor)
        .navigationTitle("Register")
        .navigationBarTitleDisplayMode(.inline)
        .modifier(LoadingView(isPresented: $VM.loading))
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
    }
    func loadImage() {
        if self.filePath != "" {
            VM.profileImage = self.uiImage ?? UIImage()
        }
    }
}
