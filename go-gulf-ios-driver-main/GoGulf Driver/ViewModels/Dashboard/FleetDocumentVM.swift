//
//  FleetDocumentVM.swift
//  SlyykDriver
//
//  Created by Office on 18/12/2022.
//

import Foundation
import UIKit


class FleetDocumentVM: BaseObservableObject {
    
    @Published var id: Int?
    @Published var fleetId = 0
    @Published var vecClass = ""
    @Published var reg = ""
    @Published var color = ""
    @Published var type = ""
    @Published var make = ""
    @Published var model = ""
    @Published var insuranceNumber = ""
    
    @Published var vehicleImage: UIImage = UIImage()
    @Published var insuranceImage: UIImage = UIImage()
    @Published var phpLicenceImage: UIImage = UIImage()
    @Published var motImage: UIImage = UIImage()
    
    @Published var insuranceExpiry = Date()
    @Published var phvLicenceExpiry = Date()
    @Published var insuranceCertificateExpiry = Date()
    @Published var motExpiry = Date()
    
    
    @Published var insurance_expiry = ""
    
    @Published var navigateToFleetList = false
    @Published var showSuccess = false
    @Published var defaultRes: DefaultResponse?
    @Published var insuranceExpiryYear = Date().year
    @Published var insuranceExpiryMonth = Calendar.current.component(.month, from: Date())
    
    
    func fetchData(data: FBFleetList){
        self.loading = true
        let dateformatter = DateFormatter()
        dateformatter.dateFormat = "dd/MM/yyyy"
        
        self.id = data.id ?? 0
        self.make = data.make ?? ""
        self.model = data.modal ?? ""
        self.reg = data.vehicle_registration_number ?? ""
        self.color = data.color ?? ""
        self.insuranceNumber = data.insurance_no ?? ""
        self.insuranceExpiry = stringToDate(data.insurance_expiry_date ?? "") ?? Date()
        self.phvLicenceExpiry = stringToDate(data.phv_licence_expiry_date ?? "") ?? Date()
        self.insuranceCertificateExpiry = stringToDate(data.insurance_certificate_expiry_date ?? "") ?? Date()
        self.motExpiry = stringToDate(data.mot_expiry_date ?? "") ?? Date()
        self.loading = false
    }
    
    public func saveDocument(fleetId: Int?) {
        if insuranceImage == UIImage() || vehicleImage == UIImage() {
            self.alertTitle = "Fields Missing"
            self.alertMessage = "Please upload all the fields"
            self.showAlert = true
            return
        }
        
        
        let images = ["insurance_image": insuranceImage, "fleet_image": vehicleImage, "phv_licence_image": phpLicenceImage, "mot_image": motImage]
        let param = [
            "vehicle_registration_number": reg,
            "make": make,
            "modal": model,
            "color": color,
            "phv_licence_expiry_date": "\(dateToString(phvLicenceExpiry))",
            "insurance_expiry_date": "\(dateToString(insuranceExpiry))",
            "insurance_certificate_expiry_date": "\(dateToString(insuranceCertificateExpiry))",
            "mot_expiry_date": "\(dateToString(motExpiry))",
            "insurance_no": insuranceNumber
        ]
        
        self.loading = true
        NetworkManager.shared.UPLOAD(
            to: APIEndpoints.fleetDocument("\(fleetId ?? 0)"),
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
                self.showSuccess.toggle()
                
            case .failure(let error):
                let err = NetworkConnection().handleNetworkError(error)
                self.alertTitle = "\(err.title ?? "")"
                self.alertMessage =  "\(err.message ?? "")"
                self.showAlert.toggle()
            }
        } receiveValue: { (response: DefaultResponse) in
            self.defaultRes = response
            print(response, "resfsdfdsfdsfsdfsdfsdf")
        }
        .store(in: &cancellables)
    }
}
