//
//  QuoteRequest.swift
//   GoGulf
//
//  Created by Mac on 6/23/22.
//

import Foundation

struct QuoteRequest: Codable, Hashable {
    var drop_address_lat: String?
    var pickup_date: String?
    var pickup_address_lat: String?
    var drop_address_lng: String?
    var drop_address, pickup_address: String?
    var pickup_time: String?
    var trip_type, pickup_address_lng: String?
    var to_location: String?
    var exp_discount: Int?
    var passenger_count: Int?
}
