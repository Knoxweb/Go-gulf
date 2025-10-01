//
//  RatingRequest.swift
//   GoGulfDriver
//
//  Created by Prabin Phasikawo on 18/07/2022.
//

import Foundation
struct RatingRequest: Codable, Hashable {
    let booking_id: String?
    let rating: Int?
    let feedback: String?
}
