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
        case registerScreen
        case registerWithEmail
        case termsScreen(endpoint: String, hasToolbar: Bool?)
        case dashboardScreen
        case bookNowScreen
        case dispatchScreen
        case notificationSreen(isFcm: Bool, type: String, bookingId: String)
        case requestingScreen(currentResponse: CurrentRideResponse?)
        case currentRideScreen
        case routerView
        case accessLocationScreen
        case safetyInspectionScreen
        case ratingScreen(bookingId: String)
        case tabs
        
    }
}
