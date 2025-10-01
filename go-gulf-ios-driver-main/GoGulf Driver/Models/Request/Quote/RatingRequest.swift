//
//  RatingRequest.swift
// SlyykDriverDriver
//
//  Created by Office on 18/07/2022.
//

import Foundation
struct RatingRequest: Codable {
    let booking_id: String?
    let rating: Int?
    let feedback: String?
}
