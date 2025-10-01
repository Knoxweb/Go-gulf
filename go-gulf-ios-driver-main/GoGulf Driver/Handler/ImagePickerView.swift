//
//  SUImagePickerView.swift
//  SUImagePickerView

import SwiftUI
import UIKit


extension UIImage {
    func aspectFittedToHeight(_ newHeight: CGFloat) -> UIImage {
        let scale = newHeight / self.size.height
        let newWidth = self.size.width * scale
        let newSize = CGSize(width: newWidth, height: newHeight)
        let renderer = UIGraphicsImageRenderer(size: newSize)

        return renderer.image { _ in
            self.draw(in: CGRect(origin: .zero, size: newSize))
        }
    }
}

func compressImage(image: UIImage) -> UIImage {
        let resizedImage = image.aspectFittedToHeight(100)
        resizedImage.jpegData(compressionQuality: 0.1)
        return resizedImage
}

struct UImagePickerView: UIViewControllerRepresentable {
    
    var sourceType: UIImagePickerController.SourceType = .photoLibrary
    @Binding var image: Image
    @Binding var isPresented: Bool
    @Binding var uiImage: UIImage?
    @Binding var filePath: String?
    
    func makeCoordinator() -> ImagePickerViewCoordinator {
        return ImagePickerViewCoordinator(image: $image, isPresented: $isPresented, uiImage: $uiImage, filePath: $filePath)
    }
    
    func makeUIViewController(context: Context) -> UIImagePickerController {
        let pickerController = UIImagePickerController()
        pickerController.sourceType = sourceType
        pickerController.delegate = context.coordinator
        return pickerController
    }

    func updateUIViewController(_ uiViewController: UIImagePickerController, context: Context) {
        // Nothing to update here
    }

}
class ImagePickerViewCoordinator: NSObject, UINavigationControllerDelegate, UIImagePickerControllerDelegate, UIDocumentPickerDelegate {
    
    @Binding var image: Image
    @Binding var isPresented: Bool
    @Binding var uiImage: UIImage?
    @Binding var filePath: String?
    
    init(image: Binding<Image>, isPresented: Binding<Bool>, uiImage: Binding<UIImage?>, filePath: Binding<String?>) {
        self._image = image
        self._isPresented = isPresented
        self._uiImage = uiImage
        self._filePath = filePath
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let image = info[.originalImage] as? UIImage {
            // Accessing imageURL
            if let imageURL = info[.imageURL] as? URL {
                // Getting filename with extension when image is picked from photo library
                let filenameWithExtension = imageURL.lastPathComponent
                self.filePath = filenameWithExtension
                print(filenameWithExtension)
            } else {
                // Handling case when image is captured using the camera
                let filenameWithExtension = "captured_image.jpg" // You can choose any convention here
                self.filePath = filenameWithExtension
                print(filenameWithExtension)
            }
            
            self.image = Image(uiImage: image)
            let resizedImage = UIImage.scaleImageWithDivisor(img: image, divisor: 4)
            self.uiImage = resizedImage
        }
        self.isPresented = false
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        self.isPresented = false
    }
    
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        guard let selectedFileURL = urls.first else {
            print("No file selected")
            return
        }
        
        // Getting filename with extension for PDF
        let filenameWithExtension = selectedFileURL.lastPathComponent
        self.filePath = filenameWithExtension
        print(filenameWithExtension)
        
        // Handling PDF file here
        // You can perform further operations like uploading, etc.
    }
    
    func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        self.isPresented = false
    }
}
extension UIImage {
    class func scaleImageWithDivisor(img: UIImage, divisor: CGFloat) -> UIImage {
        let size = CGSize(width: img.size.width/divisor, height: img.size.height/divisor)
        UIGraphicsBeginImageContext(size)
        img.draw(in: CGRect(x: 0, y: 0, width: size.width, height: size.height))
        let scaledImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return scaledImage!
    }
}

