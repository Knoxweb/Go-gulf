//
//  AddCardRequest.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/25/22.
//

import Foundation

struct AddCardRequest: Codable {
    var card_number: String
    var card_holder_name: String
    var card_expiry: String
    var card_verification_code: String
}
