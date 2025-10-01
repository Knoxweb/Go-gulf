//
//  SplashView.swift
//  AmheerDriver
//
//  Created by Prabin Phasikawo on 11/02/2024.
//

import SwiftUI
import CoreLocation
import FirebaseFirestore
import Combine

class BaseObservableObject: NSObject, ObservableObject {
    @Published var alertTitle: String?
    @Published var alertMessage: String?
    @Published var cancellables = Set<AnyCancellable>()
    @Published var loading = false
    @Published var redacting = false
    @Published var showAlert = false
    @Environment(\.presentationMode) var presentationMode
    public var router: Router? { EnvironmentManager.shared.router }
    public var appRoot: AppRootManager? { EnvironmentManager.shared.appRoot }
    
    let db = Firestore.firestore()
    var viewDismissalModePublisher = PassthroughSubject<Bool, Never>()
    public var shouldDismissView = false {
        didSet {
            viewDismissalModePublisher.send(shouldDismissView)
        }
    }
    
}

struct SplashView: View {
    @State var showLaunchScreen: Bool = false
    @State private var offset: CGFloat = 0
    @State private var scale: CGFloat = 1.0
    @Environment(\.scenePhase) var scenePhase
    var isFcm: Bool
    var type: String
    var bookingId: String
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    @EnvironmentObject var tabRouter: TabRouter
    var locationManager = LocationTracker.shared.locationManager
    @State var updateApp = false
    @AppStorage("currentPage") var currentPage = 1
    @StateObject var VM: SplashVM = SplashVM()
    
    var body: some View {
        VStack {
            Spacer()
            Image("HomeLogo")
                .resizable()
                .scaledToFit()
                .frame(width: 200, height: 200)
            Text("Driver".uppercased())
                .tracking(10)
                .padding(.top)
                .foregroundColor(Color("AccentColor"))
            Spacer()
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(
            Image("theme-bg")
                .resizable()
                .scaledToFill()
                .ignoresSafeArea()
        )
        
        .frame(maxWidth: .infinity)
        .onChange(of: scenePhase) { newPhase in
                if newPhase == .inactive {
                    print("Inactive")
                    return;
                } else if newPhase == .background {
                    print("Background")
                    return;
                }
                else if newPhase == .active {
                    if !self.enableLocationServices() {}
                    print("Inactive")
                }
                else{
                    return;
                }
            }
            .task {
                initializeApp()
                EnvironmentManager.shared.router = router
                EnvironmentManager.shared.appRoot = appRoot
                EnvironmentManager.shared.tabRouter = tabRouter
                let needUpdate = try? isUpdateAvailable { (update, error) in
                    if error != nil {
                    } else if update ?? false {
                        self.updateApp = true
                        print("update Available ----")
                    }
                    
                }
            }
    }
    
    @MainActor
    private func checkAppState() async {
        withAnimation {
            if (UserDefaults.standard.string(forKey: "isWalkthoughSeen") != "" && UserDefaults.standard.string(forKey: "isWalkthoughSeen") != nil) {
                print(UserDefaults.standard.string(forKey: "accessToken") as Any, "AccessTOkenn")
                if (UserDefaults.standard.string(forKey: "accessToken") != nil) {
//                    appRoot.currentRoot = .homeScreen
//                    router.popToRoot()
                    
                    VM.checkStatus(tabRouter: tabRouter)
                }
                else{
                    appRoot.currentRoot = .homeScreen
                    router.popToRoot()
                }
            } else {
                appRoot.currentRoot = .walkthoughScreen
                router.popToRoot()
            }
        }
    }
    
    func initializeApp() {
        print(UserDefaults.standard.string(forKey: "UID") as Any, "My UID")
        Task(priority: .high){
            await checkAppState()
        }
        
    }
    
    
    
    func enableLocationServices() -> Bool {
        var permission = false
        let authorizationStatus: CLAuthorizationStatus
        
        if #available(iOS 14, *) {
            authorizationStatus = locationManager.authorizationStatus
        } else {
            authorizationStatus = CLLocationManager.authorizationStatus()
        }
        switch authorizationStatus {
        case .notDetermined:
            // Request when-in-use authorization initially
            print("Request when-in-use authorization initially")
        case .restricted, .denied:
            // Disable location features
            print("Fail permission to get current location of user Disable location features")
        case .authorizedWhenInUse:
            print("Enable basic location features when in use")
        case .authorizedAlways:
            permission = true
            LocationTracker.shared.enableMyAlwaysFeatures()
        @unknown default:
            break
        }
        return permission
    }
    
}

#Preview {
    SplashView(isFcm: false, type: "", bookingId: "")
}
