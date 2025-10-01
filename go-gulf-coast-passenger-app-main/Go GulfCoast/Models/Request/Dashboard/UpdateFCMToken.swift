//
//  updateFCMToken.swift
//   GoGulf
//
//  Created by Mac on 6/25/22.
//

import Foundation

struct UpdateFCMToken: Codable, Hashable {
    var device_type: String
    var device_token: String
}
