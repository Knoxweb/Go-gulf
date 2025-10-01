//
//  ProfileResponse.swift
// SlyykDriver
//
//  Created by Office on 07/07/2022.
//

import Foundation

struct ProfileResponse: Codable {
    let title, message: String?
    let data: ProfileResponseData?
}
struct ProfileResponseData: Codable {
    let name: String?
    let email: String?
    let phone: String?
    let imageLink: String?
    let state: String?
    let card: ProfileResponseCard?
    enum CodingKeys: String, CodingKey {
        case name, email, phone
        case imageLink = "image_link"
        case state
        case card
    }
}
struct ProfileResponseCard: Codable {
    let cardMasked: String?
    let cardHolderName: String?
    
    enum CodingKeys: String, CodingKey {
        case cardMasked = "card_masked"
        case cardHolderName = "card_holder_name"
    }
}


struct NoticesModel: Codable {
    let title, message, type, ref, reference: String?
}


struct FBProfileData: Codable, Hashable {
    let email: String?
    let has_bank_account: Bool?
    let id: Int?
    let is_approved: Bool?
    let is_online: Bool?
    let mobile: String?
    let name: String?
    let profile_picture_url: String?
    let profile_status: String?
    let rating: String?
    let status: String?
}



struct CurrentBookingModel: Codable, Hashable {
    let current_status, description, distance: String?
    let passenger: PassengerModel?
    let drop, pickup: LocationData?
    let fare_breakdown: [FareBreakdown]?
    let duration, flight_number, fare, pickup_date_time, reference: String?
    let id, passenger_count, pet_count, wheelchair_count: Int?
    let fleet: FleetModel?
    let route: Route?
    let status: String?
    let type: String?
    let card_masked: String?
    let status_title: String?
}

struct FareBreakdown:Hashable,  Codable {
    let name: String?
    let ref: String?
    let value: String?
}

struct FleetModel: Codable, Hashable {
    let class_name, image_url, name, type_name, color, registration_number: String?
}

struct DriverModel: Codable, Hashable {
    let email, mobile, name, profile_picture_url: String?
    let rating: String?
}

struct CoordinatesModel: Codable, Hashable {
    let lat: String?
    let lng: String?
}

struct CoordinatesModelWithOTP: Codable, Hashable {
    let lat: String?
    let lng: String?
    let otp_code: String?
}
