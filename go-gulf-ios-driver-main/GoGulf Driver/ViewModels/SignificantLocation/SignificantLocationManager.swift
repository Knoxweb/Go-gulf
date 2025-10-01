//
//  SignificantLocationManager.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 05/07/2023.
//

import SwiftUI
import CoreLocation
import Firebase
import FirebaseFirestore

class SignificantLocationManager: NSObject, CLLocationManagerDelegate {
    private let locationManager = CLLocationManager()
    let identity = UserDefaults.standard.string(forKey: "identity")
    private let debouncer = Debouncer(delay: 30)
    @State private var counter = 0
    @Published var currentLocation: CLLocation?
    var db = Firestore.firestore()
    override init() {
        super.init()
        
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestAlwaysAuthorization()
        locationManager.distanceFilter = 10.0
        locationManager.requestWhenInUseAuthorization()
        locationManager.allowsBackgroundLocationUpdates = true
        locationManager.showsBackgroundLocationIndicator = true
        locationManager.startMonitoringSignificantLocationChanges()
        startGeofencing()

    }
    
    func startGeofencing() {
          let geofenceRegion = CLCircularRegion(center: CLLocationCoordinate2D(latitude: 37.7749, longitude: -122.4194), radius: 100, identifier: "GeofenceRegion")
          geofenceRegion.notifyOnEntry = true
          geofenceRegion.notifyOnExit = false

          locationManager.startMonitoring(for: geofenceRegion)
      }

      func locationManager(_ manager: CLLocationManager, didEnterRegion region: CLRegion) {
          print("Entered geofence region: \(region.identifier)")
          self.updateCurrentLocation(coordinates: manager.location?.coordinate)
      }

      func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
          if let location = locations.last {
              print("Significant location change: \(location.coordinate)")
              self.updateCurrentLocation(coordinates: location.coordinate)
          }
      }
    
    
    func startUpdatingLocation() {
        if self.enableLocationServices() {
            locationManager.startUpdatingLocation()
        }
    }
    
    func stopUpdatingLocation() {
        locationManager.stopUpdatingLocation()
    }
    
    
    func updateCurrentLocation(coordinates: CLLocationCoordinate2D?) {
        let latitude = coordinates?.latitude ?? 0
        let longitude = coordinates?.longitude ?? 0
        print(latitude as Any, longitude as Any,  "<<<<<<<<< -------- from signifcant location")
        if latitude != 0 && longitude != 0 {
            self.updateCurrentLocation(lat: latitude, lng: longitude)
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
    
    func updateCurrentLocation(lat: Double, lng: Double){
        if(lat != 0 || lng != 0) {
            let json = CurrentLocationRequest(cur_lat: lat, cur_lng: lng)
//            NetworkManager.shared.post(urlString: "driver/current-coordinates", header: nil, encodingData: json) { (RESPONSE_DATA:CurrentLocationResponse?, URL_RESPONSE, ERROR) in
//                DispatchQueue.main.async {
//                    if(URL_RESPONSE?.statusCode == 200){
//                        let data = RESPONSE_DATA
//                        print(data?.title as Any, "<-----------User Location Updated from dashboard");
//                    }
//                    else{
//                    }
//                }
//            }
        }
    }
    
}
