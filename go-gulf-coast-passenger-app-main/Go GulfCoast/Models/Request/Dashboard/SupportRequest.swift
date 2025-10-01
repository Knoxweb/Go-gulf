//
//  SupportRequest.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 12/07/2022.
//

import Foundation

struct SupportFormRequest: Codable, Hashable {
    let subject: String?
    let booking_id: String?
    let comment: String?
    let type: String?
}
