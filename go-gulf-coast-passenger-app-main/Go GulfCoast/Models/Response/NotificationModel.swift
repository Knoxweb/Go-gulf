//
//  NotificationModel.swift
//  CTP Limo
//
//  Created by Office on 23/05/2023.
//

import Foundation


// MARK: - Welcome
struct NotificationModel: Codable, Hashable {
    let title, message: String?
    let data: [NotificationModelData]?
}

// MARK: - Datum
struct NotificationModelData: Codable, Hashable {
    let id: Int?
    let title, message: String
    let seen: Int?
    let notificationAt: String?

    enum CodingKeys: String, CodingKey {
        case id, title, message, seen
        case notificationAt = "notification_at"
    }
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

struct HelpModel: Codable {
    let data: [HelpData]?
    
}
struct HelpData: Codable {
    let country: String?
    let id: Int?
    let mobile: String?
    let status: Int?
}


struct LegalModel: Codable{
    let data: [LegalData]?
    let title: String?
}

struct LegalData: Codable {
    let heading: String?
    let list: [LegalList]?
}

struct LegalList: Codable {
    let content: String?
    let type: String?
}


struct NotificationCounts: Codable {
    let notification: Int?
}

