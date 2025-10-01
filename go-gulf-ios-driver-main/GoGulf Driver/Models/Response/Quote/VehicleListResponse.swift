//
//  VehicleListResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 29/06/2022.
//

import Foundation

// MARK: - Welcome
struct VehicleListResponse: Codable {
    let title, message: String
    let data: [VehicleListResponseData]?
}

// MARK: - Datum
struct VehicleListResponseData: Codable {
    let id: Int?
    let imageLink: String?
    let title, fleetClassName, fleetTypeName, vehicleMake: String?
    let vehicleModel: String?
    let vehicleYear: String?
    let vehicleColor, vehicleRegistrationNumber: String?
    let isApproved, status: Int?

    enum CodingKeys: String, CodingKey {
        case id
        case imageLink = "image_link"
        case title
        case fleetClassName = "fleet_class_name"
        case fleetTypeName = "fleet_type_name"
        case vehicleMake = "vehicle_make"
        case vehicleModel = "vehicle_model"
        case vehicleYear = "vehicle_year"
        case vehicleColor = "vehicle_color"
        case vehicleRegistrationNumber = "vehicle_registration_number"
        case isApproved = "is_approved"
        case status
    }
}
