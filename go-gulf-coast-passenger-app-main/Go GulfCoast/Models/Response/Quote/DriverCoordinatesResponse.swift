//
//  DriverCoordinatesResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 16/07/2022.
//

import Foundation

struct DriverCoordinatesResponse: Codable, Hashable {
    let lat, lng: Double?
    let identity: String?
}
