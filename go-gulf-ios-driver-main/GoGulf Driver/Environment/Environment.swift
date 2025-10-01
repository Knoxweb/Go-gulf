//
//  Environment.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/20/22.l
//

import Foundation

struct Env {
    static var apiToken: String = "RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr"
    static let baseUrl: String = "https://app.go-gulfcoast.com/api/driver/"
    static let mapAPIKey: String = "AIzaSyDnfj0_iAlo3vFBPQR4KL05K_mph6QhEWM"
    static let websAPIKey: String = "AIzaSyC7GEGQ1_xBxmrQUUWvW7S71frlyGGmQh0"
}



class EnvironmentManager {
    static let shared = EnvironmentManager()
    var router: Router?
    var tabRouter: TabRouter?
    var appRoot: AppRootManager?

    private init() {}
}

class GlobalState: ObservableObject {
    @Published var currentCountry: CPData? = nil
    @Published var currentNumber: String? = ""
}
