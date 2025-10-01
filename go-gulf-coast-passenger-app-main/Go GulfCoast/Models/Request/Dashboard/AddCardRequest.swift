//
//  AddCardRequest.swift
//   GoGulf
//
//  Created by Mac on 6/25/22.
//

import Foundation

struct AddCardRequest: Codable, Hashable {
    var card_number: String
    var card_holder_name: String
    var card_expiry: String
    var card_verification_code: String
}

struct AddAddressModel: Codable, Hashable {
    let name, address, lat, lng: String?
}
