//
//  QuoteConfirmResponse.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/25/22.
//

import Foundation

struct QuoteConfirmResponse: Codable {
    let title, message: String
    let data: QuoteConfirmResponseData?
}

struct QuoteConfirmResponseData: Codable {
    let quoteID, fromLocation: String
    let fromLat, fromLng: Double
    let toLocation: String
    let toLat, toLng, distance: Double
    let duration: String

    enum CodingKeys: String, CodingKey {
        case quoteID = "quote_id"
        case fromLocation = "from_location"
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case toLocation = "to_location"
        case toLat = "to_lat"
        case toLng = "to_lng"
        case distance, duration
    }
}
