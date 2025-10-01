//
//  EarningResponse.swift
//  AmheerDriver
//
//  Created by Office on 26/12/2022.
//

import Foundation

// MARK: - EarningResponse
struct EarningResponse: Codable {
    let title, message: String?
    let data: EarningResponseData?
}

// MARK: - DataClass
struct EarningResponseData: Codable {
    let details: EarningResponseDataDetails?
    let earnings: [EarningData]?
}

// MARK: - Details
struct EarningResponseDataDetails: Codable {
    let amount: Double?
    let from, to: String?
}

// MARK: - Earning
struct EarningData: Codable {
    let bookingID, createdAt: String?
    let amount: Double?
    let status: String?

    enum CodingKeys: String, CodingKey {
        case bookingID = "booking_id"
        case createdAt = "created_at"
        case amount, status
    }
}

