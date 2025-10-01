//
//  ScheduleJobResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 26/07/2022.
//

import Foundation

// MARK: - Welcome
struct ScheduleJobResponse: Codable {
    let title, message: String?
    let data: ScheduleJobResponseData?
}

// MARK: - DataClass
struct ScheduleJobResponseData: Codable {
    let bookingId, fromLocation: String?
    let fromLat, fromLng: Double?
    let toLocation: String?
    let toLat, toLng, totalFare: Double?
    let pickupDatetime, bookingType: String?
    let remainingTimeSEC: Int?
    let createdAtFormat, createdAtFormatUTC, distance: String?
    let duration: String?
    let passengerUser: PassengerUser?

    enum CodingKeys: String, CodingKey {
        case bookingId = "booking_id"
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
struct PassengerUser: Codable {
    let name: String?
    let imageLink: String?
    let avgRating: Int?

    enum CodingKeys: String, CodingKey {
        case name
        case imageLink = "image_link"
        case avgRating = "avg_rating"
    }
}
