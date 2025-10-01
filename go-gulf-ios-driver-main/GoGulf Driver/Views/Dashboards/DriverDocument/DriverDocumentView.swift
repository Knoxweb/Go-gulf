//
//  DriverDocumentView.swift
//  Chauffeurs Network
//
//  Created by Prabin Phasikawo on 1/17/22.
//

import SwiftUI
import Alamofire

struct DriverDocumentView: View {
    @StateObject var viewModel: DriverDocumentVM = DriverDocumentVM()
    @Environment(\.presentationMode) var presentationMode
    
    @State var showPicker = false
    var body: some View {
        ScrollView{
            VStack{
                if viewModel.docData != nil {
                    VStack(alignment: .leading){
                        VStack {
                            HStack{
                                Text("Driver Licence".uppercased())
                                    .foregroundColor(.black).opacity(0.5)
                                    .padding(.bottom)
                                Spacer()
                            }
                            VStack{
                                VStack{
                                    HStack{
                                        Text("Licence Number")
                                            .fontWeight(.bold)
                                            .padding(.top, 8)
                                        Spacer()
                                    }
                                    TextField("", text: $viewModel.licenceNumber)
                                        .keyboardType(.numbersAndPunctuation)
                                        .cardStyleModifier()
                                }
                                
                                HStack{
                                    Text("Licence Expiry")
                                        .fontWeight(.bold)
                                        .padding(.top, 8)
                                    Spacer()
                                    DatePicker(selection: $viewModel.licenceExpiry, in: Date()..., displayedComponents: [.date]) {
                                        Text("")
                                    }
                                    .labelsHidden()
                                }
                                .cardStyleModifier()
                            }
                            
                            VStack{
                                HStack{
                                    VStack(alignment: .leading){
                                        Text("Driver Licence Photo (Front)")
                                            .fontWeight(.medium)
                                            .foregroundColor(Color("AccentColor"))
                                        Text("Upload Photo")
                                            .font(.caption2)
                                            .foregroundColor(.black).opacity(0.5)
                                    }
                                    Spacer()
                                    uploadImage(VM: viewModel, field: $viewModel.licenceFrontImage, value: .constant(viewModel.docData?.licence_front_image ?? ""))
                                }
                                Divider()
                                HStack{
                                    VStack(alignment: .leading){
                                        Text("Driver Licence Photo (Back)")
                                            .fontWeight(.medium)
                                            .foregroundColor(Color("AccentColor"))
                                        Text("Upload Photo")
                                            .font(.caption2)
                                            .foregroundColor(.black).opacity(0.5)
                                    }
                                    Spacer()
                                    uploadImage(VM: viewModel, field: $viewModel.licenceBackImage, value: .constant(viewModel.docData?.licence_back_image ?? ""))
                                }
                            }
                            .frame(maxWidth: .infinity)
                            .cardStyleModifier()
                        }
                        .padding(.top, 30)

                        VStack {
                            HStack{
                                Text("Company & Driver".uppercased())
                                    .foregroundColor(.black).opacity(0.5)
                                    .padding(.bottom)
                                Spacer()
                            }
                            VStack{
                                VStack{
                                    HStack{
                                        Text("Company Name")
                                            .fontWeight(.bold)
                                            .padding(.top, 8)
                                        Spacer()
                                    }
                                    TextField("", text: $viewModel.companyName)
                                        .cardStyleModifier()
                                }
                                
                                VStack{
                                    HStack{
                                        Text("Driver's Licence Issuing State")
                                            .fontWeight(.bold)
                                            .padding(.top, 8)
                                        Spacer()
                                    }
                                    TextField("", text: $viewModel.ABN)
                                        .cardStyleModifier()
                                }
                                
                                VStack{
                                    HStack{
                                        Text("Commercial Insurance Company Name")
                                            .fontWeight(.bold)
                                            .padding(.top, 8)
                                        Spacer()
                                    }
                                    TextField("", text: $viewModel.ACN)
                                        .keyboardType(.numbersAndPunctuation)
                                        .cardStyleModifier()
                                }
                                
                                VStack{
                                    HStack{
                                        Text("Commercial Policy Number")
                                            .fontWeight(.bold)
                                            .padding(.top, 8)
                                        Spacer()
                                    }
                                    TextField("", text: $viewModel.commercialPolicyNumber)
                                        .keyboardType(.numbersAndPunctuation)
                                        .cardStyleModifier()
                                }
                            }
                            
                            VStack{
                                HStack{
                                    VStack(alignment: .leading){
                                        Text("Other Documents")
                                            .fontWeight(.medium)
                                            .foregroundColor(Color("AccentColor"))
                                        Text("Upload Photo")
                                            .font(.caption2)
                                            .foregroundColor(.black).opacity(0.5)
                                    }
                                    Spacer()
                                    uploadImage(VM: viewModel, field: $viewModel.acnImage, value: .constant(viewModel.docData?.acn_image ?? ""))
                                }
                                Divider()
                                HStack{
                                    VStack(alignment: .leading){
                                        Text("T&C Signed Copy")
                                            .fontWeight(.medium)
                                            .foregroundColor(Color("AccentColor"))
                                        Text("Upload Photo")
                                            .font(.caption2)
                                            .foregroundColor(.black).opacity(0.5)
                                    }
                                    Spacer()
                                    uploadImage(VM: viewModel, field: $viewModel.termsImage, value: .constant(viewModel.docData?.terms_image ?? ""))
                                }
                            }
                            .frame(maxWidth: .infinity)
                            .cardStyleModifier()
                        }
                        .padding(.top, 30)
                    }
                }
            }
            .padding(.horizontal)
            .navigationDestination(isPresented: $viewModel.navigate) {
                DashboardUIView()
            }
        }
        .frame(maxWidth: .infinity)
        .toolbarRole(.editor)
   
        .modifier(
            AlertView(isPresented: $viewModel.showSuccess, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, secondaryAction: {
                self.presentationMode.wrappedValue.dismiss()
            })
        )
        
        .modifier(
            AlertView(isPresented: $viewModel.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, secondaryAction: {
                viewModel.showAlert = false
            })
        )
        
        .onAppear(){
            viewModel.initialize()
            UINavigationBar.customizeBackButton()
        }
        .onDisappear() {
            viewModel.stopListener()
        }
        .navigationBarTitle("Driver Document", displayMode: .inline)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .modifier(LoadingView(isPresented: $viewModel.loading))
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button (action : {
                    viewModel.saveDocument()
                }) {
                    HStack {
                        Text("Save").bold()
                    }
                }
            }
        }
    }
}


struct DriverLicenceCard: View{
    @ObservedObject var VM: DriverDocumentVM
    @Binding var licenceNumber: String
    @Binding var licenceExpiry: String
    @Binding var licenceExpiryMonth: Int
    @Binding var licenceExpiryYear: Int
    @State var showPicker = false
    var body: some View{
        VStack{
            HStack{
                Text("Licence Number")
                    .fontWeight(.bold)
                    .padding(.top, 8)
                Spacer()
            }
            TextField("", text: $licenceNumber)
                .keyboardType(.numbersAndPunctuation)
                .cardStyleModifier()
        }
        Spacer()
        VStack{
            HStack{
                Text("Expiry")
                    .fontWeight(.bold)
                    .padding(.top, 8)
                Spacer()
                DatePicker(selection: $VM.licenceExpiry, in: Date()..., displayedComponents: [.date]) {
                    Text("")
                }
                .labelsHidden()
            }
        }
        .cardStyleModifier()
    }
}

struct uploadImage: View {
    var VM: DriverDocumentVM
    @State var fieldImage: UIImage? = UIImage()
    @Binding var field: UIImage
    @Binding var value: String
    @State var shouldPresentImagePicker = false
    @State var shouldPresentActionScheet = false
    @State var shouldPresentCamera = false
    @State var base64Image = ""
    @State var filePath: String?
    @State var uiImage: UIImage?
    @State var image: Image = Image("Camera")
    @State var redacting = false
    
    
    var body: some View {
        Button(action: {
            self.shouldPresentActionScheet = true
        }) {
            HStack{
                if value != "" {
                        Image(uiImage: self.field)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 50, height: 50)
                }
                else {
                    image
                        .resizable()
                        .scaledToFit()
                        .frame(width: 50, height: 50)
                }
            }
        }
        .onAppear() {
            self.loadFile(from: value) { image, fileName, pdfData, contentType in
                if let image = image {
                    self.field = image
                    print(self.field, "image fieldldldlldldl")
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
        self.field = self.uiImage ?? UIImage()
    }
    
    private func loadFile(from url: String, completion: @escaping (UIImage?, String?, Data?, String?) -> Void) {
        let apiToken = UserDefaults.standard.string(forKey: "accessToken") ?? ""
        let headers: HTTPHeaders = [
            "Authorization": "Bearer \(apiToken)",
            "Accept": "application/json",
            "AppToken": "\(Env.apiToken)"
        ]
        
        print(url, "uRLKLRLRLLLRLRLLRLRLRLLRLRLRLRLRLRLRLRL")
        
        self.redacting = true
        AF.request(url, headers: headers).responseData { response in
            self.redacting = false
            switch response.result {
            case .success(let data):
                guard let fileName = self.getFileName(from: url) else {
                    print("Failed to extract filename from URL.")
                    completion(nil, nil, nil, nil)
                    return
                }
                
                let contentType = response.response?.mimeType
                
                if contentType == "application/pdf" {
                    // Handle PDF
                    if let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
                        let fileURL = documentsDirectory.appendingPathComponent(fileName)
                        do {
                            try data.write(to: fileURL)
                            print("PDF saved to:", fileURL)
//                            self.selectedFile = fileURL
                            completion(nil, fileName, data, contentType)
                        } catch {
                            print("Error saving PDF file: \(error)")
                            completion(nil, nil, nil, nil)
                        }
                    }
                } else if let loadedImage = UIImage(data: data) {
                    // Handle Image
                    completion(loadedImage, fileName, nil, contentType)
                } else {
                    print("Data is neither an image nor a PDF.")
                    completion(nil, nil, nil, nil)
                }
                print("Received data:", data)
                
            case .failure(let error):
                print("Request failed with error: \(error)")
                completion(nil, nil, nil, nil)
            }
        }
    }
    
    
    private func getFileName(from url: String) -> String? {
        let components = url.components(separatedBy: "/")
        guard let lastComponent = components.last, !lastComponent.isEmpty else {
            return nil
        }
        return lastComponent
    }
    
    func imageToData(image: UIImage) -> Data? {
        return image.jpegData(compressionQuality: 1.0)
    }
}


struct DriverDocumentView_Previews: PreviewProvider {
    static var previews: some View {
        DriverDocumentView()
    }
}
