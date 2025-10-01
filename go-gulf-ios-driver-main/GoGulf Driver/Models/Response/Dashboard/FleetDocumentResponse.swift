//
//  FleetDocumentResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 26/11/2022.
//


import Foundation

// MARK: - Welcome
struct FleetDocument: Codable {
    let title, message: String?
    let data: FleetDocumentData
}

// MARK: - DataClass
struct FleetDocumentData: Codable {
    let id: String?
    let fleetID: Int?
    let title: String?
    let imageLink: String?
    let fleetClassID, fleetTypeID: Int?
    let fleetClassName, fleetTypeName, vehicleMake, vehicleModel: String?
    let vehicleYear: String?
    let vehicleColor, vehicleRegistrationNumber: String?
    let documents: Documents

    enum CodingKeys: String, CodingKey {
        case id
        case fleetID = "fleet_id"
        case title
        case imageLink = "image_link"
        case fleetClassID = "fleet_class_id"
        case fleetTypeID = "fleet_type_id"
        case fleetClassName = "fleet_class_name"
        case fleetTypeName = "fleet_type_name"
        case vehicleMake = "vehicle_make"
        case vehicleModel = "vehicle_model"
        case vehicleYear = "vehicle_year"
        case vehicleColor = "vehicle_color"
        case vehicleRegistrationNumber = "vehicle_registration_number"
        case documents
    }
}

// MARK: - Documents
struct Documents: Codable {
    let insuranceNumber, expiryInsurance: String?
    let fileImage, fileVehicleInsurance: String?

    enum CodingKeys: String, CodingKey {
        case insuranceNumber = "insurance_number"
        case expiryInsurance = "expiry_insurance"
        case fileImage = "file_image"
        case fileVehicleInsurance = "file_vehicle_insurance"
    }
}
