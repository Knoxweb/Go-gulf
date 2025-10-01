//
//  AddEditVehicle.swift
//  Chauffeurs Network
//
//  Created by Prabin Phasikawo on 1/18/22.
//

import SwiftUI
import Alamofire

struct AddEditVehicle: View {
    @StateObject var viewModel = FleetDocumentVM()
    var fleetData: FBFleetList?
    @State var showPicker = false
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        ScrollView{
            VStack{
                VStack(alignment: .leading){
                    VStack{
                        HStack{
                            Text("Vehicle Make")
                                .fontWeight(.bold)
                                .padding(.top, 8)
                            Spacer()
                        }
                        TextField("", text: $viewModel.make)
                            .cardStyleModifier()
                    }
                    Spacer()
                    VStack{
                        HStack{
                            Text("vehicle Model")
                                .fontWeight(.bold)
                                .padding(.top, 8)
                            Spacer()
                        }
                        TextField("", text: $viewModel.model)
                            .cardStyleModifier()
                    }
                    Spacer()
                    VStack{
                        HStack{
                            Text("Vehicle Registration Number")
                                .fontWeight(.bold)
                                .padding(.top, 8)
                            Spacer()
                        }
                        TextField("", text: $viewModel.reg)
                            .cardStyleModifier()
                            .keyboardType(.numbersAndPunctuation)
                    }
                    Spacer()
                    VStack{
                        HStack{
                            Text("Color")
                                .fontWeight(.bold)
                                .padding(.top, 8)
                            Spacer()
                        }
                        TextField("", text: $viewModel.color)
                            .cardStyleModifier()
                    }
                    Spacer()
                    VStack{
                        HStack{
                            VStack(alignment: .leading){
                                Text("Vehicle Photo")
                                    .fontWeight(.medium)
                                    .foregroundColor(Color("AccentColor"))
                                Text("Upload Photo")
                                    .font(.caption2)
                                    .foregroundColor(.black).opacity(0.5)
                            }
                            Spacer()
                            fleetDocumentImage(VM: viewModel, field: $viewModel.vehicleImage, value: .constant(fleetData?.fleet_image ?? ""))
                        }
                    }
                    .cardStyleModifier()
                    
                    VStack {
                        HStack{
                            Text("Insurance".uppercased())
                                .foregroundColor(.black).opacity(0.5)
                                .padding(.bottom)
                            Spacer()
                        }
                        VStack{
                            HStack{
                                Text("Insurance Number")
                                    .fontWeight(.bold)
                                    .padding(.top, 8)
                                Spacer()
                            }
                            TextField("", text: $viewModel.insuranceNumber)
                                .keyboardType(.numbersAndPunctuation)
                                .cardStyleModifier()
                        }
                        
                        VStack{
                            HStack{
                                Text("Insurance Expiry")
                                    .fontWeight(.bold)
                                    .padding(.top, 8)
                                Spacer()
                                DatePicker(selection: $viewModel.insuranceExpiry, in: Date()..., displayedComponents: [.date]) {
                                    Text("")
                                }
                                .labelsHidden()
                            }
                        }
                        .cardStyleModifier()
                        
                        VStack{
                            HStack{
                                VStack(alignment: .leading){
                                    Text("Insurance Image")
                                        .fontWeight(.medium)
                                        .foregroundColor(Color("AccentColor"))
                                    Text("Upload Photo")
                                        .font(.caption2)
                                        .foregroundColor(.black).opacity(0.5)
                                }
                                Spacer()
                                fleetDocumentImage(VM: viewModel, field: $viewModel.insuranceImage, value: .constant(fleetData?.insurance_image ?? ""))
                            }
                        }
                        .cardStyleModifier()
                    }
                    .padding(.top, 30)
                    
                    
                    VStack {
                        HStack{
                            Text("PHV Licence".uppercased())
                                .foregroundColor(.black).opacity(0.5)
                                .padding(.bottom)
                            Spacer()
                        }
                        
                        VStack{
                            HStack{
                                Text("Insurance Expiry")
                                    .fontWeight(.bold)
                                    .padding(.top, 8)
                                Spacer()
                                DatePicker(selection: $viewModel.phvLicenceExpiry, in: Date()..., displayedComponents: [.date]) {
                                    Text("")
                                }
                                .labelsHidden()
                            }
                        }
                        .cardStyleModifier()
                        
                        VStack{
                            HStack{
                                VStack(alignment: .leading){
                                    Text("PHV Licence Image")
                                        .fontWeight(.medium)
                                        .foregroundColor(Color("AccentColor"))
                                    Text("Upload Photo")
                                        .font(.caption2)
                                        .foregroundColor(.black).opacity(0.5)
                                }
                                Spacer()
                                fleetDocumentImage(VM: viewModel, field: $viewModel.phpLicenceImage, value: .constant(fleetData?.phv_licence_image ?? ""))
                            }
                        }
                        .cardStyleModifier()
                    }
                    .padding(.top, 30)

                    
                    VStack {
                        HStack{
                            Text("MOT Certificate".uppercased())
                                .foregroundColor(.black).opacity(0.5)
                                .padding(.bottom)
                            Spacer()
                        }
                        
                        VStack{
                            HStack{
                                Text("Insurance Expiry")
                                    .fontWeight(.bold)
                                    .padding(.top, 8)
                                Spacer()
                                DatePicker(selection: $viewModel.motExpiry, in: Date()..., displayedComponents: [.date]) {
                                    Text("")
                                }
                                .labelsHidden()
                            }
                        }
                        .cardStyleModifier()
                        
                        VStack{
                            HStack{
                                VStack(alignment: .leading){
                                    Text("MOT Certificate Image")
                                        .fontWeight(.medium)
                                        .foregroundColor(Color("AccentColor"))
                                    Text("Upload Photo")
                                        .font(.caption2)
                                        .foregroundColor(.black).opacity(0.5)
                                }
                                Spacer()
                                fleetDocumentImage(VM: viewModel, field: $viewModel.motImage, value: .constant(fleetData?.mot_image ?? ""))
                            }
                        }
                        .cardStyleModifier()
                    }
                    .padding(.top, 30)

                    
                    VStack {
                        HStack{
                            Text("Insurance Certificate".uppercased())
                                .foregroundColor(.black).opacity(0.5)
                                .padding(.bottom)
                            Spacer()
                        }
                        
                        VStack{
                            HStack{
                                Text("Expiry Date")
                                    .fontWeight(.bold)
                                    .padding(.top, 8)
                                Spacer()
                                DatePicker(selection: $viewModel.motExpiry, in: Date()..., displayedComponents: [.date]) {
                                    Text("")
                                }
                                .labelsHidden()
                            }
                        }
                        .cardStyleModifier()
                    }
                    .padding(.top, 30)

                    
                    
                }
            }
            .frame(maxWidth: .infinity)
            .padding(.horizontal)
            .navigationDestination(isPresented: $viewModel.navigateToFleetList) {
                VehicleListedView()
            }
        }
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
        .toolbarRole(.editor)
        .modifier(LoadingView(isPresented: $viewModel.loading))
        .navigationBarBackButtonHidden(viewModel.loading)
        .onAppear(){
            print(self.fleetData as Any, "fleetdatatatatatatatattatatata")
            viewModel.fetchData(data: self.fleetData!)
        }
        .navigationBarTitle("Fleet Document", displayMode: .inline)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button (action : {
                    //                        if(self.fleetData?.id != "") {
                    //                            viewModel.uploadDocument()
                    //                        }
                    //                        else{
                    viewModel.saveDocument(fleetId: fleetData?.id)
                    //                        }
                }) {
                    HStack {
                        Text("Save").bold()
                    }
                }
            }
        }
    }
    
}



struct fleetDocumentImage: View {
    var VM: FleetDocumentVM
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
    @State var showPreview = false
    
    
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
        .sheet(isPresented: self.$showPreview) {
            NavigationView {
                ImagePreview(isPresented: self.$showPreview, image: self.$field)
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




struct ImagePreview: View {
    @Binding var isPresented: Bool
    @Binding var image: UIImage
    @EnvironmentObject var router: Router
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        VStack {
            VStack {
                ScrollView {
                    Image(uiImage: self.image)
                        .resizable()
                        .scaledToFit()
                        .frame(maxHeight: .infinity)
                }
            }
            .padding()
            .background(Color.linearGradient.ignoresSafeArea())
            .frame(maxHeight: .infinity)
            .frame(maxWidth: .infinity)
            .navigationTitle("Document Preview")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(action: {
                        self.presentationMode.wrappedValue.dismiss()
                    }) {
                        Text("Close")
                            .font(.title3)
                            .foregroundStyle(Color.black)
                    }
                }
            }
        }
    }
}



struct AddEditVehicle_Previews: PreviewProvider {
    static var previews: some View {
        AddEditVehicle()
    }
}
