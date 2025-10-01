//
//  FBCurrentResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 07/07/2022.
//

import Foundation

struct FirebaseCurrentResponse: Codable, Hashable {
    let title, message, mode: String?
    let data: FirebaseCurrentResponseData?
}
struct FirebaseCurrentResponseData: Codable, Hashable {
    let booking: FBCurrentBooking?
    let driver: FBCurrentDriver?
}
struct FBCurrentDriver: Codable, Hashable {
    let imageLink: String
    let name: String
    let phone: String
    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case name
        case phone
    }
}
struct FBCurrentBooking: Codable, Hashable {
    let fleet: FCCurrentFleet
    let bookingId: String
    let bookingType: String
    let fromLocation: String
    let toLocation: String
    let distance: String
    let duration: String
    let fare: Double
    enum CodingKeys: String, CodingKey {
        case fleet
        case bookingId = "booking_id"
        case bookingType = "booking_type"
        case fromLocation = "from_location"
        case toLocation = "to_location"
        case distance, duration, fare
    }
}
struct FCCurrentFleet: Codable, Hashable {
    let imageLink: String
    let title: String
    let typeName: String
    let className: String
    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case title
        case typeName = "type_name"
        case className = "class_name"
    }
}


