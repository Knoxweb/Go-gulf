//
//  EndTripResponse.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 14/07/2022.
//

import Foundation

struct EndTripResponse: Codable {
    let title, message: String?
    let data: EndTripResponseData?
}
struct EndTripResponseData: Codable {
    let paymentStatus: Int
    enum CodingKeys: String, CodingKey {
        case paymentStatus = "payment_status"
    }
}
