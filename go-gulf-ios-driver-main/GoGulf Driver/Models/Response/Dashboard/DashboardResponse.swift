//
//  DashboardResponse.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/19/22.
//

import Foundation

// MARK: - DashboardResponse
struct DashboardResponse: Codable {
    let title, message: String
    let data: DashboardResponseData?
}

// MARK: - DataClass
struct DashboardResponseData: Codable {
    let user: User
    let completedJob: Int
    let nextJob: String
    let isOnline: Int
    let myPickup, availablePickup: Int
    let bankValidation, processValidation: Validation
    let docValidation: [DocValidation]
    let activeVehicle: DashboardActiveVehicle
    let isCurrentRide: Int

    enum CodingKeys: String, CodingKey {
        case user
        case completedJob = "completed_job"
        case nextJob = "next_job"
        case isOnline = "is_online"
        case myPickup = "my_pickup"
        case availablePickup = "available_pickup"
        case bankValidation = "bank_validation"
        case processValidation = "process_validation"
        case docValidation = "doc_validation"
        case activeVehicle = "active_vehicle"
        case isCurrentRide = "is_current_ride"
    }
}

// MARK: - DocValidation
struct DocValidation: Codable {
    let title, message: String
}

// MARK: - ActiveVehicle
struct DashboardActiveVehicle: Codable {
    let make, model, registrationNumber: String
    let imageLink: String

    enum CodingKeys: String, CodingKey {
        case make, model
        case registrationNumber = "registration_number"
        case imageLink = "image_link"
    }
}

// MARK: - Validation
struct Validation: Codable {
    let status: Int
    let title, message: String
}

// MARK: - User
struct User: Codable {
    let name, email, phone: String
    let imageLink: String

    enum CodingKeys: String, CodingKey {
        case name, email, phone
        case imageLink = "image_link"
    }
}

struct FCMTokenResponse: Codable {
    let title, message: String
}


// MARK: - Welcome
struct invoiceDownloadResponse: Codable {
    let title, message: String?
    let data: invoiceDownloadResponseData
}

// MARK: - DataClass
struct invoiceDownloadResponseData: Codable {
    let link: String?
    let name: String?
}
