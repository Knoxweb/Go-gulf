//
//  BookingDetailResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 14/07/2022.
//
import Foundation

// MARK: - Welcome
struct BookingDetailResponse: Codable, Hashable {
    let title, message: String?
    let data: BookingDetailResponseData?
}

// MARK: - DataClass
struct BookingDetailResponseData: Codable, Hashable {
    let bookingID, bookingType, fromLocation: String?
    let fromLat, fromLng: Double?
    let toLocation: String?
    let toLat, toLng: Double?
    let status, label, pickupDatetimeStd, pickupDatetime: String?
    let fleet: Fleet
    let name, phoneCc, phoneNo, email: String?
    let leadPassengerName, leadPassengerPhone, flightNumber: String?
    let driverNote: String?
    let passenger, pet, wheelChair: Int?
    let distance, duration: String?
    let toCancel: Int?
    let invoice: Invoice
    let passengerUser, driverUser: ErUser?
    let card: Card

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case bookingType = "booking_type"
        case fromLocation = "from_location"
        case fromLat = "from_lat"
        case fromLng = "from_lng"
        case toLocation = "to_location"
        case toLat = "to_lat"
        case toLng = "to_lng"
        case status, label
        case pickupDatetimeStd = "pickup_datetime_std"
        case pickupDatetime = "pickup_datetime"
        case fleet, name
        case phoneCc = "phone_cc"
        case phoneNo = "phone_no"
        case email
        case leadPassengerName = "lead_passenger_name"
        case leadPassengerPhone = "lead_passenger_phone"
        case flightNumber = "flight_number"
        case driverNote = "driver_note"
        case passenger, pet
        case wheelChair = "wheel_chair"
        case distance, duration
        case toCancel = "to_cancel"
        case invoice
        case passengerUser = "passenger_user"
        case driverUser = "driver_user"
        case card
    }
}

// MARK: - Card
struct Card: Codable, Hashable {
    let imageLink: String?
    let cardMask: String?

    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case cardMask = "card_mask"
    }
}

// MARK: - ErUser
struct ErUser: Codable, Hashable {
    let name, email, phone: String?
    let imageLink: String?
    let avgRating: Double?

    enum CodingKeys: String, CodingKey {
        case name, email, phone
        case imageLink = "image_link"
        case avgRating = "avg_rating"
    }
}

// MARK: - Fleet
struct Fleet: Codable, Hashable {
    let imageLink: String?
    let title, className, typeName, vehicleMake: String?
    let vehicleModel, vehicleRegistrationNumber: String?
    let passenger, pet, wheelChair: Int?

    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case title
        case className = "class_name"
        case typeName = "type_name"
        case vehicleMake = "vehicle_make"
        case vehicleModel = "vehicle_model"
        case vehicleRegistrationNumber = "vehicle_registration_number"
        case passenger
        case pet = "pet"
        case wheelChair = "wheel_chair"
    }
}

// MARK: - Invoice
struct Invoice: Codable, Hashable {
    let invoiceID, createdAt: String?
    let fare: Double?
    let surchargeTitle, surchargePercentage: String?
    let surchargeAmount: Double?
    let gstTitle, gstPercentage: String?
    let gstAmount: Double?
    let finalAmount: Double?
    let isPaid: Int?

    enum CodingKeys: String, CodingKey {
        case invoiceID = "invoice_id"
        case createdAt = "created_at"
        case fare
        case surchargeTitle = "surcharge_title"
        case surchargePercentage = "surcharge_percentage"
        case surchargeAmount = "surcharge_amount"
        case gstTitle = "gst_title"
        case gstPercentage = "gst_percentage"
        case gstAmount = "gst_amount"
        case finalAmount = "final_amount"
        case isPaid = "is_paid"
    }
}

// MARK: - ChargeDetail
struct ChargeDetail: Codable, Hashable {
    let key: String?
    let value: Double?
}
