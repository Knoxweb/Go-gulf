//
//  FirebaseOTPCheck.swift
//  133VIP
//
//  Created by Prabin Phasikawo on 19/08/2022.
//

import Foundation

struct FBOtpCheck: Codable, Hashable {
    let profileStatus, authToken: String

    enum CodingKeys: String, CodingKey {
        case profileStatus = "profile_status"
        case authToken = "auth_token"
    }
}
