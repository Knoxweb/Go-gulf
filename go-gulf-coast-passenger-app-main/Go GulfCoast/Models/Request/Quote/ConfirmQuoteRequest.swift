//
//  ConfirmQuoteRequest.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//

import Foundation

struct ConfirmQuoteRequest: Codable, Hashable {
    var description: String?
    var flight_number: String?
    var lead_passenger_phone: String?
    var passenger_count, pet_count, wheelchair_count, passenger_card_id: String?
}


struct QuoteResponseData: Codable, Hashable {
    let distance: String?
    let drop: LocationData?
    let duration: String?
    let expire_at: Int?
    let fare: String?
    let id: Int?
    let message: String?
    let pickup: LocationData?
    let pickup_date_time: String?
    let reference: String?
    let route: Route?
    let status: String?
    let title: String?
}

struct LocationData: Codable, Hashable {
    let lat: Double?
    let lng: Double?
    let name: String?
}



// MARK: - Welcome
struct QuoteConfirmResponse: Codable {
    let title, message: String?
    let data: QuoteConfirmResponseData?
}

// MARK: - DataClass
struct QuoteConfirmResponseData: Codable {
    let id: Int?
    let reference: String?
    let pickup, drop: Drop?
    let route: Route?
    let distance, duration, pickupDateTime, fare: String?
    let status, type, flightNumber, description: String?
    let passengerCount, petCount, wheelchairCount: Int?
    let fleet: Fleet?
    let expireAt: Int?
    let title, message: String?

    enum CodingKeys: String, CodingKey {
        case id, reference, pickup, drop, route, distance, duration
        case pickupDateTime = "pickup_date_time"
        case fare, status, type
        case flightNumber = "flight_number"
        case description
        case passengerCount = "passenger_count"
        case petCount = "pet_count"
        case wheelchairCount = "wheelchair_count"
        case fleet
        case expireAt = "expire_at"
        case title, message
    }
}

// MARK: - Drop
struct Drop: Codable {
    let name: String
    let lat, lng: Double
}

struct DiscountCodeCheckModel:Codable, Hashable {
    let quote_id: String?
    let fleet_id: String?
    let discount_code: String?
}

struct DiscountCodeResponse:Codable, Hashable {
    let title: String?
    let message: String?
    let data: DiscountCodeResponseData?
}

struct DiscountCodeResponseData:Codable, Hashable {
    let discount_amount: Double?
}

