//
//  CPData.swift
//  RiderApp
//
//  Created by Prabin Phasikawo on 10/10/2023.
//

import Foundation

struct CPData: Codable, Hashable, Equatable, Identifiable {
    let id: String
    let name: String
    let flag: String
    let code: String
    let dial_code: String
    let pattern: String
    let limit: Int
    
    static let allCountry: [CPData] = loadJSON("CountryNumbers.json")
    static let example = allCountry[0]
    
    static func loadJSON<T: Codable>(_ filename: String) -> T {
        guard let fileURL = Bundle.main.url(forResource: filename, withExtension: nil)
            else {
                fatalError("Failed to locate \(filename) in the app bundle.")
        }
        
        do {
            let data = try Data(contentsOf: fileURL)
            let decoder = JSONDecoder()
            return try decoder.decode(T.self, from: data)
        } catch {
            fatalError("Failed to load \(filename) from the app bundle: \(error)")
        }
    }
}
