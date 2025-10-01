//
//  DriverDocumentResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 31/07/2022.
//

import Foundation

struct DriverDocumentResponse: Codable {
    let title, message: String?
    let data: DriverDocumentResponseData?
}

// MARK: - DataClass
struct DriverDocumentResponseData: Codable {
    let licenceNo, acn, abn, licenceExpiryDate: String?
    let acnExpiryDate, fileLicenceFront, fileLicenceBack, fileAcn: String?

    enum CodingKeys: String, CodingKey {
        case licenceNo = "licence_no"
        case acn, abn
        case licenceExpiryDate = "licence_expiry_date"
        case acnExpiryDate = "acn_expiry_date"
        case fileLicenceFront = "file_licence_front"
        case fileLicenceBack = "file_licence_back"
        case fileAcn = "file_acn"
    }
}
