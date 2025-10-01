//
//  AddCardResponse.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/25/22.
//

import Foundation

struct AddCardResponse: Codable {
    let title, message: String
    let data: AddCardResponseData?
}

struct AddCardResponseData: Codable {
    let cardMask: String
    let imageLink: String
    
    enum CodingKeys: String, CodingKey {
        case cardMask = "card_mask"
        case imageLink = "image_link"
    }
}
