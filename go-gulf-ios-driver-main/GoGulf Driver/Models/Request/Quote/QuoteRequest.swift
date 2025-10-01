//
//  QuoteRequest.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/23/22.
//

import Foundation
struct QuoteRequest: Codable {
    var booking_type: String?
    var pickup_datetime: String?
    var from_location: String
    var from_lat, from_lng: Double
    var via_location: String?
    var via_lat, via_lng: Double?
    var to_location: String
    var to_lat, to_lng: Double
}
