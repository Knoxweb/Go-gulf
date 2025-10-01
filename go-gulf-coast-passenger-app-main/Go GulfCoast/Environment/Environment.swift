//
//  Environment.swift
//   GoGulf
//
//  Created by Mac on 6/20/22.l
//

import Foundation

struct Env {
    static var apiToken: String = "RK9AIh0GFJ640FZhe1eSTUpgoU1h48OmwqrbbDFGL9zqbtREix1hxUr"
    static let baseUrl: String = "https://app.go-gulfcoast.com/api/passenger/"
    static let mapAPIKey: String = "AIzaSyDnfj0_iAlo3vFBPQR4KL05K_mph6QhEWM"
//    static let baseUrl: String = "https://GoGulf./coredreams.com/api/passenger/"
//    static let mapAPIKey: String = "AIzaSyCwC5nO4mBvWK9bmNnu417kQsbDrj7LI2A"
    
    static let websAPIKey: String = "AIzaSyBtAARMPA6CMtKHuczB-jq0LM_UQjdqcDo"
    static let stripePubKey: String = "pk_live_51ITfNyDblVjAFCXKkhXY5oqWXSBKXOygHWK2Ov4WOtrT1DLshWLuZ38feNIMxGXNBvs84UovVUJleDhOaBtX4hwZ00zkpH5yHd"
}
 


class EnvironmentManager {
    static let shared = EnvironmentManager()
    var router: Router?
    var appRoot: AppRootManager?

    private init() {}
}

class GlobalState: ObservableObject {
    @Published var currentCountry: CPData? = nil
    @Published var currentNumber: String? = ""
}

