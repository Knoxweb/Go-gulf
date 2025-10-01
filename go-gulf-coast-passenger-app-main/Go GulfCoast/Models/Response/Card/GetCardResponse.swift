//
//  GetCardResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 13/07/2022.
//

import Foundation

struct GetCardResponse: Codable, Hashable {
    let title, message: String?
    let data: [GetCardResponseData]?
}
struct GetCardResponseData: Codable, Hashable {
    let cardMasked: String?
    let id: Int?
    let brand: String?
    let cardHolderName: String?
    let cardExpiryMonth: String?
    let CVC: String?
    let cardExpiryYear: String?
    let isActive: Int?
    let imageLink: String?
    let status: String?
    
    enum CodingKeys: String, CodingKey {
        case cardMasked = "card_masked"
        case id, brand
        case cardHolderName = "card_holder_name"
        case cardExpiryMonth = "card_expiry_month"
        case CVC = "card_verification_number"
        case cardExpiryYear = "card_expiry_year"
        case isActive = "is_active"
        case imageLink = "image_link"
        case status
    }
}


struct CardModel: Codable, Hashable {
    let address: String?
    let brand: String?
    let brand_image: String?
    let card_masked: String?
    let email: String?
    let exp_month: Int?
    let exp_year: Int?
    let id: Int?
    let is_active: Bool?
    let name: String?
}
