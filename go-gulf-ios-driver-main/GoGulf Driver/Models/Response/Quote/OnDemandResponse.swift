//
//  CurrentRideResponse.swift
// SlyykDriverDriver
//
//  Created by Prabin Phasikawo on 6/26/22.
//

import Foundation

struct OnDemandResponse: Codable {
    let title: String?
    let message: String?
    let mode: String?
    let data: [OndemandResponseData]?
}

struct OndemandResponseData: Codable {
    let bookingId: String
    let driverFare: Double
    let fromLocation: String
    let toLocation: String
    let pickupDatetime: String
    enum CodingKeys: String, CodingKey {
        case bookingId = "booking_id"
        case driverFare = "driver_fare"
        case fromLocation = "from_location"
        case toLocation = "to_location"
        case pickupDatetime = "pickup_datetime"
    }
}
