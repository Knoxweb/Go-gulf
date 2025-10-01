//
//  FirebaseJobResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 30/06/2022.
//

import Foundation

struct FirebaseJobResponse: Codable {
    var docId: String?
    var bookingId: String?
    var distance: String?
    var driverfare: Double?
    var driverId: String?
    var duration: String?
    var fromLat: Double?
    var fromLng: Double?
    var fromLocation: String?
    var passengerAvgRating: Int?
    var passengerImageLink: String?
    var passengerName: String?
    var pickupDatetime: String?
    var status: String?
    var toLat: Double?
    var toLng: Double?
    var toLocation: String?
    var createdAtUTC: String?
    var passenger: Int?
    var pet: Int?
    var wheelChair: Int?
    var flightNumber: String?
    var driverNote: String?
    
    enum CodingKeys: String, CodingKey {
        case docId
        case bookingId = "booking_id"
        case distance
        case driverfare = "driver_fare"
        case driverId = "driver_id"
        case duration
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case fromLocation = "from_location"
        case passengerAvgRating = "passenger_avg_rating"
        case passengerImageLink = "passenger_image_link"
        case passengerName = "passenger_name"
        case pickupDatetime = "pickup_datetime"
        case status
        case toLat = "to_lat"
        case toLng = "to_lng"
        case createdAtUTC = "created_at_format_utc"
        case passenger, pet
        case wheelChair = "wheel_chair"
        case flightNumber = "flight_number"
        case driverNote = "driver_note"
        
    }
}
