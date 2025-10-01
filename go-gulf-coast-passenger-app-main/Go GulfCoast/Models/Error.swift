//
//  Error.swift
//  133VIP
//
//  Created by Prabin Phasikawo on 18/08/2022.
//

import Foundation

// MARK: - Welcome
struct APIError: Codable, Hashable {
    let type, message: String
}
