//
//  LocationTracker.swift
//  SlyykDriver
//
//  Created by Office on 28/12/2022.
//


import Foundation
import CoreLocation
import UIKit

typealias LocateMeCallback = (_ location: CLLocation?) -> Void
class LocationTracker: NSObject {
    
    static let shared = LocationTracker()
    
    var lastLocation: CLLocation?
    var locations: [CLLocation] = []
    var previousLocation: CLLocation?
    
    var locationManager: CLLocationManager = {
        let locationManager = CLLocationManager()
        locationManager.activityType = .automotiveNavigation
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.distanceFilter = 5
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
        let authorizationStatus: CLAuthorizationStatus
           if #available(iOS 14, *) {
               authorizationStatus = locationManager.authorizationStatus
           } else {
               authorizationStatus = CLLocationManager.authorizationStatus()
           }
        switch authorizationStatus {
        case .notDetermined:
            // Request when-in-use authorization initially
            locationManager.requestWhenInUseAuthorization()
            print("Request when-in-use authorization initially")
//            openSetting()
        case .restricted, .denied:
            // Disable location features
//            openSetting()
            print("Fail permission to get current location of user Disable location features")
        case .authorizedWhenInUse:
            // Enable basic location features
            enableMyWhenInUseFeatures()
            print("Enable basic location features when in use")
//            openSetting()
        case .authorizedAlways:
            // Enable any of your app's location features
            print("Alwaysssss location features")
            enableMyAlwaysFeatures()
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
        // Escalate only when the authorization is set to when-in-use
        let authorizationStatus: CLAuthorizationStatus
           if #available(iOS 14, *) {
               authorizationStatus = locationManager.authorizationStatus
           } else {
               authorizationStatus = CLLocationManager.authorizationStatus()
           }
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
//            enableLocationServices()
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

extension LocationTracker: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
//        print(locations)
        
        guard let location = locations.first else { return }
        
        guard location.timestamp.timeIntervalSinceNow < 10 || location.horizontalAccuracy > 0 else {
            print("invalid location received")
            return
        }
        
        self.locations.append(location)
        previousLocation = lastLocation
        lastLocation = location
        locateMeCallback?(location)
        
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print(error.localizedDescription)
    }
    
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        enableLocationServices()
    }
}

