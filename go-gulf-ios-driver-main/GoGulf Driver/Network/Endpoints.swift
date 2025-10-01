//
//  Endpoints.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 10/09/2024.
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
    static let addFleet = "fleets"
    static let updateLang = "language-update"
    static let update = "update"
    static let document = "documents"
    static let leaveCompany = "leave-company"
    static let bank = "bank"
    static let multiplePDFDownload = "invoice-pdf"
    static let resetPassword = "reset-password"
    static let online = "online"
    
    static let addChauffeur = "add-chauffeur"
    static let removeChauffeur =  "remove-chauffeur"
    
    static func fleetDocument(_ id: String) -> String {
        return "fleet-documents/\(id)"
    }
    
    static func profileUpdate(_ endpoint: String) -> String {
        return "profile-update/\(endpoint)"
    }
    
    static func fleetStatus(_ endpoint: String) -> String {
        return "fleet-status/\(endpoint)"
    }
    
    static func downloadPDF(_ id: String) -> String {
        return "invoice-pdf/\(id)"
    }
    
    static func statementPdf(_ id: String) -> String {
        return "statement-pdf/\(id)"
    }
    
    
    static func chat(_ endpoint: String) -> String {
        return "job-chat-passenger/\(endpoint)"
    }
    
    static let forgotPassword = "forget-password"
    static let forgotPasswordReset = "forget-password-reset"
    static let otpCheck = "forget-password-opt-check"
    static let changeAvailability = "change-availability"
    static let logOut = "logout"
    static let deleteAccount = "delete-account"
    static let acceptRejectInvitation = "accept-reject-chauffeur"

    static let udpatePosition = "update-location"
    
    static func fleetDocument(_ id: Int) -> String {
        return "fleet-documents/\(id)"
    }
    static func acceptJob(_ id: Int) -> String {
        return "job-accept/\(id)"
    }
    static func markAsSeen(_ id: String) -> String {
        return "notification-read/\(id)"
    }
    static func startJob(_ id: Int) -> String {
        return "job-start/\(id)"
    }
    static func cancelJob(_ id: Int) -> String {
        return "job-cancel/\(id)"
    }
    
    static func noShow(_ id: Int) -> String {
        return "job-no-show-up/\(id)"
    }
    
    static func jobReject(_ id: Int) -> String {
        return "job-reject/\(id)"
    }
    
    static func assignJob(_ id: Int) -> String {
        return "job-change-driver/\(id)"
    }
    
    static func DOD(_ id: Int) -> String {
        return "job-dod/\(id)"
    }
    
    static func POB(_ id: Int) -> String {
        return "job-pob/\(id)"
    }
    
    static func EndTrip(_ id: Int) -> String {
        return "job-completed/\(id)"
    }
    
    
    static func skipRating(_ id: String) -> String {
        return "job-skip-review/\(id)"
    }
    
    static func jobReview(_ id: String) -> String {
        return "job-review/\(id)"
    }
    
    static func callPassenger(_ id: String) -> String {
        return "job-call-passenger/\(id)"
    }
    
    static func confirmBooking(_ id: String) -> String {
        return "confirm-booking/\(id)"
    }
    static let addCard = "add-card"
    static let fleet = "fleet"
    static let support = "support"
    static let profileUpdate = "update-profile"
    static let quoteRequest = "quote-request"
    static let confirmRequest = "confirm-booking"
    static let dasboardTask = "dashboard-task"

    static func cancelBookingRequest(_ id: String) -> String {
        return "cancel-booking-request/\(id)"
    }
    static func getCancelInfo(_ id: String) -> String {
        return "cancel-info/\(id)"
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
