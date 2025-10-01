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
        case termsScreen(show: String)
        case dashboardScreen
        case emailLoginScreen
        case notificationSreen
        case settingListScreen
        case accountScreen
        case bookNowScreen
        case emailOTP(email: String)
        case cardsScreen
        case addCardScreen
        case editCardScreen(cardData: CardModel?)
        case profileEditScreen(name: String?, email: String?, phone: String?)
        case addShortcut(addr: AddressModel?)
        
        
        case placePicker(pAddress: String, pLat: Double, pLng: Double)
        case menusScreen
        case settingDetailScreen
        case scheduleBookingScreen
        case bookingHistoryScreen
        case invoicesScreen
        case supportScreen
        case vehicleSelection(data: QuoteResponse?, passenger: Int?)
        case cardListScreen
        case dateTimeScreen(pickup: String, pLat: Double, pLng: Double, via: String, vLat: Double, vLng: Double, dropoff: String, dLat: Double, dLng: Double)
        case quoteConfirm(quoteData: QuoteResponse?, fleetData: FleetList?, passenger: Int?)
//        case placePickerScreen(address: String?, lat: Double?, lng: Double?, type: String?, dropoff: String?, dLat: Double?, dLng: Double?)
        case requestingScreen(quoteData: QuoteResponseData?)
        case currentRideScreen
        case ratingScreen(bookingId: String)
        case forgotPassword
        case resetPassword(email: String, code: String)
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
        case .termsScreen (let show):
            AcceptTermsView(show: show)
        case .dashboardScreen:
            DashboardUIView()
        case .emailLoginScreen:
            LoginWithEmail()
        case .notificationSreen:
            NotificationsView()
        case .settingListScreen:
            SettingListView()
        case .accountScreen:
            SettingListView()
        case .bookNowScreen:
            BookNowUIView()
        case .emailOTP(let email):
            EmailOTPView(email: email)
            
        case .cardsScreen:
            CardDetailView()
        case .addCardScreen:
            AddCardView()
        case .editCardScreen (let cardData):
            EditCardView(cardData: cardData)
        case .profileEditScreen(let name, let email, let phone):
            ProfileDetailView(name: name, email: email, phone: phone)
        case .addShortcut(let addr):
            AddShortcutsView(addr: addr)
            
        case .placePicker(let addr, let lat, let lng):
            PlacePickerView(pAddress: addr, pLat: lat, pLng: lng)
        case .menusScreen:
            MenuListView()
        case .settingDetailScreen:
            SettingDetailView()
        case .scheduleBookingScreen:
            ScheduledBookingsView()
        case .bookingHistoryScreen:
            BookingHistoryUIView()
        case .invoicesScreen:
            InvoicesView()
        case .supportScreen:
            SupportFormView()
        case .vehicleSelection(let data, let passenger):
            VehicleListView(data: data, passenger: passenger)
        case .cardListScreen:
            CardDetailView()
        case .dateTimeScreen(let pickup, let pLat, let pLng, let via, let vLat, let vLng, let dropoff, let dLat, let dLng):
            setDateTimeView(pickup: pickup, pLat: pLat, pLng: pLng, via: via, vLat: vLat, vLng: vLng, dropoff: dropoff, dLat: dLat, dLng: dLng)
        case .quoteConfirm (let quoteData, let fleetData, let passenger):
            QuoteConfirmView(quoteData: quoteData, fleetData: fleetData, passenger: passenger)
//        case .placePickerScreen(let address, let lat, let lng, let type, let dropoff, let dLat, let dLng):
//            PlacePickerView(address: address, lat: lat, lng: lng, type: type, dropoff: dropoff, dLat: dLat, dLng: dLng)
        case .requestingScreen(let quoteData):
            RequestingRideView(quoteData: quoteData)
            
            
        case .currentRideScreen:
            CurrentRideView()
        case .ratingScreen (let booking_id):
            RatingsView(bookingId: booking_id)
        case .forgotPassword:
            ForGotPasswordView()
        case .resetPassword (let email, let code):
            ResetPasswordView(email: email, code: code)
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

