//
//  SupportFormReqest.swift
// SlyykDriverDriver
//
//  Created by Office on 19/07/2022.
//

import Foundation

struct SupportFormRequest: Codable {
    let subject: String?
    let booking_id: String?
    let comment: String?
    let type: String?
}
