//
//  ScheduleResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 19/07/2022.
//

// MARK: - Welcome
import Foundation

// MARK: - Welcome
struct ScheduleResponse: Codable, Hashable {
    let title, message: String?
    let data: [ScheduleResponseData]
}

// MARK: - Datum
struct ScheduleResponseData: Codable, Hashable {
    let bookingID, fromLocation, toLocation, pickupDatetime: String?
    let totalFare: Double?
    let toCancel: Int?

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case fromLocation = "from_location"
        case toLocation = "to_location"
        case pickupDatetime = "pickup_datetime"
        case totalFare = "total_fare"
        case toCancel = "to_cancel"
    }
}


struct MyBookingsModel: Codable, Hashable{
    let bookingId, fromLocation, pickupDatetime, toLocation: String?
    let totalFare: Double?
}
