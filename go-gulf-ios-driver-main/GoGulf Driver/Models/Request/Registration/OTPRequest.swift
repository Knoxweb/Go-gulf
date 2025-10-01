//
//  OTPRequest.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/20/22.
//

import Foundation

struct OTPRequest: Codable, Hashable {
    let uid, phone_cc, phone: String
}

struct LoginRequest:Hashable,  Codable {
    let uid: String?
    let mobile: String?
    let voip_token: String?
    let device_token: String?
}

struct LoginResponse:Hashable,  Codable {
    let title, message: String?
    let data: LoginResponseData?
}

struct SocialLoginRequest:Hashable,  Codable {
    let uid, email: String?
}


struct SocialRegisterRequest:Hashable,  Codable {
    let first_name, last_name, email, mobile: String?
}


struct ForgotPasswordResponse:Hashable,  Codable {
    let title, message: String?
    let data: ForgotPasswordResponseData?
}

// MARK: - DataClass
struct ForgotPasswordResponseData:Hashable,  Codable {
    let status: Bool?
    let message: String?
}

struct OTPCheckRequest:Hashable,  Codable {
    let email: String?
    let code: String?
}

// MARK: - DataClass
struct LoginResponseData:Hashable,  Codable {
    let profile_status, auth_token, ref: String?

}
