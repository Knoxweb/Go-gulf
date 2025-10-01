//
//  CancelBookingRequest.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 01/07/2022.
//

import Foundation

struct CancelBookingRequest: Codable, Hashable {
    var booking_id: String
    var reason: String
    var auto: Bool
}


struct PassengerBookingcancel: Codable, Hashable {
    var booking_id: String
    var reason: String
}
