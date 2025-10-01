//
//  VersionController.swift
//  DoubleLeap
//
//  Created by Office on 12/02/2023.
//

import Foundation

enum VersionError: Error {
    case invalidResponse, invalidBundleInfo
}

@discardableResult

func isUpdateAvailable(completion: @escaping (Bool?, Error?) -> Void) throws -> URLSessionDataTask {
    let identifier = Bundle.main.infoDictionary?["CFBundleIdentifier"] as? String
    let currentVersion = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String
    var urlComponents = URLComponents(string: "https://itunes.apple.com/lookup")
    
    urlComponents?.queryItems = [
        URLQueryItem(name: "bundleId", value: identifier),
        URLQueryItem(name: "timestamp", value: "\(Date().timeIntervalSince1970)")
    ]
    
    let url = urlComponents?.url
    let request = URLRequest(url: url!, cachePolicy: URLRequest.CachePolicy.reloadIgnoringLocalCacheData)
    
        let task = URLSession.shared.dataTask(with: request) { (data, response, error) in

            do {
                if let error = error { throw error }
                guard let data = data else { throw VersionError.invalidResponse }
    
                let json = try JSONSerialization.jsonObject(with: data, options: [.allowFragments]) as? [String: Any]
    
                guard let result = (json?["results"] as? [Any])?.first as? [String: Any], let lastVersion = result["version"] as? String else {
                    throw VersionError.invalidResponse
                }
                print(lastVersion, currentVersion as Any, "versionnnnnnnnnnnnnnnnn")
                completion(lastVersion > currentVersion ?? "", nil)
            } catch {
                completion(nil, error)
            }
        }
    
        task.resume()
        return task
}

