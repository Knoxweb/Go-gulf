//
//  GoGulf_DriverApp.swift
//  GoGulf Driver
//
//  Created by Prabin Phasikawo on 16/02/2025.
//


import SwiftUI
import GoogleMaps
import GooglePlaces
import Firebase
import FirebaseAuth
import BackgroundTasks
import FirebaseCrashlytics
import PushKit
import CallKit



let APIKey = Env.mapAPIKey

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    var window: UIWindow?
    let vibration = Timer.publish(every: 1, tolerance: 0.5, on: .main, in: .common).autoconnect()
    @State private var vibrator = 1
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
        let pushManager = PushNotificationManager()
        pushManager.registerForPushNotifications()
        //API key in GMSServices and GMSPlaceClient
        UNUserNotificationCenter.current().delegate = self
        GMSServices.provideAPIKey(APIKey)
        GMSPlacesClient.provideAPIKey(APIKey)
        BGTaskScheduler.shared.register(forTaskWithIdentifier: "com.gogulf.driver.backgroundfetch", using: DispatchQueue.main) { task in
            self.handleAppRefresh(task: task as! BGProcessingTask)
        }
        
        let pushRegistry = PKPushRegistry(queue: DispatchQueue.main)
        pushRegistry.delegate = self
        pushRegistry.desiredPushTypes = [.voIP]
        
        self.voipRegistration()
        return true
    }
    
    
    func voipRegistration() {
        let mainQueue = DispatchQueue.main
        // Create a push registry object
        let voipRegistry: PKPushRegistry = PKPushRegistry(queue: mainQueue)
        // Set the registry's delegate to self
        voipRegistry.delegate = self
        // Set the push type to VoIP
        voipRegistry.desiredPushTypes = [.voIP]
    }
    
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        print("\(#function)")
        Auth.auth().setAPNSToken(deviceToken, type: .prod)
    }
    
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
        print(notificationData, "noTIITITITITITITITITITITITITITITITITI")
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
    
    func application(_ application: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any]) -> Bool {
        print("\(#function)")
        if Auth.auth().canHandle(url) {
            return true
        }
        if url.scheme == "slyykapp" {
            if let host = url.host {
                print("Deep link to: \(host)")
            }
            return true
        }
        return false
    }
    
    func applicationDidEnterBackground(_ application: UIApplication) {
        print("App Entered Background")
        scheduleAppRefresh()
    }
    
    
    func scheduleAppRefresh() {
        let request = BGProcessingTaskRequest(identifier: "com.gogulf.driver.backgroundfetch")
        request.requiresNetworkConnectivity = true
        request.earliestBeginDate = Date(timeIntervalSinceNow: 60)
        
        do {
            try BGTaskScheduler.shared.submit(request)
            print("BGTask Scheduled")
        } catch {
            print("Could not schedule app refresh: \(error)")
        }
    }
    func handleAppRefresh(task: BGProcessingTask) {
        scheduleAppRefresh()
        
    }
    
    
    
}



extension AppDelegate : PKPushRegistryDelegate, CXProviderDelegate {
    
    // Handle updated push credentials
    func pushRegistry(_ registry: PKPushRegistry, didUpdate pushCredentials: PKPushCredentials, for type: PKPushType) {
        let deviceTokenString = pushCredentials.token.reduce("", { $0 + String(format: "%02X", $1) })
        print("VoIP device token: \(deviceTokenString)")
        UserDefaults.standard.set(deviceTokenString, forKey: "VOIPToken")
        // Register VoIP push token (a property of PKPushCredentials) with server
    }
    
    // Handle incoming pushes
    func pushRegistry(_ registry: PKPushRegistry, didReceiveIncomingPushWith payload: PKPushPayload, for type: PKPushType, completion: @escaping () -> Void) {
        if type == .voIP {
            print("Received VoIP push notification: \(payload.dictionaryPayload)")
            
            // Extract the call information from the push notification payload
//            let meetingId = payload.dictionaryPayload["meeting_id"] as? String
            let aps = payload.dictionaryPayload["aps"] as? [String: Any]
            let alert = aps?["alert"] as? [String: Any]
            let title = alert?["title"] as? String
            let type = payload.dictionaryPayload["type"] as? String

        }
    }
    
    func pushRegistry(_ registry: PKPushRegistry, didInvalidatePushTokenFor type: PKPushType) {
        // Notify your push server to invalid VoIP push token
    }
    /// Called when the provider has been reset. Delegates must respond to this callback by cleaning up all internal call state (disconnecting communication channels, releasing network resources, etc.). This callback can be treated as a request to end all calls without the need to respond to any actions
    func providerDidReset(_ provider: CXProvider) {
        print("Function: \(#function) called")
    }
    
    //optional delegate methods
}


@main
struct GoGulf_DriverApp: App {
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
    @State private var isLinkHandled = false
    
    var body: some View {
        VStack {
            ContentView()
                .environment(\.sizeCategory, .medium)
                .environment(\.colorScheme, .light)
                .environmentObject(globalState)
                .environmentObject(tabRouter)
                .environmentObject(appRootManager)
                .onOpenURL { url in
                    print("Received URL: \(url)")
//                    handleDeepLink(url)
                    Auth.auth().canHandle(url)
                }
//                .onReceive(NotificationCenter.default.publisher(for: Notification.Name("NotificationReceived"))) { notification in
//                    if let userInfo = notification.userInfo,
//                       let type = userInfo["type"] as? String,
//                       let booking_id = userInfo["booking_id"] as? String {
//                        appRootManager.currentRoot = .notificationSreen(isFcm: true, type: type, bookingId: booking_id)
//                    }
//                }
//                .onAppear() {
//                    UserDefaults.standard.set(true, forKey: "isFirstVisit")
//                    if isFcms == "yes" {
//                        appRootManager.currentRoot = .splashScreen(isFcm: true, type: type ?? "", bookingId: booking_id ?? "")
//                    }
//                }
        }
    }
    private func handleDeepLink(_ url: URL) {
        guard !isLinkHandled else {
            return
        }
        if UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        } else {
            if let appStoreURLString = Bundle.main.infoDictionary?["https://apps.apple.com/au/app/slyyk-driver/id1585805676"] as? String,
               let appStoreURL = URL(string: appStoreURLString) {
                UIApplication.shared.open(appStoreURL, options: [:], completionHandler: nil)
            } else {
                print("Error: App Store URL is missing in Info.plist")
            }
        }
        self.isLinkHandled = true
    }
}

