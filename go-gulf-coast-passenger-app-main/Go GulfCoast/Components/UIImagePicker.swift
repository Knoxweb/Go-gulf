//
//  UIImagePicker.swift
//  GoGulf
//
//  Created by Mac on 6/8/22.
//

import SwiftUI

//struct UIImagePicker: View {
//    @State private var image: Image? = Image("DefaultClientPic")
//    @State private var shouldPresentImagePicker = false
//    @State private var shouldPresentActionScheet = false
//    @State private var shouldPresentCamera = false
//    @Binding var base64Image: String
//    
//    var body: some View {
//        
//        image!
//            .resizable()
//            .aspectRatio(contentMode: .fill)
//            .frame(width: 100, height: 100)
//            .clipShape(Circle())
////            .overlay(Circle().stroke(Color.white))
//            .shadow(radius: 4)
//            .onTapGesture { self.shouldPresentActionScheet = true }
//            .sheet(isPresented: $shouldPresentImagePicker, onDismiss: loadImage) {
//                SUImagePickerView(sourceType: self.shouldPresentCamera ? .camera : .photoLibrary,  image: self.$image, isPresented: self.$shouldPresentImagePicker, base64: self.$base64Image)
//            }.actionSheet(isPresented: $shouldPresentActionScheet) { () -> ActionSheet in
//                ActionSheet(title: Text("Upload Photo"), buttons: [ActionSheet.Button.default(Text("Take a photo"), action: {
//                    self.shouldPresentImagePicker = true
//                    self.shouldPresentCamera = true
//                }), ActionSheet.Button.default(Text("Choose from library"), action: {
//                    self.shouldPresentImagePicker = true
//                    self.shouldPresentCamera = false
//                }), ActionSheet.Button.cancel()])
//            }
//        
//    }
//    func loadImage() {
//        print(self.base64Image, "dfdfddfdfdfdfdfdfdfdf")
////        convertImageToBase64(image: image)
//    }
//
//}

//struct UIImagePicker_Previews: PreviewProvider {
//    static var previews: some View {
//        UIImagePicker(base64Image: .constant("data:image/jpeg;base64,"))
//    }
//}
