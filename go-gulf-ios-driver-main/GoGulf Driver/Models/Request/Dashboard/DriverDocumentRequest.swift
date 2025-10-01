//
//  DriverDocumentRequest.swift
// SlyykDriverDriver
//
//  Created by Office on 04/08/2022.
//

import Foundation


struct DriverDocumentModel: Codable {
    let abn, acn, acn_image, company_name, terms_image, commercial_policy_number, licence_back_image, licence_expiry_date, licence_front_image, licence_no: String?
}
