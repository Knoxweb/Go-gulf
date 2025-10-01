//
//  AutocompleteResponse.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/23/22.
//

import Foundation

// MARK: - Welcome
struct AutoCompleteResponse: Codable {
    let errorMessage: String?
    let htmlAttributions: String?
    let result: AutoCompleteResult?
    let status: String?

    enum CodingKeys: String, CodingKey {
        case htmlAttributions = "html_attributions"
        case result, status, errorMessage
    }
}

// MARK: - Result
struct AutoCompleteResult: Codable {
    let addressComponents: [AddressComponent]
    let adrAddress, formattedAddress: String?
    let geometry: Geometry
    let icon: String?
    let iconBackgroundColor: String?
    let iconMaskBaseURI: String?
    let name, placeID, reference: String?
    let types: [String]
    let url: String?
    let utcOffset: Int?
    let vicinity: String?

    enum CodingKeys: String, CodingKey {
        case addressComponents = "address_components"
        case adrAddress = "adr_address"
        case formattedAddress = "formatted_address"
        case geometry, icon
        case iconBackgroundColor = "icon_background_color"
        case iconMaskBaseURI = "icon_mask_base_uri"
        case name
        case placeID = "place_id"
        case reference, types, url
        case utcOffset = "utc_offset"
        case vicinity
    }
}

// MARK: - AddressComponent
struct AddressComponent: Codable {
    let longName, shortName: String?
    let types: [String]?

    enum CodingKeys: String, CodingKey {
        case longName = "long_name"
        case shortName = "short_name"
        case types
    }
}

// MARK: - Geometry
struct Geometry: Codable {
    let location: Location
    let viewport: Viewport
}

// MARK: - Location
struct Location: Codable {
    let lat, lng: Double?
}

// MARK: - Viewport
struct Viewport: Codable {
    let northeast, southwest: Location
}
