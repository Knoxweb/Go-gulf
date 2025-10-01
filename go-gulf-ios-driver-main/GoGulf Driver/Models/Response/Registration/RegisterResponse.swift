//
//  user.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 4/25/22.
//

import Foundation

struct LogoutModel:Hashable,  Codable {
    let device_token: String?
    let voip_token: String?
}


struct RegisterRequest:Hashable,  Codable {
    let name: String?
    let mobile: String?
    let email: String?
    let password: String?
//    let passengerType: String?
    enum CodingKeys: String, CodingKey {
        case name
        case mobile
        case email
        case password
//        case passengerType = "passenger_type"
    }
}



struct SocialRegisterAccount: Codable {
    let first_name: String?
    let last_name: String?
    let email: String?
    let mobile: String?
    let passenger_type: String?
}

struct RegisterResponse:Hashable,  Codable {
    let title, message: String?
    let data: RegisterResponseData?
}

// MARK: - DataClass
struct RegisterResponseData:Hashable,  Codable {
    let firebaseAuthToken: String?

    enum CodingKeys: String, CodingKey {
        case firebaseAuthToken = "firebase_auth_token"
    }
}
