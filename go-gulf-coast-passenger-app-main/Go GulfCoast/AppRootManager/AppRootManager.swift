//
//  AppRootManager.swift
//  NavigationCheck
//
//  Created by Prabin Phasikawo on 04/02/2024.
//

import Foundation

final class AppRootManager: ObservableObject {
    @Published var currentRoot: eAppRoots = .splashScreen(isFcm: false, type: "", bookingId: "")
    enum eAppRoots {
        case walkthoughScreen
        case appUpdateScreen
        case splashScreen(isFcm: Bool, type: String, bookingId: String)
        case homeScreen
        case ratingScreen(bookingId: String)
        case registerScreen
        case termsScreen(endpoint: String, hasToolbar: Bool?)
        case settingScreen
        case otpScreen
        case dashboardScreen
        case bookNowScreen
        case registerWithEmail
        case notificationSreen(isFcm: Bool, type: String, bookingId: String)
        case currentRideScreen
        case routerView
        case tabs
    }
}
