//
//  StatementModel.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 01/10/2024.
//

import Foundation


struct StatementModel: Codable {
    let amount: String?
    let end_date: String?
    let generated_at: String?
    let id: Int?
    let reference: String?
    let remark: String?
    let start_date: String?
    let status: String?
    let status_title: String?
    let timestamp: Int?
}
