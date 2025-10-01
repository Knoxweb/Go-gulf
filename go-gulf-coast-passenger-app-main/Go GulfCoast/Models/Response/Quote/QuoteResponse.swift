//
//  QuoteResponse.swift
//   GoGulf
//
//  Created by Mac on 6/23/22.
//

import Foundation
import SwiftUI
import CoreLocation

// MARK: - Welcome
struct QuoteResponse: Codable, Hashable {
    let quote: QuoteData?
    let fleets: [FleetList]?
}

// MARK: - Fleet
struct FleetList: Codable, Hashable {
    let id: Int?
    let fullName, className, typeName: String?
    let imagePath: String?
    let smallImagePath: String?
    let passengers, pet, wheelchair: Int?
    let fare, offerFare: String?

    enum CodingKeys: String, CodingKey {
        case id
        case fullName = "full_name"
        case className = "class_name"
        case typeName = "type_name"
        case imagePath = "image_path"
        case smallImagePath = "small_image_path"
        case passengers, pet, wheelchair, fare
        case offerFare = "offer_fare"
    }
}

// MARK: - Quote
struct QuoteData: Codable, Hashable {
    let id: Int?
    let tripType, description: String?
    let pickupAddress, dropAddress: PAddress?
    let pickupDateTimestamp: Int?
    let pickupDate, pickupTime, duration, distance: String?
    let cardNumber, cardId, pickupType: String?
    let route: Route?

    enum CodingKeys: String, CodingKey {
        case id
        case tripType = "trip_type"
        case description
        case pickupAddress = "pickup_address"
        case dropAddress = "drop_address"
        case pickupDateTimestamp = "pickup_date_timestamp"
        case pickupDate = "pickup_date"
        case pickupTime = "pickup_time"
        case duration, distance
        case cardNumber = "card_number"
        case cardId = "card_id"
        case pickupType = "pickup_type"
        case route
    }
}

// MARK: - PAddress
struct PAddress: Codable, Hashable {
    let name: String?
    let lat, lng: Double?
}

// MARK: - Route
struct Route: Codable, Hashable {
    let bounds: BoundsData?
    let overviewPolyline: OverviewPolyline?

    enum CodingKeys: String, CodingKey {
        case bounds
        case overviewPolyline = "overview_polyline"
    }
}

// MARK: - Bounds
struct BoundsData: Codable, Hashable {
    let northeast, southwest: NortheastBound?
}

// MARK: - Northeast
struct NortheastBound: Codable, Hashable {
    let lat, lng: Double?
}

// MARK: - OverviewPolyline
struct OverviewPolyline: Codable, Hashable {
    let points: String?
}





// MARK: - Welcome
struct PredictionsData:Hashable,  Codable {
    let predictions: [Prediction]?
    let status: String?
}

// MARK: - Prediction
struct Prediction:Hashable,  Codable {
    let structuredFormatting: StructuredFormatting?
    let placeID: String?
    enum CodingKeys: String, CodingKey {
        case structuredFormatting = "structured_formatting"
        case placeID = "place_id"
    }
}

// MARK: - StructuredFormatting
struct StructuredFormatting:Hashable,  Codable {
    let mainText: String?
    let secondaryText: String?

    enum CodingKeys: String, CodingKey {
        case mainText = "main_text"
        case secondaryText = "secondary_text"
    }
}





struct GeoLocationData: Codable {
    let coordinate: CoordinatesData?
    let zipcode: String?
}
struct CoordinatesData: Codable {
    let lat: Double?
    let lng: Double?
}
