//
//  DefaultResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 07/07/2022.
//

import Foundation

struct DefaultResponse: Codable {
    let title, message: String?
    let data: DefaultResponseData?
}
struct DefaultResponseData: Codable {
}
