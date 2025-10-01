//
//  SplashModel.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 04/09/2024.
//

import Foundation

struct StatusResponse:Hashable,  Codable {
    let title, message: String?
    let data: StatusResponseData?
}

// MARK: - DataClass
struct StatusResponseData:Hashable,  Codable {
    let profileStatus, firebaseReference: String?
    let currentDispatch: Int?
    let is_approved: Bool?

    enum CodingKeys: String, CodingKey {
        case profileStatus = "profile_status"
        case firebaseReference = "firebase_reference"
        case currentDispatch = "current_dispatch"
        case is_approved
    }
}
