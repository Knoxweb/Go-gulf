//
//  DefaultResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 07/07/2022.
//

import Foundation

struct DefaultResponse: Codable, Hashable {
    let title, message: String?
    let data: DefaultResponseData?
}
struct DefaultResponseData: Codable, Hashable {
}

