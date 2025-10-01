//
//  EndTripResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 14/07/2022.
//

import Foundation

struct EndTripResponse: Codable, Hashable {
    let title, message: String?
    let data: EndTripResponseData?
}
struct EndTripResponseData: Codable, Hashable {
    let paymentStatus: Int
    enum CodingKeys: String, CodingKey {
        case paymentStatus = "payment_status"
    }
}
