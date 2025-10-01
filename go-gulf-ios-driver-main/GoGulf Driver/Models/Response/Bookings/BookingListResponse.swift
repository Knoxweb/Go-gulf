//
//  BookingListResponse.swift
// SlyykDriver
//
//  Created by Office on 13/07/2022.
//
// This file was generated from JSON Schema using quicktype, do not modify it directly.
// To parse the JSON, add this file to your project and do:
//
//   let welcome = try? newJSONDecoder().decode(Welcome.self, from: jsonData)

import Foundation

// MARK: - Welcome
struct BookingListResponse: Codable {
    let title, message: String?
    let data: [BookingListResponseData]
}

// MARK: - Datum
struct BookingListResponseData: Codable {
    let bookingID, fromLocation, toLocation, bookingType: String?
       let bookingStatus, pickupDatetime: String?
       let driverFare: Double?

       enum CodingKeys: String, CodingKey {
           case bookingID = "booking_id"
           case fromLocation = "from_location"
           case toLocation = "to_location"
           case bookingType = "booking_type"
           case bookingStatus = "booking_status"
           case pickupDatetime = "pickup_datetime"
           case driverFare = "driver_fare"
       }
}
