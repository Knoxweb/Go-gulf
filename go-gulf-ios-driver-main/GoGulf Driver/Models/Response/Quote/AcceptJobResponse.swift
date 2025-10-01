//
//  AcceptJobResponse.swift
// SlyykDriverDriver
//
//  Created by Office on 29/06/2022.
//

import Foundation
struct AcceptJobResponse: Codable {
    let title: String?
    let message: String?
    let data: AcceptJobResponseData?
}
struct AcceptJobResponseData: Codable {
    
}
