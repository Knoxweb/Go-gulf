//
//  AvailablePickupResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 11/10/2022.
//


import Foundation

// MARK: - Welcome
struct AvailablePickResponse: Codable {
    let title, message: String?
    let data: [AvailablePickResponseData]
}

// MARK: - Datum
struct AvailablePickResponseData: Codable {
    let bookingID, fromLocation, toLocation: String?
       let totalFare: Double?
       let pickupDatetime, expiredAt: String?

       enum CodingKeys: String, CodingKey {
           case bookingID = "booking_id"
           case fromLocation = "from_location"
           case toLocation = "to_location"
           case totalFare = "total_fare"
           case pickupDatetime = "pickup_datetime"
           case expiredAt = "expired_at"
       }
}
