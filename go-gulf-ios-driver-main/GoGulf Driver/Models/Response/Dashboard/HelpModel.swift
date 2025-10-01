//
//  HelpModel.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 18/09/2024.
//

import Foundation

struct HelpModel: Codable {
    let data: [HelpData]?
    
}
struct HelpData: Codable {
    let country: String?
    let id: Int?
    let mobile: String?
    let status: Int?
}


struct LegalModel: Codable{
    let data: [LegalData]?
    let title: String?
}

struct LegalData: Codable {
    let heading: String?
    let list: [LegalList]?
}

struct LegalList: Codable {
    let content: String?
    let type: String?
}
