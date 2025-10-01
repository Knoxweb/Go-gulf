//
//  CurrentRideResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 01/07/2022.
//

import Foundation

struct CurrentRideResponse: Codable {
    var title, message, mode: String?
    var data: CurrentRideResponseData?
}

struct CurrentRideResponseData: Codable {
    var quote: CurrentRideQuote?
    var driverJobAcceptedLocation: userCLLocation?
    var pickupLocation, dropoffLocation: userCLLocation?
    var viaLocation: viaCLLocation?
    var polylinePoints: PolylinePoints?
    var driverCurrentLocation: userCLLocation?
    var passenger: CurrentRidePassenger?
    var booking: CurrentRideBooking?
    var invoice: CurrentRideInvoice?
    var card: CurrentRideResponseCard?
    var duration: String?
    var distance: String?
    var remainingDuration, remainingDistance: String?
    
    enum CodingKeys: String, CodingKey {
        case quote
        case driverJobAcceptedLocation = "driver_job_accepted_location"
        case pickupLocation = "pickup_location"
        case dropoffLocation = "dropoff_location"
        case viaLocation = "via_location"
        case polylinePoints = "polyline_points"
        case driverCurrentLocation = "driver_current_location"
        case passenger, booking, invoice, card, duration, distance
        case remainingDuration = "remaining_duration"
        case remainingDistance = "remaining_distance"
    }
}

struct PolylinePoints: Codable {
    let bounds: Bounds?
    let points: String?
}

struct Bounds: Codable {
    let northeast, southwest: Northeast?
}


struct Northeast: Codable {
    let lat, lng: Double?
}

struct viaCLLocation: Codable {
    var lat, lng: String?
}

// MARK: - Location
struct userCLLocation: Codable {
    var lat, lng: Double?
}

struct CurrentRideInvoice: Codable {
    var invoiceId, createdAt: String?
    var fare: Double?
    var chargeDetails: CurrentRideChargeDetails?
    var subTotal: Double?
    var discountTitle, discountPercentage: String?
    var discountAmount: Int?
    var surchargeTitle, surchargePercentage: String?
    var surchargeAmount, finalAmount: Double?
    
    enum CodingKeys: String, CodingKey {
        case invoiceId = "invoice_id"
        case createdAt = "created_at"
        case fare = "fare"
        case chargeDetails = "charge_details"
        case subTotal = "sub_total"
        case discountTitle = "discount_title"
        case discountPercentage = "discount_percentage"
        case discountAmount = "discount_amount"
        case surchargeTitle = "surcharge_title"
        case surchargePercentage = "surcharge_percentage"
        case surchargeAmount = "surcharge_amount"
        case finalAmount = "final_amount"
    }
    
}

struct CurrentRideChargeDetails: Codable {
    var tolls: Int?
    var serviceFare: Int?
    enum CodingKeys: String, CodingKey {
        case tolls = "tolls"
        case serviceFare = "service_fee"
    }
}



struct CurrentRidePassenger: Codable {
    var name, phone: String?
    var imageLink: String?
    var avgRating: Double?

    enum CodingKeys: String, CodingKey {
        case name, phone
        case imageLink = "image_link"
        case avgRating = "avg_rating"
    }
}

struct CurrentRideResponseCard: Codable {
    var imageLink: String?
    var cardMask: String?

    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case cardMask = "card_mask"
    }
}

struct CurrentRideBooking: Codable {
    var bookingID, bookingType, fromLocation: String?
    var fromLat, fromLng: Double?
    var toLocation: String?
    var toLat, toLng: Double?
    var distance: String?
    var duration: String?
    var fare: Double?
    var pickupDatetime: String?
    var driverNote: String?
    var passenger, pet, wheelChair: Int?
    var flightNumber: String?
    var fleet: CurrentRideFleet?
    var cardNumber: String?
    var rideOtp: String?

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case bookingType = "booking_type"
        case fromLocation = "from_location"
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case toLocation = "to_location"
        case toLat = "to_lat"
        case toLng = "to_lng"
        case distance, duration
        case driverNote = "driver_note"
        case fare
        case pickupDatetime = "pickup_datetime"
        case passenger, pet
        case wheelChair = "wheel_chair"
        case flightNumber = "flight_number"
        case fleet
        case cardNumber = "card_number"
        case rideOtp = "ride_otp"
    }
}


struct CurrentRideQuote: Codable {
    var bookingID, bookingType, fromLocation: String?
    var fromLat, fromLng: Double?
    var toLocation: String?
    var toLat, toLng: Double?
    var distance: String?
    var duration: String?
    var estFare: Double?
    var passenger, luggage, infantSeat, childSeat: Int?
    var boosterSeat: Int?
    var pickupDatetime: String?
    var remainingTimeSEC: Int?
    var createdAtFormat: String?
    var fleet: CurrentRideFleet?
    var passengerUser: CurrentRidePassengerUser?
    var cardNumber: String?

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case bookingType = "booking_type"
        case fromLocation = "from_location"
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case toLocation = "to_location"
        case toLat = "to_lat"
        case toLng = "to_lng"
        case distance, duration
        case estFare = "est_fare"
        case passenger, luggage
        case infantSeat = "infant_seat"
        case childSeat = "child_seat"
        case boosterSeat = "booster_seat"
        case pickupDatetime = "pickup_datetime"
        case remainingTimeSEC = "remaining_time_sec"
        case createdAtFormat = "created_at_format"
        case fleet
        case passengerUser = "passenger_user"
        case cardNumber = "card_number"
    }
}

// MARK: - Fleet
struct CurrentRideFleet: Codable {
    var imageLink: String?
    var title, className, typeName: String?
    var vehicleMake, vehicleModel, vehicleRegistrationNumber: String?
    var passenger, pet, wheelChair: Int?

    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case title
        case className = "class_name"
        case typeName = "type_name"
        case vehicleMake = "vehicle_make"
        case vehicleModel = "vehicle_model"
        case vehicleRegistrationNumber = "vehicle_registration_number"
        case passenger, pet
        case wheelChair = "wheel_chair"
    }
}

// MARK: - PassengerUser
struct CurrentRidePassengerUser: Codable {
    var name: String?
    var imageLink: String?

    enum CodingKeys: String, CodingKey {
        case name
        case imageLink = "image_link"
    }
}

