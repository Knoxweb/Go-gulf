//
//  QuoteResponse.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/23/22.
//
import Foundation

struct QuoteResponse: Codable {
    let title, message: String
    let data: QuoteResponseData?
}

struct QuoteResponseData: Codable {
    let quoteID, fromLocation: String
    let fromLat, fromLng: Double
    let toLocation: String
    let toLat, toLng, distance: Double
    let duration: String
    let cardNumber: String
    let fleets: [QuoteResponseFleets]

    enum CodingKeys: String, CodingKey {
        case quoteID = "quote_id"
        case fromLocation = "from_location"
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case toLocation = "to_location"
        case toLat = "to_lat"
        case toLng = "to_lng"
        case distance, duration
        case cardNumber = "card_number"
        case fleets
    }
}

// MARK: - Fleet
struct QuoteResponseFleets: Codable {
    let fleet_id: Int
    let title, className, typeName: String
    let imageLink, image1_Link, iconLink: String
    let passenger, luggage, infantSeat, childSeat: Int
    let boosterSeat: Int
    let fare: String

    enum CodingKeys: String, CodingKey {
        case fleet_id, title
        case className = "class_name", typeName = "type_name", imageLink = "image_link"
        case image1_Link = "image_1_link"
        case iconLink = "icon_link"
        case passenger, luggage
        case infantSeat = "infant_seat"
        case childSeat = "child_seat"
        case boosterSeat = "booster_seat"
        case fare
    }
}

