//
//  Endpoints.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 04/09/2024.
//

import Foundation
struct APIEndpoints {
    static let login = "login"
    static let status = "status"
    static let firstUpdate = "first-update"
    static let socialFirstUpdate = "first-social-update"
    static let emailLogin = "email-login"
    static let socialLogin = "social-login"
    static let register = "register"
    static let update = "update"
    static let forgotPassword = "forget-password"
    static let forgotPasswordReset = "forget-password-reset"
    static let otpCheck = "forget-password-opt-check"
    static let calculateFare = "calculate-fare"
    static let logOut = "logout"
    static let deleteAccount = "delete-account"
    static let updateLang = "language-update"
    static let resetPassword = "reset-password"
    static let addAddress = "add-address"
    
    
    static let promoCode = "booking-promo-code"
    
    
    static func editAddress(_ id: String) -> String {
        return "edit-address/\(id)"
    }
    
    static func skipRating(_ id: String) -> String {
        return "booking-skip-review/\(id)"
    }
    
    
    static func retryBooking(_ id: String) -> String {
        return "retry-booking/\(id)"
    }
    
    static func cancelBookingRequest(_ id: String) -> String {
        return "cancel-booking-request/\(id)"
    }
    
    
    
    static func confirmQuote(_ id: String) -> String {
        return "confirm-booking/\(id)"
    }
    
    
    static func deleteAddress(_ id: String) -> String {
        return "delete-address/\(id)"
    }
    
    static func confirmBooking(_ id: String) -> String {
        return "confirm-booking/\(id)"
    }
    static func chat(_ endpoint: String) -> String {
        return "booking-chat-driver/\(endpoint)"
    }
    
    static func markAsSeen(_ id: String) -> String {
        return "notification-read/\(id)"
    }
    
    static func profileUpdate(_ endpoint: String) -> String {
        return "profile-update/\(endpoint)"
    }
    static func cancelInfo(_ id: String) -> String {
        return "cancel-info/\(id)"
    }
    static func cancelBooking(_ id: Int) -> String {
        return "cancel-booking/\(id)"
    }
    static func updateInstruction(_ id: String) -> String {
        return "booking-special-instruction/\(id)"
    }
    static func updateFlight(_ id: String) -> String {
        return "booking-flight-number/\(id)"
    }
    static func bookingTip(_ id: String) -> String {
        return "booking-tip/\(id)"
    }
    static func bookingReview(_ id: String) -> String {
        return "booking-review/\(id)"
    }
    static func invoiceStatus(_ id: String) -> String {
        return "invoice-status/\(id)"
    }
    static func retryInvoice(_ id: String) -> String {
        return "invoice-retry/\(id)"
    }
    
    static func bookingSkipTip(_ id: String) -> String {
        return "booking-skip-tip/\(id)"
    }
    static func checkedTime(_ id: String) -> String {
        return "booking-checkup-time/\(id)"
    }
    
    static func deletePassenger(_ id: String) -> String {
        return "delete-lead-passenger/\(id)"
    }
    static func editPassenger(_ id: String) -> String {
        return "edit-lead-passenger/\(id)"
    }
    static let addCard = "add-card"
    static let addPassenger = "add-lead-passenger"
    static let fleet = "fleet"
    static let support = "support"
    static let profileUpdate = "update-profile"
    static let quoteRequest = "quote-request"
    static let confirmRequest = "confirm-booking"
    static let dasboardTask = "dashboard-task"
    static let updateDriver = "drivers"
    static let multiplePDFDownload = "invoice-pdf"
    static let sortDriver = "driver-sort-orders"
    static func cardUpdate(_ id: String) -> String {
        return "update-card/\(id)"
    }
    
    static func activateCard(_ id: String) -> String {
        return "activate-card/\(id)"
    }

    static func deleteCard(_ id: String) -> String {
        return "delete-card/\(id)"
    }
    static func downloadPDF(_ id: String) -> String {
        return "invoice-pdf/\(id)"
    }
    
    static func getCancelInfo(_ id: String) -> String {
        return "cancel-info/\(id)"
    }
    
    static func callDriver(_ id: String) -> String {
        return "booking-call-driver/\(id)"
    }
    
}

struct Profile {
    static let fName = "first_name"
    static let lName = "last_name"
    static let email = "email"
    static let billingFName = "billing_first_name"
    static let billingLName = "billing_last_name"
    static let billingCompanyName = "billing_company_name"
    static let billingAddress = "billing_address"
    static let billingCountry = "billing_country"
    static let pickupSign = "pickup_sign"
    static let profileImage = "profile_image"
    static let mobile = "mobile"
}


struct FBEndpoint {
    static let passengerLegal = "passenger_legal_notice"
    static let passengerTerms = "passenger_term_of_use"
    static let passengerPolicy = "passenger_privacy_policy"
}
