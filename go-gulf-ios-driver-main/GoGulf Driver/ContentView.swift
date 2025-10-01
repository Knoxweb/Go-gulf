//
//  ContentView.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/8/22.
//

import SwiftUI
import CoreLocation

extension Color {
    static let systemBackground = Color("FormField")
}

struct ClearListBackgroundModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.scrollContentBackground(.hidden)
        } else {
            content
        }
    }
}
  
extension View {
    func clearListBackground() -> some View {
        modifier(ClearListBackgroundModifier())
    }
}


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
    @AppStorage("currentPage") var currentPage = 1
    var locationManager = LocationTracker.shared.locationManager
    @Environment(\.scenePhase) var scenePhase
    let prevDay = UserDefaults.standard.string(forKey: "prevDay")
    let date = Date.now
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject private var router: Router
    @State var updateApp = false
    @State var navigateToLocationSettting = false
    
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    
    init() {
        if #unavailable(iOS 16.0) {
            UITableView.appearance().backgroundColor = .clear
        }
    }
    var body: some View {
        RouterView {
            switch appRootManager.currentRoot {
            case .walkthoughScreen:
                Walkthrough()
            case .splashScreen(let isFcm, let type, let bookingId) :
                SplashView(isFcm: isFcm, type: type, bookingId: bookingId)
            case .homeScreen:
                homeUIView()
            case .appUpdateScreen:
                AppUpdateView()
            case .accessLocationScreen:
                AccessLocationView()
            case .termsScreen(let endpoint, let hasToolbar):
                LegalView(endpoint: endpoint, hasToolbar: hasToolbar)
            case .safetyInspectionScreen:
                SafetyInspectionView()
            case .dashboardScreen:
                DashboardUIView()
            case .dispatchScreen:
                DispatchView()
            case .registerScreen:
                RegistersUiView()
            case .registerWithEmail:
                RegisterEmailView()
            case .notificationSreen(let isFcm, let type, let bookingId):
                NotificationsView(isFcm: isFcm, type: type, bookingId: bookingId)
            case .currentRideScreen:
                CurrentRideView()
            case .ratingScreen (let bookingId):
                RatingsView(bookingId: bookingId)
            case .tabs:
                TabScreenView()
            default:
                homeUIView()
            }
        }
        .environmentObject(appRootManager)
        .navigationViewStyle(.stack)
        .onAppear(){
            print("token", token, "rorkkkkkn")
            LocationTracker.shared.startTracking()
        }
    }
}
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
