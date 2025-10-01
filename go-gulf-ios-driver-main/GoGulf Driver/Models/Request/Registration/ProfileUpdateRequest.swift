//
//  ProfileUpdateRequest.swift
// SlyykDriverDriver
//
//  Created by Office on 31/07/2022.
//

import Foundation

struct profileUpdateRequest: Codable {
    let name, email, image: String?
}

struct satusUpdateRequest: Codable{
    let is_online: Int?
}
