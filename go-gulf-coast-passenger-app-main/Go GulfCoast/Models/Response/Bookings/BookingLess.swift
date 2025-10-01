//
//  BookingLess.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 27/09/2022.
//
import Foundation

struct BookingLess: Codable, Hashable {
    let title, message: String?
    let data: BookingLessData?
}

struct BookingLessData: Codable, Hashable {
    let bookingID, fromLocation: String?
    let fromLat, fromLng: Double?
    let toLocation: String?
    let toLat, toLng: Double?
    let distance, duration: String?
    let driverUser: DriverUser
    let message1, message2, message3: String?

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case fromLocation = "from_location"
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case toLocation = "to_location"
        case toLat = "to_lat"
        case toLng = "to_lng"
        case distance, duration
        case driverUser = "driver_user"
        case message1 = "message_1"
        case message2 = "message_2"
        case message3 = "message_3"
    }
}

// MARK: - DriverUser
struct DriverUser: Codable, Hashable {
    let name: String?
    let imageLink: String?

    enum CodingKeys: String, CodingKey {
        case name
        case imageLink = "image_link"
    }
}
