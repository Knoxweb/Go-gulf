//
//  RequestingRideResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 29/06/2022.
//


import Foundation

struct RequestingRideReponse: Codable, Hashable {
    var title, message, mode: String?
    let data: RequestingRideReponseData?
}

struct RequestingRideReponseData: Codable, Hashable {
    let quote: RequestingRideReponseQuote
}

struct RequestingRideReponseQuote: Codable, Hashable {
    let bookingID, bookingType, fromLocation: String
    let fromLat, fromLng: Double
    let toLocation: String
    let toLat, toLng, distance: Double
    let duration: String
    let estFare: Double
    let passenger, luggage, infantSeat, childSeat: Int
    let boosterSeat: Int
    let pickupDatetime: String
    let remainingTimeSEC: Int
    let createdAtFormat: String
    let fleet: RequestingRideReponseFleet
    let passengerUser: RequestingRideReponseUser
    let cardNumber: String

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case bookingType = "booking_type"
        case fromLocation = "from_location"
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case toLocation = "to_location"
        case toLat = "to_lat"
        case toLng = "to_lng"
        case distance, duration
        case estFare = "est_fare"
        case passenger, luggage
        case infantSeat = "infant_seat"
        case childSeat = "child_seat"
        case boosterSeat = "booster_seat"
        case pickupDatetime = "pickup_datetime"
        case remainingTimeSEC = "remaining_time_sec"
        case createdAtFormat = "created_at_format"
        case fleet
        case passengerUser = "passenger_user"
        case cardNumber = "card_number"
    }
}

// MARK: - Fleet
struct RequestingRideReponseFleet: Codable, Hashable {
    let imageLink: String
    let title, className, typeName: String
    let passenger, luggage, infantSeat, childSeat: Int
    let boosterSeat: Int

    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case title
        case className = "class_name"
        case typeName = "type_name"
        case passenger, luggage
        case infantSeat = "infant_seat"
        case childSeat = "child_seat"
        case boosterSeat = "booster_seat"
    }
}

// MARK: - PassengerUser
struct RequestingRideReponseUser: Codable, Hashable {
    let name: String
    let imageLink: String

    enum CodingKeys: String, CodingKey {
        case name
        case imageLink = "image_link"
    }
}

