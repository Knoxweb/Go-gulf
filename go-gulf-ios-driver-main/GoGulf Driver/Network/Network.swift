//
//  Network.swift
//  133VIP
//
//  Created by Laksh Purbey on 9/1/21.
//
//


import Combine
import Foundation
import UIKit
import Alamofire


class NetworkManager {
    static let shared = NetworkManager()
    
    private let baseURL = URL(string: "\(Env.baseUrl)")!
    private var authToken: String?
    
    private init() {}
    
    func UPLOAD<T: Decodable>(
        to path: String,
        images: [String: UIImage]?,
        singleImageWithPrefix: (key: String, image: UIImage)?,
        params: [String: Any] = [:]
    ) -> AnyPublisher<T, NetworkError> {
        let url = baseURL.appendingPathComponent(path)
        var request = URLRequest(url: url)
        let apiToken = UserDefaults.standard.string(forKey: "accessToken") ?? ""
        request.httpMethod = "POST"
        let boundary = "Boundary-\(UUID().uuidString)"
        request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
        request.addValue("\(Env.apiToken)", forHTTPHeaderField: "AppToken")
        request.setValue("iOS", forHTTPHeaderField: "DeviceType")
        request.setValue("\(UIDevice.current.systemVersion) \(getDeviceModel()), \(getAppVersion())", forHTTPHeaderField: "DeviceName")
        request.addValue("Bearer \(apiToken)", forHTTPHeaderField: "Authorization")
        var body = Data()
        print(apiToken, "apiiitokenen")
        // Add parameters
        for (key, value) in params {
            body.append("--\(boundary)\r\n".data(using: .utf8)!)
            body.append("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n".data(using: .utf8)!)
            body.append("\(value)\r\n".data(using: .utf8)!)
        }
        
        // Add single image with prefix
        if let (prefixKey, image) = singleImageWithPrefix {
            guard let imageData = image.jpegData(compressionQuality: 1) else {
                print("imageData", image, "herehrere")
                return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
            }
            
            let fileName = "\(prefixKey).jpg"
            let mimeType = "image/jpeg"
            
            body.append("--\(boundary)\r\n".data(using: .utf8)!)
            body.append("Content-Disposition: form-data; name=\"\(prefixKey)\"; filename=\"\(fileName)\"\r\n".data(using: .utf8)!)
            body.append("Content-Type: \(mimeType)\r\n\r\n".data(using: .utf8)!)
            body.append(imageData)
            body.append("\r\n".data(using: .utf8)!)
        }
        
        // Add multiple images with prefixes
        if let imageDict = images {
            for (key, image) in imageDict {
                if let imageData = image.jpegData(compressionQuality: 0.8) {
                    let fileName = "\(key).jpg"
                    let mimeType = "image/jpeg"
                    
                    body.append("--\(boundary)\r\n".data(using: .utf8)!)
                    body.append("Content-Disposition: form-data; name=\"\(key)\"; filename=\"\(fileName)\"\r\n".data(using: .utf8)!)
                    body.append("Content-Type: \(mimeType)\r\n\r\n".data(using: .utf8)!)
                    body.append(imageData)
                    body.append("\r\n".data(using: .utf8)!)
                    print(image, "sdfdsfsdfdsfsdfdsfdsf")
                }
            }
        }
        
        body.append("--\(boundary)--\r\n".data(using: .utf8)!)
        request.httpBody = body
        
        let networkRequestPublisher = URLSession.shared.dataTaskPublisher(for: request)
            .mapError { error in
                    .requestFailed(error)
            }
            .flatMap { data, response -> AnyPublisher<T, NetworkError> in
                guard let httpResponse = response as? HTTPURLResponse else {
                    return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                }
                
                if 200..<300 ~= httpResponse.statusCode {
                    do {
                        debugPrint("✅ \(request)", httpResponse.statusCode, "POST", convertToJson(data: data) as Any)
                        
                        let decodedData = try JSONDecoder().decode(T.self, from: data)
                        return Just(decodedData)
                            .setFailureType(to: NetworkError.self)
                            .eraseToAnyPublisher()
                    } catch let decodingError as DecodingError {
                        var errorMessage: String
                        switch decodingError {
                        case .dataCorrupted(let context):
                            errorMessage = "Data Corrupted: \(context)"
                        case .keyNotFound(let key, let context):
                            errorMessage = "Key Not Found: \(key), Context: \(context)"
                        case .typeMismatch(let type, let context):
                            errorMessage = "Type Mismatch: \(type), Context: \(context)"
                        case .valueNotFound(let type, let context):
                            errorMessage = "Value Not Found: \(type), Context: \(context)"
                        @unknown default:
                            errorMessage = "Unknown decoding error"
                        }
                        print("⚠️ \(httpResponse.statusCode) (\(request.httpMethod ?? "") \(request)", errorMessage)
                        return Fail(error: NetworkError.decodingError(errorMessage)).eraseToAnyPublisher()
                    } catch {
                        print("Unknown error during decoding: \(error)")
                        return Fail(error: NetworkError.decodingError("Unknown decoding error")).eraseToAnyPublisher()
                    }
                    
                } else if httpResponse.statusCode == 401 {
                    print(apiToken, "apiiitokenen")
                    return Fail(error: NetworkError.authenticationError).eraseToAnyPublisher()
                } else if 400...501 ~= httpResponse.statusCode {
                    if let errorResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) {
                        let error = NSError(domain: "\(request)", code: httpResponse.statusCode, userInfo: [NSLocalizedDescriptionKey: errorResponse])
                        debugPrint("❌", error)
                        return Fail(error: NetworkError.serverError(errorResponse)).eraseToAnyPublisher()
                    } else {
                        print(apiToken, "apiiitokenen")
                        return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                    }
                } else {
                    print(apiToken, "apiiitokenen")
                    return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                }
            }
            .eraseToAnyPublisher()
        
        return networkRequestPublisher
            .timeout(.seconds(30), scheduler: DispatchQueue.main, customError: { .timeoutError })
            .receive(on: DispatchQueue.main)
            .eraseToAnyPublisher()
    }
    
    func GET<T: Decodable>(from path: String) -> AnyPublisher<T, NetworkError> {
        let url = baseURL.appendingPathComponent(path)
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        print(UserDefaults.standard.string(forKey: "lang") as Any, "lanngnngngng")
        let apiToken = UserDefaults.standard.string(forKey: "accessToken") ?? ""
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.addValue("\(Env.apiToken)", forHTTPHeaderField: "AppToken")
        request.setValue("iOS", forHTTPHeaderField: "DeviceType")
        request.setValue("\(UIDevice.current.systemVersion) \(getDeviceModel()), \(getAppVersion())", forHTTPHeaderField: "DeviceName")
        request.addValue("\(UserDefaults.standard.string(forKey: "lang") ?? "en")", forHTTPHeaderField: "language")
        request.addValue("Bearer \(apiToken)", forHTTPHeaderField: "Authorization")
        
        
        print(request, "REQQQQQQQQQQQQQQQQ", apiToken, "BEARERERRERE", Env.apiToken);
        let networkRequestPublisher = URLSession.shared.dataTaskPublisher(for: request)
            .mapError { error in
                    .requestFailed(error)
            }
            .flatMap { data, response -> AnyPublisher<T, NetworkError> in
                guard let httpResponse = response as? HTTPURLResponse else {
                    return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                }
                if 200..<300 ~= httpResponse.statusCode {
                    do {
                        let decodedData = try JSONDecoder().decode(T.self, from: data)
                        print("✅", request, "GET METHOD: \(httpResponse.statusCode)", decodedData)
                        return Just(decodedData)
                            .setFailureType(to: NetworkError.self)
                            .eraseToAnyPublisher()
                    } catch let decodingError as DecodingError {
                        var errorMessage: String
                        switch decodingError {
                        case .dataCorrupted(let context):
                            errorMessage = "Data Corrupted: \(context)"
                        case .keyNotFound(let key, let context):
                            errorMessage = "Key Not Found: \(key), Context: \(context)"
                        case .typeMismatch(let type, let context):
                            errorMessage = "Type Mismatch: \(type), Context: \(context)"
                        case .valueNotFound(let type, let context):
                            errorMessage = "Value Not Found: \(type), Context: \(context)"
                        @unknown default:
                            errorMessage = "Unknown decoding error"
                        }
                        print("⚠️ \(httpResponse.statusCode) (\(request.httpMethod ?? "") \(request)", errorMessage)
                        return Fail(error: NetworkError.decodingError(errorMessage)).eraseToAnyPublisher()
                    } catch {
                        print("Unknown error during decoding: \(error)")
                        return Fail(error: NetworkError.decodingError("Unknown decoding error")).eraseToAnyPublisher()
                    }
                    
                } else if httpResponse.statusCode == 401 {
                    return Fail(error: NetworkError.authenticationError).eraseToAnyPublisher()
                } else if 400...501 ~= httpResponse.statusCode {
                    // Handle 4xx client errors (e.g., 400 Bad Request)
                    if let errorResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) {
                        let error = NSError(domain: "\(request)", code: httpResponse.statusCode, userInfo: [NSLocalizedDescriptionKey: errorResponse])
                        debugPrint("❌", error)
                        return Fail(error: NetworkError.serverError(errorResponse)).eraseToAnyPublisher()
                    } else {
                        return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                    }
                } else {
                    return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                }
            }
            .eraseToAnyPublisher()
        
        return networkRequestPublisher
            .timeout(.seconds(30), scheduler: DispatchQueue.main, customError: { .timeoutError })
            .receive(on: DispatchQueue.main)
            .eraseToAnyPublisher()
    }
    
    func downloadPDF(from path: String) -> AnyPublisher<Data, NetworkError> {
        let url = baseURL.appendingPathComponent(path)
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        let apiToken = UserDefaults.standard.string(forKey: "accessToken") ?? ""
        request.setValue("application/pdf", forHTTPHeaderField: "Accept")
        request.addValue("\(Env.apiToken)", forHTTPHeaderField: "AppToken")
        request.setValue("iOS", forHTTPHeaderField: "DeviceType")
        request.setValue("\(UIDevice.current.systemVersion) \(getDeviceModel()), \(getAppVersion())", forHTTPHeaderField: "DeviceName")
        request.addValue("Bearer \(apiToken)", forHTTPHeaderField: "Authorization")
        request.addValue("\(UserDefaults.standard.string(forKey: "lang") ?? "en")", forHTTPHeaderField: "language")
        
        let networkRequestPublisher = URLSession.shared.dataTaskPublisher(for: request)
            .mapError { error in
                .requestFailed(error)
            }
            .flatMap { data, response -> AnyPublisher<Data, NetworkError> in
                guard let httpResponse = response as? HTTPURLResponse else {
                    return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                }
                if 200..<300 ~= httpResponse.statusCode {
                    return Just(data)
                        .setFailureType(to: NetworkError.self)
                        .eraseToAnyPublisher()
                } else if httpResponse.statusCode == 401 {
                    return Fail(error: NetworkError.authenticationError).eraseToAnyPublisher()
                } else {
                    return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                }
            }
            .eraseToAnyPublisher()
        
        return networkRequestPublisher
            .timeout(.seconds(30), scheduler: DispatchQueue.main, customError: { .timeoutError })
            .receive(on: DispatchQueue.main)
            .eraseToAnyPublisher()
    }
    
   
    func multiplePDFDownload<T: Codable>(to path: String, body: T) -> AnyPublisher<Data, NetworkError> {
        let url = baseURL.appendingPathComponent(path)
        let apiToken = UserDefaults.standard.string(forKey: "accessToken") ?? ""
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Accept": "application/pdf",
            "DeviceType": "iOS",
            "AppToken": Env.apiToken,
            "Authorization": "Bearer \(apiToken)",
            "DeviceName": "\(UIDevice.current.systemVersion) \(getDeviceModel()), \(getAppVersion())",
            "language": UserDefaults.standard.string(forKey: "lang") ?? "en"
        ]

        return Future<Data, NetworkError> { promise in
            AF.request(url, method: .post, parameters: body, encoder: JSONParameterEncoder.default, headers: headers)
                .validate()
                .responseData { response in
                    switch response.result {
                    case .success(let data):
                        promise(.success(data))
                    case .failure(let error):
                        let networkError: NetworkError
                        if let afError = error.asAFError {
                            switch afError {
                            case .responseSerializationFailed(reason: _):
                                networkError = .invalidResponse
                            case .sessionTaskFailed(error: let error):
                                networkError = .requestFailed(error)
                            default:
                                networkError = .unknown
                            }
                        } else {
                            networkError = .unknown
                        }
                        promise(.failure(networkError))
                    }
                }
        }
        .eraseToAnyPublisher()
    }


    
    func POST<T: Codable, U: Decodable>(to path: String, body: T) -> AnyPublisher<U, NetworkError> {
        let url = baseURL.appendingPathComponent(path)
        var request = URLRequest(url: url)
        print(UserDefaults.standard.string(forKey: "lang") as Any, "lanngnngngng")
        let apiToken = UserDefaults.standard.string(forKey: "accessToken") ?? ""
        request.httpMethod = "POST"
        request.setValue("iOS", forHTTPHeaderField: "DeviceType")
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        request.setValue("\(UIDevice.current.systemVersion) \(getDeviceModel()), \(getAppVersion())", forHTTPHeaderField: "DeviceName")
        request.addValue("\(Env.apiToken)", forHTTPHeaderField: "AppToken")
        request.addValue("Bearer \(apiToken)", forHTTPHeaderField: "Authorization")
        
        do {
            request.httpBody = try JSONEncoder().encode(body)
            if let requestBody = request.httpBody, let requestBodyString = String(data: requestBody, encoding: .utf8) {
                print("📶 \(request) Param: \(requestBodyString)")
            }
        } catch let encodingError {
            let errorMessage = "Failed to encode request body: \(encodingError.localizedDescription)"
            return Fail(error: NetworkError.decodingError(errorMessage)).eraseToAnyPublisher()
        }
        
        let networkRequestPublisher = URLSession.shared.dataTaskPublisher(for: request)
            .mapError { error in
                print("📶 \(request) Request Error: \(error.localizedDescription)")
                return .requestFailed(error)
            }
        
            .flatMap { data, response -> AnyPublisher<U, NetworkError> in
                guard let httpResponse = response as? HTTPURLResponse else {
                    return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                }
                if 200..<300 ~= httpResponse.statusCode {
                    do {
                        if let json = convertToJson(data: data) {
                            print("✅ \(httpResponse.statusCode) (\(request.httpMethod ?? "") \(request)", json)
                        }
                        let decodedData = try JSONDecoder().decode(U.self, from: data)
                        
                        return Just(decodedData)
                            .setFailureType(to: NetworkError.self)
                            .eraseToAnyPublisher()
                    } catch let decodingError as DecodingError {
                        var errorMessage: String
                        switch decodingError {
                        case .dataCorrupted(let context):
                            errorMessage = "Data Corrupted: \(context)"
                        case .keyNotFound(let key, let context):
                            errorMessage = "Key Not Found: \(key), Context: \(context)"
                        case .typeMismatch(let type, let context):
                            errorMessage = "Type Mismatch: \(type), Context: \(context)"
                        case .valueNotFound(let type, let context):
                            errorMessage = "Value Not Found: \(type), Context: \(context)"
                        @unknown default:
                            errorMessage = "Unknown decoding error"
                        }
                        print("⚠️ \(httpResponse.statusCode) (\(request.httpMethod ?? "") \(request)", errorMessage)
                        return Fail(error: NetworkError.decodingError(errorMessage)).eraseToAnyPublisher()
                    } catch {
                        print("Unknown error during decoding: \(error)")
                        return Fail(error: NetworkError.decodingError("Unknown decoding error")).eraseToAnyPublisher()
                    }
                } else if httpResponse.statusCode == 401 {
                    return Fail(error: NetworkError.authenticationError).eraseToAnyPublisher()
                } else if 400...501 ~= httpResponse.statusCode {
                    if let errorResponse = try? JSONDecoder().decode(ErrorResponse.self, from: data) {
                        let error = NSError(domain: "\(request)", code: httpResponse.statusCode, userInfo: [NSLocalizedDescriptionKey: errorResponse])
                        debugPrint("❌", error)
                        return Fail(error: NetworkError.serverError(errorResponse)).eraseToAnyPublisher()
                    } else {
                        print(NetworkError.invalidResponse, httpResponse.statusCode, "herererererherererer")
                        return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                    }
                } else {
                    return Fail(error: NetworkError.invalidResponse).eraseToAnyPublisher()
                }
            }
            .eraseToAnyPublisher()
        
        return networkRequestPublisher
            .timeout(.seconds(30), scheduler: DispatchQueue.main, customError: { .timeoutError })
//            .retry(3)
            .receive(on: DispatchQueue.main)
            .eraseToAnyPublisher()
    }
}


class NetworkConnection: BaseObservableObject {
    func handleNetworkError(_ error: NetworkError) -> ErrorResponse {
        switch error {
        case .serverError(let err):
            return ErrorResponse(title: err.title, message: err.message, data: nil)
        case .decodingError(let err):
            return ErrorResponse(title: "Type Mismatch", message: err, data: nil)
        case .authenticationError:
            self.appRoot?.currentRoot = .homeScreen
            self.router?.popToRoot()
            return ErrorResponse(title: "Authentication Error", message: "You are not authorized. Please login", data: nil)
        case .timeoutError:
            return ErrorResponse(title: "Timeout Error", message: "The request timed out. Please try again.", data: nil)
        case .invalidResponse:
            print("invalid respomse")
            return ErrorResponse(title: "Invalid Response", message: "The server returned an invalid response. Please try again.", data: nil)
        default:
            return ErrorResponse(title: "Sorry", message: "Something went wrong", data: nil)
        }
    }
}



enum NetworkError: Error {
    case invalidURL
    case requestFailed(Error)
    case invalidResponse
    case decodingError(String)
    case authenticationError
    case timeoutError
    case unknown
    case serverError(ErrorResponse)
}

struct CustomError: Error {
    let message: String
    
    init(_ message: String) {
        self.message = message
    }
}

struct ErrorResponse: Decodable {
    let title: String?
    let message: String?
    let data: defaultResponseData?
}
struct defaultResponseData: Codable, Hashable {}

func convertToJson(data: Data) -> String? {
    do {
        // Convert Data to JSON object
        guard let jsonObject = try JSONSerialization.jsonObject(with: data) as? [String: Any] else {
            print("🎃 Failed to convert data to JSON object")
            return nil
        }
        
        // Convert JSON object to Data with pretty printing
        let prettyJsonData = try JSONSerialization.data(withJSONObject: jsonObject, options: .prettyPrinted)
        
        // Convert the pretty printed Data to a string
        if let prettyJsonString = String(data: prettyJsonData, encoding: .utf8) {
            return prettyJsonString
        } else {
            print("🎃 Failed to convert data to string")
            return nil
        }
    } catch {
        print("🎃 Error converting data to JSON: \(error)")
        return nil
    }
}

//
//import SwiftUI
//import Foundation
//import UIKit
//
//class NetworkManagers {
//    init(){
//    }
//    static let shared = NetworkManagers()
//
//    let networkReachability = NetworkReachability()
//
//    let networkMessage = ""
//    @State var navigateToHome = false
//
//    func showAlert(title: String, msg: String){
//        let alertController = GlobalAlertController(title: title, message: msg, preferredStyle: .alert)
//        alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: {_ in
//            print("foo")
//        }))
//        alertController.presentGlobally(animated: true, completion: nil)
//    }
//
//    func post<T: Decodable, Q: Encodable>(urlString: String, header: String?,encodingData: Q, completion: @escaping (T?, HTTPURLResponse?, Error?) -> ()) {
//        guard networkReachability.reachable else { showAlert(title: "No Connection", msg: "Seems like you are not connected to internet."); return }
//
//        guard let url = URL(string: "\(Env.baseUrl)\(urlString)") else { return }
//
//        let config = URLSessionConfiguration.default
//        config.timeoutIntervalForRequest = 300.0
//        config.timeoutIntervalForResource = 300.0
//        let session = URLSession(configuration: config)
//
//        let encoder = JSONEncoder()
//        encoder.outputFormatting = [.sortedKeys]
//        encoder.outputFormatting = [.withoutEscapingSlashes]
//        encoder.outputFormatting = [.prettyPrinted]
//
//        guard let encoded = try? encoder.encode(encodingData) else {
//            print("Failed to encode")
//            return
//        }
//        var request = URLRequest(url: url)
//        let apiToken = header != nil ? header : UserDefaults.standard.string(forKey: "accessToken")
////        print(apiToken as Any, urlString, "post,  ------->>>>>>>>>>>>>>>  api token ---------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
//        request.httpMethod = "POST"
//        request.httpBody = encoded
//        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
//        request.addValue("application/json", forHTTPHeaderField: "Accept")
//        request.setValue( "Bearer \(apiToken ?? "")", forHTTPHeaderField: "Authorization")
//        let task = session.dataTask(with: request) { (data, response, err) in
//            guard let data = data else { return}
//            do {
//                let dataReceived = try JSONDecoder().decode(T.self, from: data)
////                                print(dataReceived, "<<<<<<--------POSTTTTTTTTTTTT")
//                completion(dataReceived,response as? HTTPURLResponse,err)
//                let resp = response as? HTTPURLResponse
////                print("status---->>>>", resp?.statusCode as Any)
//                DispatchQueue.main.async {
//                    if(resp?.statusCode == 400){
//                    }
//                }
//
//            } catch let jsonErr {
//                DispatchQueue.main.async {
//                    print("Failed to serialize json:", jsonErr, jsonErr.localizedDescription)
//                    let alertController = GlobalAlertController(title: "Failed to serialize json", message: jsonErr.localizedDescription, preferredStyle: .alert)
//                    alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: nil))
//                    alertController.presentGlobally(animated: true, completion: nil)
//                }
//
//                completion( nil,response as? HTTPURLResponse,jsonErr)
//
//
//            }
//        }
//        task.resume()
//
//    }
//
//
//    func get<T: Decodable>(urlString: String, header: String?, completion: @escaping (T?, HTTPURLResponse?, Error?) -> ()) {
//        guard networkReachability.reachable else { showAlert(title: "No Connection", msg: "Seems like you are not connected to internet."); return }
//
//        guard let url = URL(string: "\(Env.baseUrl)\(urlString)") else { return }
//        let config = URLSessionConfiguration.default
//        config.timeoutIntervalForRequest = 300.0
//        config.timeoutIntervalForResource = 300.0
//        let session = URLSession(configuration: config)
//
//        var request = URLRequest(url: url)
//        let apiToken = header != nil ? header : UserDefaults.standard.string(forKey: "accessToken")
//
////        print(apiToken as Any, "get api token ---------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
//        request.httpMethod = "GET"
//        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
//        request.addValue("application/json", forHTTPHeaderField: "Accept")
//        request.setValue( "Bearer \(apiToken ?? "")", forHTTPHeaderField: "Authorization")
//        let task = session.dataTask(with: request) { (data,response, err) in
//            guard let data = data else { return}
//
//            do {
//                let dataReceived = try JSONDecoder().decode(T.self, from: data)
//                completion(dataReceived,response as? HTTPURLResponse,err)
//
//            } catch let jsonErr {
//                DispatchQueue.main.async {
//                    print("Failed to serialize json:", jsonErr, jsonErr.localizedDescription)
//                    let alertController = GlobalAlertController(title: "Failed to serialize json", message: jsonErr.localizedDescription, preferredStyle: .alert)
//                    alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: nil))
//                    alertController.presentGlobally(animated: true, completion: nil)
//                }
//                completion( nil,response as? HTTPURLResponse,jsonErr)
//            }
//        }
//        task.resume()
//    }
//}
//
//


func getAppVersion() -> String {
    if let version = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String,
       let build = Bundle.main.infoDictionary?["CFBundleVersion"] as? String {
        return "v\(version) (Build \(build))"
    }
    return "Version info not available"
}
