//
//  LocationTracker.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 21/07/2022.
//


import Foundation
import CoreLocation

typealias LocateMeCallback = (_ location: CLLocation?) -> Void

/*
 LocationTracker to track the user in while navigating from one place to other and store new locations in locations array.
 **/
class LocationTracker: NSObject {
    
    static let shared = LocationTracker()
    
    var lastLocation: CLLocation?
    var locations: [CLLocation] = []
    var previousLocation: CLLocation?
    let manager = CLLocationManager()

    var locationManager: CLLocationManager = {
       let locationManager = CLLocationManager()
       locationManager.activityType = .automotiveNavigation
       locationManager.desiredAccuracy = kCLLocationAccuracyBest
       locationManager.distanceFilter = 5
        
        locationManager.startUpdatingLocation()
        locationManager.activityType = .automotiveNavigation
        locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
        locationManager.distanceFilter = 1
       return locationManager
    }()
    
    
    
    var locateMeCallback: LocateMeCallback?
   
    var isCurrentLocationAvailable: Bool {
        if lastLocation != nil, lastLocation!.timestamp.timeIntervalSinceNow < 10 {
          return true
        }
        return false
    }
    
    func enableLocationServices() {
        locationManager.delegate = self
        
        
        let authorizationStatus: CLAuthorizationStatus

        if #available(iOS 14, *) {
            authorizationStatus = manager.authorizationStatus
        } else {
            authorizationStatus = CLLocationManager.authorizationStatus()
        }
        switch authorizationStatus {
        case .notDetermined:
            // Request when-in-use authorization initially
            locationManager.requestWhenInUseAuthorization()
        case .restricted, .denied:
            // Disable location features
            print("Fail permission to get current location of user")
        case .authorizedWhenInUse:
            // Enable basic location features
            print("authorizedWhenInUse")
//            enableMyWhenInUseFeatures()
       case .authorizedAlways:
            // Enable any of your app's location features
            print("location background track")
//            enableMyAlwaysFeatures()
        @unknown default:
            break
        }
    }
    
    func enableMyWhenInUseFeatures() {
       locationManager.startUpdatingLocation()
       locationManager.delegate = self
        
       escalateLocationServiceAuthorization()
    }
    
    func escalateLocationServiceAuthorization() {
        let authorizationStatus: CLAuthorizationStatus

        if #available(iOS 14, *) {
            authorizationStatus = manager.authorizationStatus
        } else {
            authorizationStatus = CLLocationManager.authorizationStatus()
        }
        // Escalate only when the authorization is set to when-in-use
        if authorizationStatus == .authorizedWhenInUse {
            locationManager.requestAlwaysAuthorization()
        }
    }
    
    func enableMyAlwaysFeatures() {
       locationManager.allowsBackgroundLocationUpdates = true
       locationManager.pausesLocationUpdatesAutomatically = true
       locationManager.startUpdatingLocation()
       locationManager.delegate = self
    }
    
    func locateMeOnLocationChange(callback: @escaping LocateMeCallback) {
        self.locateMeCallback = callback
        if lastLocation == nil {
            enableLocationServices()
        } else {
           callback(lastLocation)
        }
    }
    
    func startTracking() {
         enableLocationServices()
    }
    
    func stopTracking() {
        
    }
    private override init() {}
}

// MARK: - CLLocationManagerDelegate
extension LocationTracker: CLLocationManagerDelegate {
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        print(locations)
        
        guard let location = locations.first else { return }
        
        guard location.timestamp.timeIntervalSinceNow < 10 || location.horizontalAccuracy > 0 else {
            print("invalid location received")
            return
        }
        
        self.locations.append(location)
        previousLocation = lastLocation
        lastLocation = location
        
        print("location = \(location.coordinate.latitude) \(location.coordinate.longitude)")
        locateMeCallback?(location)
        
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error.localizedDescription)
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        enableLocationServices()
    }
}

