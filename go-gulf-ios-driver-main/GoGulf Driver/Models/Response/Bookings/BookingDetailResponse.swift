
import Foundation

// MARK: - Welcome
struct BookingDetailResponse: Codable {
    let title, message: String?
    let data: BookingDetailResponseData?
}

// MARK: - DataClass
struct BookingDetailResponseData: Codable {
    let bookingID, bookingType, fromLocation, toLocation: String?
    let status, label, pickupDatetimeStd, pickupDatetime: String?
    let fleet: Fleet
    let name, phoneCc, phoneNo, email: String?
    let leadPassengerName, leadPassengerPhone, flightNumber: String?
    let driverNote: String?
    let passenger, pet, wheelChair: Int?
    let distance, duration: String?
    let invoice: Invoice
    let driverTransaction: DriverTransaction
    let passengerUser, driverUser: ErUser?
    let card: Card

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case bookingType = "booking_type"
        case fromLocation = "from_location"
        case toLocation = "to_location"
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
        case distance, duration, invoice
        case driverTransaction = "driver_transaction"
        case passengerUser = "passenger_user"
        case driverUser = "driver_user"
        case card
    }
}

// MARK: - Card
struct Card: Codable {
    let imageLink: String?
    let cardMask: String?

    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case cardMask = "card_mask"
    }
}

// MARK: - DriverTransaction
struct DriverTransaction: Codable {
    let transactionID: String?
    let amount: Double?

    enum CodingKeys: String, CodingKey {
        case transactionID = "transaction_id"
        case amount
    }
}

// MARK: - ErUser
struct ErUser: Codable {
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
struct Fleet: Codable {
    let imageLink: String?
    let title, className, typeName, vehicleMake: String?
    let vehicleModel, vehicleRegistrationNumber: String?
    let passenger, luggage, infantSeat, childSeat: Int?
    let boosterSeat: Int?

    enum CodingKeys: String, CodingKey {
        case imageLink = "image_link"
        case title
        case className = "class_name"
        case typeName = "type_name"
        case vehicleMake = "vehicle_make"
        case vehicleModel = "vehicle_model"
        case vehicleRegistrationNumber = "vehicle_registration_number"
        case passenger, luggage
        case infantSeat = "infant_seat"
        case childSeat = "child_seat"
        case boosterSeat = "booster_seat"
    }
}

// MARK: - Invoice
struct Invoice: Codable {
    let invoiceID, createdAt: String?
    let fare: Double?
    let  finalAmount: Double?
    let isPaid: Int?

    enum CodingKeys: String, CodingKey {
        case invoiceID = "invoice_id"
        case createdAt = "created_at"
        case fare
        case finalAmount = "final_amount"
        case isPaid = "is_paid"
    }
}

// MARK: - ChargeDetail
struct ChargeDetail: Codable {
    let key: String?
    let value: Double?
}
