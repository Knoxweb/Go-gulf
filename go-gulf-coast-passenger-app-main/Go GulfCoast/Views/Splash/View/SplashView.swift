//
//  SplashView.swift
//  Amheer
//
//  Created by Prabin Phasikawo on 07/07/2023.
//

import SwiftUI
import CoreLocation
import Combine
import Stripe
import FirebaseFirestore

class BaseObservableObject: NSObject, ObservableObject, STPAuthenticationContext {
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
    
    func authenticationPresentingViewController() -> UIViewController {
        return UIApplication.shared.windows.first?.rootViewController ?? UIViewController()
    }
    
}


struct SplashView: View {
    @State private var offset: CGFloat = 0
    @State private var scale: CGFloat = 1.0
    var isFcm: Bool
    var type: String
    var bookingId: String
//    @State var updateApp = false
    @AppStorage("currentPage") var currentPage = 1
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    @StateObject var VM: SplashVM = SplashVM()
    var locationManager = LocationTracker.shared.locationManager
    
    
    var body: some View {
        VStack(spacing: 60) {
            Spacer()
            Image("HomeLogo")
                .resizable()
                .scaledToFit()
                .frame(width: 200)
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
        
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert = false
            })
        )
        .task {
            initializeApp()
            EnvironmentManager.shared.router = router
            EnvironmentManager.shared.appRoot = appRoot
           
        }
       
    }
    
  
    
    
    func initializeApp() {
        print(UserDefaults.standard.string(forKey: "UID") as Any, "My UID")
        Task(priority: .high){
            await checkAppState()
        }
        
    }
    
    @MainActor
    private func checkAppState() async {
        withAnimation {
            if (UserDefaults.standard.string(forKey: "isWalkthoughSeen") != "" && UserDefaults.standard.string(forKey: "isWalkthoughSeen") != nil) {
                print(UserDefaults.standard.string(forKey: "accessToken") as Any, "AccessTOkenn")
                if (UserDefaults.standard.string(forKey: "accessToken") != nil) {
                    VM.checkStatus()
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

struct SplashView_Previews: PreviewProvider {
    static var previews: some View {
        SplashView(isFcm: false, type: "", bookingId: "")
    }
}
