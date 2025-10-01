//
//  DispatchModel.swift
//  SlyykDriver
//
//  Created by Office on 18/01/2023.
//

import Foundation

struct DispatchModel: Codable{
    let data: [DispatchJobModel]?
}

struct DispatchJobModel: Codable, Hashable{
    let distance, driver_uid, drop_address, duration: String?
    let expire_at, id: Int?
    let fare, pickup_address, pickup_date_time: String?
    let type: String?
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




struct PassengerModel: Codable, Hashable {
    let mobile, name, profile_picture_url: String?
    let rating: String?
}

struct TimerModel: Codable{
    var index: String? = ""
    var counter: Int? = 0
}

struct LocationData: Codable, Hashable {
    let lat: Double?
    let lng: Double?
    let name: String?
}



struct AvailableJobModel: Codable{
    let data: [AvailableJobModelData]?
}

struct AvailableJobModelData: Codable{
    let bookingId, fromLocation, toLocation: String?
    let flightNumber: String?
    let duration, distance: Double?
    let totalFare: Double?
    let pickupDatetime: String?
    let createdAtUTC: String?
    enum CodingKeys: String, CodingKey {
        case bookingId = "booking_id"
        case fromLocation = "from_location"
        case toLocation = "to_location"
        case flightNumber = "flight_number"
        case duration
        case distance
        case totalFare = "total_fare"
        case pickupDatetime = "pickup_datetime"
        case createdAtUTC = "created_at_utc"
    }
}
