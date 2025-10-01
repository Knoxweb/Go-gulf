//
//  ProfileUpdateRequest.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 31/07/2022.
//

import Foundation

struct profileUpdateRequest: Codable, Hashable {
    let name, email, image: String?
}
