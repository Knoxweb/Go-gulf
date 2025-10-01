//
//  InvoiceModel.swift
//  Connect Smart Drive
//
//  Created by Prabin Phasikawo on 28/05/2024.
//

import Foundation

struct PaymentModel:Hashable,  Codable {
    let brand: String?
    let brand_image: String?
    let card_masked: String?
    let email: String?
    let exp_month: Int?
    let exp_year: Int?
    let id: Int?
    let is_active: Bool?
    let name: String
    let country: String?
}


struct CardPaymentModel: Codable {
    let brand: String?
    let brandImage: String?
}


struct UpdateCardModel: Codable {
    let name: String?
    let exp_year: String?
    let exp_month: String?
}


struct InvoiceModel: Codable, Hashable {
    let amount: String?
    let client_secret: String?
    let generated_at: String?
    let id: Int?
    let reference: String?
    let refund_amount: String?
    let remark: String?
    let status: String?
    let status_title: String?
    let timestamp: Int?
    
}

struct UpdateInvoiceModel: Codable {
    let payment_status: String?
    let payment_id: String?
}

struct Retryinvoice: Codable {
    let card_id: String?
}

struct StatusModel: Codable, Hashable {
    let color: String?
    let text: String?
    let type: String?
}


struct MultiplePDFRequestModel: Codable {
    let invoices: [Int]?
}
