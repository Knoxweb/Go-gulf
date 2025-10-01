//
//  BookingListResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 13/07/2022.
//

import Foundation

// MARK: - Welcome
struct BookingListResponse: Codable, Hashable {
    let title, message: String?
    let data: [BookingListResponseData]?
}

// MARK: - Datum
struct BookingListResponseData: Codable, Hashable {
    let bookingID, fromLocation, toLocation, bookingType: String?
      let bookingStatus, pickupDatetime: String?
      let totalFare: Double?

      enum CodingKeys: String, CodingKey {
          case bookingID = "booking_id"
          case fromLocation = "from_location"
          case toLocation = "to_location"
          case bookingType = "booking_type"
          case bookingStatus = "booking_status"
          case pickupDatetime = "pickup_datetime"
          case totalFare = "total_fare"
      }
}
