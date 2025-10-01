//
//  AcceptJob.swift
// SlyykDriverDriver
//
//  Created by Office on 29/06/2022.
//

import Foundation
struct AcceptJobRequest: Codable {
    let booking_id, type: String
    let lat, lng: Double?
}

struct RejectJobRequest: Codable {
    let booking_id, type: String
    let auto: Bool
}


struct StartJobRequest: Codable {
    let booking_id, type: String?
    let lat, lng: Double?
}
