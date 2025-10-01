//
//  ContentView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 17/12/2022.
//

import SwiftUI


struct TextFieldView: View {
    @Binding var field: String
    @State var text: LocalizedStringKey?
    @State var placeholder: LocalizedStringKey?
    @State var caption: LocalizedStringKey?
    @State var keyboardType: UIKeyboardType?
    @State var axis: Axis?
    @State var localizedCaption: LocalizedStringKey?
    @State var localizedText: LocalizedStringKey?
    @State var Localizedplaceholder: LocalizedStringKey?
    
    var body: some View {
        VStack {
            VStack (alignment: .leading, spacing: 8){
                Group {
                    if let text =  text{
                        Text(text)
                    }
                }
                .font(.system(size: 13))
                .foregroundStyle(Color.gray)
                Group {
                    TextField(placeholder ?? "", text: $field, axis: axis ?? .horizontal)
                }
                .textFieldStyle()
                .keyboardType(keyboardType  ?? .default)
            }
            Group {
                if let caption = caption {
                    Text(caption)
                }
            }
            .font(.caption)
        }
    }
}


struct ContentView: View {
    let token = (UserDefaults.standard.string(forKey: "accessToken") ?? "");
    let networkReachability = NetworkReachability()
    @EnvironmentObject var appRootManager: AppRootManager
    @AppStorage("currentPage") var currentPage = 1
    @Environment(\.scenePhase) var scenePhase
    @State var navigateToAccess = false
    @State var tokenExpired = false
    
    @State var isFcm: Bool?
    @State var type: String?
    @State var booking_id: String?
    
    var body: some View {
        RouterView {
            switch appRootManager.currentRoot {
            case .walkthoughScreen:
                Walkthrough()
            case .splashScreen(let isFcm, let type, let bookingId) :
                SplashView(isFcm: isFcm, type: type, bookingId: bookingId)
            case .homeScreen:
                homeUIView()
            case .ratingScreen (let booking_id):
                RatingsView(bookingId: booking_id)
            case .appUpdateScreen:
                AppUpdateView()
            case .registerScreen:
                RegistersUiView()
            case .termsScreen(let endpoint, let hasToolbar):
                LegalView(endpoint: endpoint, hasToolbar: hasToolbar)
            case .dashboardScreen:
                DashboardUIView()
            case .otpScreen:
                OTPUIView(show: true, ID: "", phoneCC: "", phoneNumber: "")
            case .notificationSreen(let isFcm, let type, let bookingId):
                NotificationsView(isFcm: isFcm, type: type, bookingId: bookingId)
            case .bookNowScreen:
                BookNowUIView()
            case .registerWithEmail:
                RegisterEmailView()
            case .currentRideScreen:
                CurrentRideView()
            case .settingScreen:
                SettingListView()
            case .tabs:
                TabScreenView()
            default:
                homeUIView()
            }
        }
        .navigationViewStyle(.stack)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
