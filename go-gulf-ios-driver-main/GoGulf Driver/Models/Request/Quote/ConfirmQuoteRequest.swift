//
//  ConfirmQuoteRequest.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/24/22.
//

import Foundation

struct ConfirmQuoteRequest: Codable {
    var quote_id: String
    var fleet_id: Int
    var driver_note: String?
    var extras: [extras]?
    struct extras: Codable {
        var passenger: Int?
        var pet: Int?
        var wheel_chair: Int?
    }
    
}
