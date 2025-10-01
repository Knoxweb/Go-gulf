//
//  CurrentRideResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 29/06/2022.
//

import Foundation

struct DiscountPromo: Codable {
    let discount: String?
    let discount_status, discount_title, discount_message, promotional_message, promotional_status, promotional_title: String
}


struct CurrentBookingModel: Codable, Hashable {
    let current_status, description, distance: String?
    let driver: DriverModel?
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


struct OTPCodeModel: Codable {
    let message, title: String?
    let otp: Int?
}
