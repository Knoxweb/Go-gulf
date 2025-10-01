//
//  BankResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 06/08/2022.
//

import Foundation


// MARK: - DataClass
struct BankResponseData: Codable {
    let bank_name, account_number, sort_code, account_holder_name: String?
}
