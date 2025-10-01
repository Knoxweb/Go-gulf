//
//  NotificationModel.swift
//  CTP Limo
//
//  Created by Office on 23/05/2023.
//

import Foundation


// MARK: - Welcome
struct NotificationModel: Codable {
    let title, message: String?
    let data: [NotificationModelData]?
}

// MARK: - Datum
struct NotificationModelData: Codable {
    let id: Int?
    let title, message: String
    let seen: Int?
    let notificationAt: String?

    enum CodingKeys: String, CodingKey {
        case id, title, message, seen
        case notificationAt = "notification_at"
    }
}



struct ReadNotificationModel: Codable{
    let id: Int
}

//struct NotificationListModel: Codable {
//    let bookingId: String?
//    let clientSecret: String?
//    let invoiceId: String?
//    let message: String?
//    let notificationAt: String?
//    let notificationId: Int?
//    let timestamp: Int?
//    let seen: Bool?
//    let target: String?
//    let title: String?
//
//    enum CodingKeys: String, CodingKey {
//        case bookingId = "booking_id"
//        case clientSecret = "client_secret"
//        case invoiceId = "invoice_id"
//        case message
//        case notificationAt = "notification_at"
//        case notificationId = "notification_id"
//        case timestamp
//        case seen
//        case target
//        case title
//    }
//}


struct NotificationCounts: Codable {
    let notification: Int?
}


struct NotificationListModel: Codable, Hashable {
    let id: String?
    let is_seen: Bool?
    let message: String?
    let target: String?
    let timestamp: Int?
    let title: String?
    let type: String?
}
