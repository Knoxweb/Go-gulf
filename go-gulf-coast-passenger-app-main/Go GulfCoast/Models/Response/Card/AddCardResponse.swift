//
//  AddCardResponse.swift
//   GoGulf
//
//  Created by Mac on 6/25/22.
//

import Foundation

struct AddCardResponse: Codable, Hashable {
    let title, message: String
    let data: AddCardResponseData?
}

struct AddCardResponseData: Codable, Hashable {
    let cardMask: String
    let imageLink: String
    
    enum CodingKeys: String, CodingKey {
        case cardMask = "card_mask"
        case imageLink = "image_link"
    }
}

