//
//  AccessLocationView.swift
//  SlyykDriver
//
//  Created by Office on 22/03/2023.
//


import SwiftUI
import CoreLocation

struct AccessLocationView: View {
    @State var navigateToDashboard = false
    @Environment(\.scenePhase) var scenePhase
    var locationManager = LocationTracker.shared.locationManager
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    
    var body: some View {
        VStack(alignment: .center, spacing: 15){
            Spacer()
            Image(systemName: "mappin.and.ellipse")
                .font(.system(size: 48))
                .foregroundColor(.green)
            Text("Location Permission Required")
                .font(.title2)
                .fontWeight(.bold)
            Text("Slyyk collects location data to track your location when your app is opened or in background. Please set your location access permission to 'ALWAYS' in setting")
                .font(.body)
                .foregroundColor(.white.opacity(0.7))
                .multilineTextAlignment(.center)
            VStack{
                Image("location-vector")
                    .padding(.top)
            }
            Spacer()
            Divider()
            Button(action: {self.openSetting()}){
                Text("Open Setting")
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color("AccentColor"))
                    .foregroundColor(Color("ThemeColor"))
                    .cornerRadius(10)
            }
            .padding(.top)
//            .navigationDestination(isPresented: self.$navigateToDashboard) {
//                DashboardUIView()
//            }
        }
        .padding()
        .background(Color.radialGradient.edgesIgnoringSafeArea(.all))
        .navigationBarBackButtonHidden(true)
        .navigationBarHidden(true)
        .onChange(of: scenePhase) { newPhase in
            if newPhase == .inactive {
                print("Inactive")
                return;
            } else if newPhase == .background {
                print("Background")
                return;
            }
            else{
                if self.enableLocationServices() {
                    appRootManager.currentRoot = .dashboardScreen
                    router.popToRoot()
                }
                print("location enabled", self.enableLocationServices())
                return;
            }
        }
        
    }
    func openSetting() {
            if let BUNDLE_IDENTIFIER = Bundle.main.bundleIdentifier,
               let url = URL(string: "\(UIApplication.openSettingsURLString)&path=LOCATION/\(BUNDLE_IDENTIFIER)") {
                DispatchQueue.main.async {
                    UIApplication.shared.open(url, options: [:], completionHandler: nil)
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
            print("Request when-in-use authorization initially")
        case .restricted, .denied:
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

struct AccessLocationView_Previews: PreviewProvider {
    static var previews: some View {
        AccessLocationView()
    }
}
