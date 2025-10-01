//
//  OTPResponse.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/20/22.
//

import Foundation

//   let welcome = try? newJSONDecoder().decode(Welcome.self, from: jsonData)

import Foundation

struct OTPResponse: Codable {
    let title, message: String
    let data: OTPResponseData?
}

// MARK: - DataClass
struct OTPResponseData: Codable {
    let isProvisional: Int
    let identity, name, email, phone: String?
    let imageLink: String?
    let avgRating: Double?
    let apiToken: String?

    enum CodingKeys: String, CodingKey {
        case isProvisional = "is_provisional"
        case identity, name, email, phone
        case imageLink = "image_link"
        case avgRating = "avg_rating"
        case apiToken = "api_token"
    }
}
