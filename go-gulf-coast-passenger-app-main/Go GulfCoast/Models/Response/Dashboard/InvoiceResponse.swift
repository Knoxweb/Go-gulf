//
//  InvoiceResponse.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 01/12/2022.
//

import Foundation

// MARK: - Welcome
struct InvoiceResponse: Codable, Hashable {
    let title: String?
    let message: String?
    let data: InvoiceResponseData
}

struct InvoiceResponseData: Codable, Hashable {
    let amounts: InvoiceAmounts
}

struct InvoiceAmounts: Codable, Hashable {
    let total, paid, unpaid, from: String?
    let to: String?
    let invoices: [InvoiceList]
}

// MARK: - Invoice
struct InvoiceList: Codable, Hashable {
    let invoiceID, createAt, amount, status: String?

    enum CodingKeys: String, CodingKey {
        case invoiceID = "invoice_id"
        case createAt = "create_at"
        case amount, status
    }
}

