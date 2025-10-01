//
//  RouterManager.swift
//  NavigationCheck
//
//  Created by Prabin Phasikawo on 04/02/2024.
//

import Foundation
import SwiftUI

class Router: ObservableObject {

    // Contains the possible destinations in our Router
    enum Route: Hashable {
        case splashScreen
        case homeScreen
        case phoneNumberScreen
        case otpScreen(show: Bool, ID: String, phoneCC: String, phoneNumber: String)
        case registerScreen
        case registerWithEmail
        case legalScreen(endpoint: String)
        case menuScreen
        case accessLocationScreen
        case dashboardScreen
        case safetyInspectionScreen
        case dispatchScreen
        case currentRideScreen
        case notificationSreen(isFcm: Bool, type: String, bookingId: String)
        case ratingScreen(bookingId: String)
        case profileEditScreen(name: String?, email: String?, phone: String?)
        case forgotPassword
        case resetPassword(email: String, code: String)
        case emailOTP(email: String)
        case emailLoginScreen
        case menusScreen
        case pickups
        case historyScreen
        case driverDocScreen
        case vehicleDocScreen
        case statementScreen
        case support
        case account
        case tabs
        case bookingDetail(bookingId: String?, type: String?)
    }


    // Used to programatically control our navigation stack
    @Published var path: NavigationPath = NavigationPath()
    
    
    // Builds the views
    @ViewBuilder func view(for route: Route) -> some View {
        switch route {
        case .splashScreen:
            SplashView(isFcm: false, type: "", bookingId: "")
        case .homeScreen:
            homeUIView()
        case .phoneNumberScreen:
            PhoneVerifyUIView()
        case .otpScreen(let show, let ID, let phoneCC, let phoneNumber):
            OTPUIView(show: show, ID: ID, phoneCC: phoneCC, phoneNumber: phoneNumber)
        case .registerScreen:
            RegistersUiView()
        case .registerWithEmail:
            RegisterEmailView()
        case .legalScreen (let endpoint):
            LegalView(endpoint: endpoint)
        case .menuScreen:
            MenuListView()
        case .accessLocationScreen:
            AccessLocationView()
        case .dashboardScreen:
            DashboardUIView()
        case .safetyInspectionScreen:
            SafetyInspectionView()
        case .dispatchScreen:
            DispatchView()
        case .currentRideScreen:
            CurrentRideView()
        case .notificationSreen(let isFcm, let type, let bookingId):
            NotificationsView(isFcm: isFcm, type: type, bookingId: bookingId)
        case .ratingScreen (let booking_id):
            RatingsView(bookingId: booking_id)
        case .profileEditScreen(let name, let email, let phone):
            ProfileDetailView(name: name, email: email, phone: phone)
        case .forgotPassword:
            ForGotPasswordView()
        case .resetPassword (let email, let code):
            ResetPasswordView(email: email, code: code)
        case .emailOTP(let email):
            EmailOTPView(email: email)
        case .emailLoginScreen:
            LoginWithEmail()
        case .menusScreen:
            MenuListView()
        case .pickups:
            ScheduledBookingsView()
        case .historyScreen:
            BookingHistoryUIView()
        case .driverDocScreen:
            DriverDocumentView()
        case .vehicleDocScreen:
            VehicleListedView()
        case .statementScreen:
            StatementsView()
        case .support:
            SupportFormView()
        case .account:
            SettingListView()
        case .tabs:
            TabScreenView()
        case .bookingDetail(let bookingId, let type):
            BookingDetailView(bookingId: bookingId, type: type)
        }
        
    }
    
    // Used by views to navigate to another view
    func navigateTo(_ appRoute: Route) {
        path.append(appRoute)
    }

    // Used to go back to the previous screen
    func navigateBack() {
        if path.count > 1 {
            path.removeLast()
        }
    }

    // Pop to the root screen in our hierarchy
    func popToRoot() {
//        print(path.count, "paththhthsss countntntntn")
        if path.count > 1 {
            path.removeLast(path.count)
        }
    }
    
    func removeLast(_ count: Int) {
        path.removeLast(count)
    }

    func removeStacks() {
        path = NavigationPath()
    }
}

