//
//  GetFleetResponse.swift
//  SlyykDriver
//
//  Created by Office on 18/12/2022.
//

import Foundation


struct FBFleetList: Codable, Hashable {
    let class_name: String?
    let color: String?
    let fleet_image: String?
    let id: Int?
    let image_path: String?
    let insurance_expiry_date: String?
    
    let phv_licence_expiry_date: String?
    let phv_licence_image: String?
    let mot_expiry_date: String?
    let mot_image: String?
    let insurance_certificate_expiry_date: String?
    let insurance_image: String?
    let insurance_no: String?
    let is_active: Bool?
    let make: String?
    let modal: String?
    let name: String?
    let passengers: Int?
    let pet: Int?
    let type_name: String?
    let vehicle_registration_number: String?
    let wheelchait: Int?
}
