//
//  CancelBookingResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 01/07/2022.
//

import Foundation

struct CancelBookingResponse: Codable, Hashable {
    var title, message: String?
    var data: CancelBookingResponseData?
}
struct CancelBookingResponseData: Codable, Hashable {
}
