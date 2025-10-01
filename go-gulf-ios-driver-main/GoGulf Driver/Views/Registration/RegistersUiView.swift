//
//  RegistersUiView.swift
//  SwiftProject
//
//  Created by Mac on 12/11/21.
//

import SwiftUI

struct RegistersUiView: View {
    @StateObject var VM = RegisterVM()
    @State var shouldPresentImagePicker = false
    @State var shouldPresentActionScheet = false
    @State var shouldPresentCamera = false
    @State var base64Image = ""
    @State var filePath: String?
    @State var uiImage: UIImage?
    @State var image: Image = Image("Default")
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    @FocusState private var focusedField: Field?
    
    enum Field: Int, Hashable {
        case fullName
        case email
        case password
        case confirmPassword
    }
    
    var body: some View {
        VStack{
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
                    
                    TextFieldView(field: $VM.email, text: "Email", placeholder: "")
                        .submitLabel(.next)
                        .focused($focusedField, equals: .email)
                        .onSubmit { self.focusNextField($focusedField) }
                    
                    SecuredTextFieldView(field: $VM.password, text: "Password")
                        .submitLabel(.next)
                        .focused($focusedField, equals: .password)
                        .onSubmit { self.focusNextField($focusedField) }
                    
                    SecuredTextFieldView(field: $VM.confirmPassword, text: "Confirm Password")
                        .submitLabel(.done)
                        .focused($focusedField, equals: .confirmPassword)
                        .onSubmit {
                            VM.validate(tabRouter: tabRouter)
                        }
                }
                .padding()
            }
            VStack {
                Button(action: {
                    VM.validate(tabRouter: tabRouter)
                }) {
                    Text("Create an account")
                        .fullWithButton()
                }
            }
            .padding()
        }
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert = false
            })
        )
        .onAppear() {
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
                self.focusedField = .fullName
            }
        }
        .toolbarRole(.editor)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationBarTitle(Text("Register"), displayMode: .inline)
        .modifier(LoadingView(isPresented: $VM.loading))
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
    }
    func loadImage() {
        if self.filePath != "" {
            VM.profileImage = self.uiImage ?? UIImage()
        }
    }
    
}


struct RegistersUiView_Previews: PreviewProvider {
    static var previews: some View {
        RegistersUiView()
    }
}
