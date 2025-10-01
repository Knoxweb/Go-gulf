//
//  DashboardResponse.swift
//   GoGulf
//
//  Created by Mac on 6/19/22.
//

import Foundation



// MARK: - User
struct FBProfileData: Codable, Hashable {
    let name, email, mobile, profile_picture_url, profile_status, reference: String?
    let ratings: String?
}

struct settingDetailModel {
    var docId: String?
    var title: String?
    var user: String?
    var name: String?
    var description: String?
}

struct FCMTokenResponse: Codable, Hashable {
    let title, message: String
}


struct invoiceDownloadResponse: Codable, Hashable {
    let title, message: String?
    let data: invoiceDownloadResponseData
}

// MARK: - DataClass
struct invoiceDownloadResponseData: Codable, Hashable {
    let link: String?
    let name: String?
}



struct AddressModel: Codable, Hashable {
    let address, name: String?
    let id: Int?
    let lat, lng: Double?
}
