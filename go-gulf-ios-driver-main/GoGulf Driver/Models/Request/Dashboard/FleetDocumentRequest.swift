//
//  FleetDocumentRequest.swift
//  SlyykDriver
//
//  Created by Office on 18/12/2022.
//

import Foundation

struct FleetDocUpload: Codable {
    var id: String?
    var fleet_id: String?
    var vehicle_make: String?
    var vehicle_model: String?
    var vehicle_registration_number: String?
    var vehicle_color: String?
    var insurance_number: String?
    var expiry_insurance: String?
    var files: FleetDocUploadFiles
}
struct FleetDocUploadFiles: Codable {
    var file_vehicle_insurance: String?
    var file_image: String?
}
