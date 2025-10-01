//
//  Go_GulfCoastApp.swift
//  Go GulfCoast
//
//  Created by Prabin Phasikawo on 05/02/2025.
//


import SwiftUI
import GoogleMaps
import GooglePlaces
import Firebase
import FirebaseAuth
import FirebaseCore
import FirebaseMessaging
import UserNotifications
import FirebaseAppCheck


//Config key
let APIKey  = Env.mapAPIKey

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate  {
    let gcmMessageIDKey = "gcm.message_id"
//    App Delegate Function that initializes the Services
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure() // Firebase Configuration Setup
        let providerFactory = AppCheckDebugProviderFactory() // Use for debugging
            AppCheck.setAppCheckProviderFactory(providerFactory)
        let pushManager = PushNotificationManager() // Push Notification Handler Methods are called
        pushManager.registerForPushNotifications() // Registered for Push Notification
        UNUserNotificationCenter.current().delegate = self
        //API key in GMSServices and GMSPlaceClient
        GMSServices.provideAPIKey(APIKey)
        GMSPlacesClient.provideAPIKey(APIKey)

        return true
    }
    
//    Register Device Token For Push Notification
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("\(#function)")
        print(deviceToken, "deviceToken")
        Auth.auth().setAPNSToken(deviceToken, type: .prod)
    }
    
//    Triggers When the Push Notification is received
    func application(_ application: UIApplication, didReceiveRemoteNotification notification: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        print("\(#function)")
        if Auth.auth().canHandleNotification(notification) {
            completionHandler(.noData)
            return
        }
    }
    
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void)
    {
        completionHandler([.banner, .list, .sound])
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let notificationData = response.notification.request.content.userInfo
        let type = notificationData["type"] as? String ?? ""
        let booking_id = notificationData["booking_id"] as? String ?? ""
        if UIApplication.shared.applicationState == .active {
            print("acccccccc")
            NotificationCenter.default.post(name: Notification.Name("NotificationReceived"), object: nil, userInfo: ["type": type, "booking_id": booking_id])
        }
        else if UIApplication.shared.applicationState == .inactive  {
            print("inacccccccc")
            NotificationCenter.default.post(name: Notification.Name("NotificationReceived"), object: nil, userInfo: ["type": type, "booking_id": booking_id])
        }
        else {
            if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
                if let window = windowScene.windows.first {
                    let rootView: AnyView
                    if UserDefaults.standard.string(forKey: "UID") != "" {
                        rootView = AnyView(
                            BaseAppView(isFcms: "yes", type: type, booking_id: booking_id)
                        )
                    } else {
                        rootView = AnyView(BaseAppView())
                    }
                    let hostingController = UIHostingController(rootView: rootView)
                    let navigationController = UINavigationController(rootViewController: hostingController)
                    navigationController.navigationBar.isHidden = true
                    navigationController.modalPresentationStyle = .overFullScreen
                    
                    if let presentedViewController = window.rootViewController?.presentedViewController {
                        presentedViewController.dismiss(animated: false) {
                            window.rootViewController?.present(navigationController, animated: true, completion: nil)
                        }
                    } else {
                        window.rootViewController?.present(navigationController, animated: true, completion: nil)
                    }
                }
            }
        }
        
        completionHandler()
    }
    
    
    
//    Callback function when the Push Notification is pressed
    func application(_ application: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any]) -> Bool {
        print("\(#function)")
        if Auth.auth().canHandle(url) {
            return true
        }
        return false
    }
}

@main
struct GoGulfApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    @State var showLaunchScreen: Bool = false
    
    init() {
        UIView.appearance(whenContainedInInstancesOf: [UIAlertController.self]).tintColor = UIColor(Color.accentColor)
        UITableView.appearance().backgroundColor = .clear
        if #available(iOS 13.0, *) {
            UIWindow.appearance().overrideUserInterfaceStyle = .dark
        }
    }
    var body: some Scene {
        WindowGroup {
            BaseAppView()
        }
        
    }
}


struct BaseAppView: View {
    @StateObject var appRootManager = AppRootManager()
    @StateObject private var globalState = GlobalState()
    @StateObject var tabRouter = TabRouter()
    @State var isFcms: String?
    @State var type: String?
    @State var booking_id: String?
    var body: some View {
        VStack {
            ContentView()
                .onOpenURL { url in
                    print("Received URL: \(url)")
                    Auth.auth().canHandle(url)
                }
                .environmentObject(globalState)
                .environmentObject(tabRouter)
                .environmentObject(appRootManager)
                .environment(\.sizeCategory, .medium)
                .background(Color.linearGradient).edgesIgnoringSafeArea(.all)
                .environment(\.colorScheme, .light)
                .onReceive(NotificationCenter.default.publisher(for: Notification.Name("NotificationReceived"))) { notification in
                    DispatchQueue.main.async {
                        if let userInfo = notification.userInfo,
                           let type = userInfo["type"] as? String,
                           let booking_id = userInfo["booking_id"] as? String {
                            appRootManager.currentRoot = .notificationSreen(isFcm: true, type: type, bookingId: booking_id)
                        }
                    }
                }
                .onAppear() {
                    UserDefaults.standard.set(true, forKey: "isFirstVisit")
                    DispatchQueue.main.async {
                        if isFcms == "yes" {
                            appRootManager.currentRoot = .splashScreen(isFcm: true, type: type ?? "", bookingId: booking_id ?? "")
                        }
                    }
                }
        }
    }
}
