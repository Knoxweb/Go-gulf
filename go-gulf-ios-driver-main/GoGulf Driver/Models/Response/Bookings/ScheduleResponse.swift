//
//  ScheduleResponse.swift
// SlyykDriver
//
//  Created by Office on 19/07/2022.
//
// This file was generated from JSON Schema using quicktype, do not modify it directly.
// To parse the JSON, add this file to your project and do:
//
//   let welcome = try? newJSONDecoder().decode(Welcome.self, from: jsonData)

import Foundation

// MARK: - Welcome
struct ScheduleResponse: Codable {
    let title, message: String?
    let data: [ScheduleResponseData]?
}

// MARK: - DataClass
struct ScheduleResponseData: Codable {
    let bookingID, fromLocation: String?
    let fromLat, fromLng: Double?
    let toLocation: String?
    let toLat, toLng, totalFare: Double?
    let pickupDatetime, bookingType: String?
    let remainingTimeSEC: Int?
    let createdAtFormat, createdAtFormatUTC, distance: String?
    let duration: String?
    let passengerUser: SchedulePassengerUser

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case fromLocation = "from_location"
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case toLocation = "to_location"
        case toLat = "to_lat"
        case toLng = "to_lng"
        case totalFare = "total_fare"
        case pickupDatetime = "pickup_datetime"
        case bookingType = "booking_type"
        case remainingTimeSEC = "remaining_time_sec"
        case createdAtFormat = "created_at_format"
        case createdAtFormatUTC = "created_at_format_utc"
        case distance, duration
        case passengerUser = "passenger_user"
    }
}

// MARK: - PassengerUser
struct SchedulePassengerUser: Codable {
    let name: String?
    let imageLink: String?
    let avgRating: Int?

    enum CodingKeys: String, CodingKey {
        case name
        case imageLink = "image_link"
        case avgRating = "avg_rating"
    }
}
