//
//  Network.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 4/25/22.
//

import Foundation

struct RegisterResource{
    func authenticate ( registerRequest: RegisterRequest, completionHandler:@escaping(_ result: RegisterResponse?)->Void) {
        var urlRequest = URLRequest (
            url: URL(string: "https://api-dev-scus-demo.azurewebsites.net/api/User/Login")!)
        urlRequest.httpMethod = "post"
        urlRequest.addValue ("application/json", forHTTPHeaderField: "content-type")
        urlRequest.httpBody = try? JSONEncoder().encode(registerRequest)
        HttpUtility.shared.postData(request: urlRequest, resultType: RegisterResponse.self) {
            response in _ = completionHandler (response)
        }
    }
}
