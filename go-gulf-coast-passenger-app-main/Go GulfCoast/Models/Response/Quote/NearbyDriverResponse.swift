//
//  NearbyDriverResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 13/10/2022.
//

import Foundation

// MARK: - Welcome
struct NearbyDriverResponse: Codable, Hashable {
    let title, message: String?
    let data: [NearbyDriverResponseData]
}

// MARK: - Datum
struct NearbyDriverResponseData: Codable, Hashable {
    let identity: String?
    let lat, lng: Double?
}
