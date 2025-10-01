//
//  BankInfoRequest.swift
// SlyykDriverDriver
//
//  Created by Office on 07/08/2022.
//

import Foundation

struct BankInfoRequest: Codable {
    var bank_name: String?
    var bank_account_holder_name: String?
    var bank_account_no: String?
    var bank_sort_code: String?
}
