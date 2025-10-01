//
//  DriverDocumentVM.swift
// SlyykDriverDriver
//
//  Created by Office on 31/07/2022.
//

import Foundation
import SwiftUI
import UIKit
import Firebase


class DriverDocumentVM: BaseObservableObject {
    @Published var responseData: DriverDocumentResponse?
    @Published var res: DriverDocumentResponse?
    @Published var licenceNumber = ""
    @Published var ACN = ""
    @Published var ABN = ""
    @Published var companyName = ""
    @Published var licenceExpiry = Date()
    
    @Published var commercialPolicyNumber = ""
    
    
    @Published var base64 = ""
    @Published var navigate = false
    
    @Published var licenceFrontImage: UIImage = UIImage()
    @Published var licenceBackImage: UIImage = UIImage()
    
    @Published var acnImage: UIImage = UIImage()
    @Published var termsImage: UIImage = UIImage()
    
    
    
    
    
    @Published var defaultRes: DefaultResponse?
    private var listener: ListenerRegistration?
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var docData: DriverDocumentModel?
    @Published var showSuccess = false
    
    public func initialize() {
        Task(priority: .medium) {
            await getDocumentData()
        }
    }
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    @MainActor
    private func getDocumentData() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("documents")
        print(documentReference.path, "documentReference")
        
        listener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            guard let self = self else { return }
            if let error = error {
                print("Error fetching profile document: \(error)")
                return
            }
            guard let document = documentSnapshot else {
                print("Profile document does not exist")
                return
            }
            do {
                guard document.exists else {
                    throw NSError(domain: "Firestore", code: -1, userInfo: [NSLocalizedDescriptionKey: "Document does not exist"])
                }
                let data = try JSONSerialization.data(withJSONObject: document.data() ?? [:], options: [])
                self.docData = try JSONDecoder().decode(DriverDocumentModel.self, from: data)
                ABN = self.docData?.abn ?? ""
                ACN = self.docData?.acn ?? ""
                licenceNumber = self.docData?.licence_no ?? ""
                licenceExpiry = stringToDate(self.docData?.licence_expiry_date ?? "") ?? Date()
                commercialPolicyNumber = self.docData?.commercial_policy_number ?? ""
                companyName = self.docData?.company_name ?? ""
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    
    public func saveDocument() {
        if licenceBackImage == UIImage() || licenceFrontImage == UIImage() || acnImage == UIImage() {
            self.alertTitle = "Fields Missing"
            self.alertMessage = "Please upload all the fields"
            self.showAlert = true
            return
        }
        let images = ["licence_back_image": licenceBackImage, "licence_front_image": licenceFrontImage, "acn_image": acnImage, "terms_image": termsImage]
        let param = ["licence_expiry_date": "\(dateToString(licenceExpiry))", "company_name": companyName, "commercial_policy_number": commercialPolicyNumber, "licence_no": licenceNumber, "abn": ABN, "acn": ACN]
        
        print(images, "Images to upload")
        print(param, "Parameters to upload")
        
        self.loading = true
        NetworkManager.shared.UPLOAD(
            to: APIEndpoints.document,
            images: images,
            singleImageWithPrefix: nil,
            params: param
        )
        .sink { completion in
            self.loading = false
            switch completion {
            case .finished:
                self.alertTitle = "\(self.defaultRes?.title ?? "")"
                self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                self.showSuccess = true
                
            case .failure(let error):
                let err = NetworkConnection().handleNetworkError(error)
                self.alertTitle = "\(err.title ?? "")"
                self.alertMessage =  "\(err.message ?? "")"
                self.showAlert = true
            }
        } receiveValue: { (response: DefaultResponse) in
            self.defaultRes = response
            print(response, "Response received")
        }
        .store(in: &cancellables)
    }

}
