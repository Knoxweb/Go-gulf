//
//  ProfileResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 07/07/2022.
//

import Foundation

struct ProfileResponse: Codable, Hashable {
    let title, message: String?
    let data: ProfileResponseData?
}
struct ProfileResponseData: Codable, Hashable {
    let name: String?
    let email: String?
    let phone: String?
    let imageLink: String?
    let card: ProfileResponseCard?
    enum CodingKeys: String, CodingKey {
        case name, email, phone
        case imageLink = "image_link"
        case card
    }
}
struct ProfileResponseCard: Codable, Hashable {
    let cardMasked: String?
    let cardHolderName: String?
    
    enum CodingKeys: String, CodingKey {
        case cardMasked = "card_masked"
        case cardHolderName = "card_holder_name"
    }
}
