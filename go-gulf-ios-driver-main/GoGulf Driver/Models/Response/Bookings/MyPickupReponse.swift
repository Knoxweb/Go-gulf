//
//  MyPickupReponse.swift
// SlyykDriverDriver
//
//  Created by Office on 11/10/2022.
//

import Foundation

// MARK: - Welcome
struct MyPickupResponse: Codable {
    let title, message: String
    let data: [MyPickupResponseData]
}

// MARK: - Datum
struct MyPickupResponseData: Codable {
    let bookingID, fromLocation, toLocation, pickupDatetime: String
    let totalFare: Int

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case fromLocation = "from_location"
        case toLocation = "to_location"
        case pickupDatetime = "pickup_datetime"
        case totalFare = "total_fare"
    }
}
