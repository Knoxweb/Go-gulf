//
//  GoogleMapPlotRoute.swift
//  CSD Chauffeur
//
//  Created by Prabin Phasikawo on 14/05/2024.
//

import Foundation
import SwiftUI
import GoogleMaps
import CoreLocation
import Combine
import MapKit
import Alamofire
import FirebaseFirestore

struct GoogleMapPlotRoute: UIViewRepresentable {
    
    @State var loadMap = false
    
    @State var fromLat: Double
    @State var fromLng: Double
    @State var toLat: Double
    @State var toLng: Double
    @State var route: Route?
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    var currentRideListener: ListenerRegistration?
    var locationManager = LocationTracker.shared.locationManager
    @State var currentRides: [CurrentBookingModel]?
    @State var currentData: CurrentBookingModel?
    @State var driverPosition: DriverPositionResponse?
    @State private var cancellable: AnyCancellable?
    @Environment(\.colorScheme) var colorScheme
    @State private var lastAppliedColorScheme: ColorScheme?
    
    
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    @State var polyline = GMSPolyline()
    @State var startMarker: GMSMarker = GMSMarker()
    @State var viaMarker: GMSMarker = GMSMarker()
    @State var endMarker: GMSMarker = GMSMarker()
    @State var driver = GMSMarker()
    let mapView = GMSMapView()
    
    @State  var start = GMSMarker();
    @State  var end = GMSMarker();
    
    
    let camera = GMSCameraPosition.camera(
        withLatitude: -33.868820,
        longitude: 151.209290,
        zoom: 16
    )
    
    /// Creates a `UIView` instance to be presented.
    func makeUIView(context: Self.Context) -> GMSMapView {
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        
        mapView.settings.scrollGestures = true
        mapView.isMyLocationEnabled = false
        mapView.padding = UIEdgeInsets(top: 0, left: 0, bottom: 20, right: 5)
        mapView.settings.zoomGestures = true
        mapView.settings.rotateGestures = true
        mapView.settings.compassButton = true
        mapView.settings.scrollGestures = true
        mapView.settings.zoomGestures = true
        mapView.settings.rotateGestures = true
        
        mapView.animate(toLocation: CLLocationCoordinate2D(latitude: fromLat, longitude: fromLng))
        CATransaction.begin()
        CATransaction.setAnimationDuration(0.0)
        CATransaction.commit()
        var _: GMSPolyline?
        var _: GMSMarker!
      
        
        DispatchQueue.main.async {
            
            let profileDocument = parentCollection.collection("data").document("current_booking")
            
            profileDocument.addSnapshotListener {  documentSnapshot, error in
//                guard let self = self else { return }
                if let error = error {
                    print("Error fetching profile document: \(error)")
                    return
                }
                guard let document = documentSnapshot else {
                    print("Profile document does not exist")
                    return
                }
                do {
                    guard document.exists else {
                        throw NSError(domain: "Firestore", code: -1, userInfo: [NSLocalizedDescriptionKey: "Document does not exist"])
                    }
                    let data = try JSONSerialization.data(withJSONObject: document.data() ?? [:], options: [])
                    self.currentData = try JSONDecoder().decode(CurrentBookingModel.self, from: data)
                    self.fromLat = self.currentData?.pickup?.lat ?? 0
                    self.fromLng = self.currentData?.pickup?.lng ?? 0
                    self.toLat = self.currentData?.drop?.lat ?? 0
                    self.toLng = self.currentData?.drop?.lng ?? 0
                    self.route = self.currentData?.route
                    plotOnMap(mapView)
                } catch {
                    print("Error decoding profile document: \(error)")
                }
            }
            
            
            parentCollection.collection("data").document("driver_position").addSnapshotListener { documentSnapshot, error in
                if let error = error {
                    print("Error fetching profile document: \(error)")
                    return
                }
                guard let document = documentSnapshot else {
                    print("Profile document does not exist")
                    return
                }
                do {
                    guard document.exists else {
                        throw NSError(domain: "Firestore", code: -1, userInfo: [NSLocalizedDescriptionKey: "Document does not exist"])
                    }
                    let data = try JSONSerialization.data(withJSONObject: document.data() ?? [:], options: [])
                    self.driverPosition = try JSONDecoder().decode(DriverPositionResponse.self, from: data)
                    
                    let currentLocation = CLLocationCoordinate2D(latitude: self.driverPosition?.lat ?? 0, longitude: self.driverPosition?.lng ?? 0)
                    let previousLocation = driver.position
                    
                    DispatchQueue.main.async {
                        if previousLocation.latitude != currentLocation.latitude || previousLocation.longitude != currentLocation.longitude {
                            let carAnimator = CarAnimator(carMarker: driver, mapView: mapView)
                            carAnimator.animate(from: previousLocation, to: currentLocation)
                        }
                        
                        driver.position = currentLocation
                        driver.icon = UIImage(named: "vehicle")
//                        driver.icon = imageWithImage(image: UIImage(named: "vehicle")!, scaledToSize: CGSize(width: 25.0, height: 40.0))
                        driver.appearAnimation = .pop
                        driver.groundAnchor = CGPoint(x: 0.5, y: 0.5)
                        driver.map = mapView
                    }
                } catch {
                    print("Error decoding profile document: \(error)")
                }
            }
            driver.map = nil
        }
        do {
            if let styleURL = Bundle.main.url(forResource: "style", withExtension: "json") {
                mapView.mapStyle = try GMSMapStyle(contentsOfFileURL: styleURL)
            } else {
                NSLog("Unable to find style.json")
            }
        } catch {
            NSLog("One or more of the map styles failed to load. \(error)")
        }
        return mapView
    }
    
    func updateUIView(_ mapView: GMSMapView, context: Self.Context) {
        
    }
    

    func plotOnMap(_ mapView: GMSMapView) {
        if toLat != 0 {
            let points = self.route?.overviewPolyline?.points
            let path = GMSPath.init(fromEncodedPath: points ?? "")
            self.polyline = GMSPolyline(path: path)
            let overlayBounds = GMSCoordinateBounds(coordinate: CLLocationCoordinate2D(latitude: fromLat, longitude: fromLng), coordinate: CLLocationCoordinate2D(latitude: toLat, longitude: toLng))
            let cameraUpdate = GMSCameraUpdate.fit(overlayBounds)
            mapView.animate(with: cameraUpdate)
            
            let gradientBlue = GMSStrokeStyle.gradient(from: #colorLiteral(red: 0.05882352941, green: 0.8470588235, blue: 0.5882352941, alpha: 1), to: #colorLiteral(red: 0.4666666667, green: 0.8196078431, blue: 0.168627451, alpha: 1))
            self.polyline.spans = [GMSStyleSpan(style: gradientBlue)]
            
            //                            Set Polyline to Map
            self.polyline.strokeColor = UIColor.routeColor
            self.polyline.strokeWidth = 6.0
            self.polyline.map = mapView
            
            
            CATransaction.commit()
        }
        
        start.position = CLLocationCoordinate2D(latitude: fromLat, longitude: fromLng)
        start.icon = UIImage(named: "start")
        start.appearAnimation = .pop
        start.groundAnchor = CGPoint(x: 0.5, y: 0.5)
        start.map = mapView
        
        
        end.position = CLLocationCoordinate2D(latitude: self.toLat, longitude: self.toLng)
        end.icon = UIImage(named: "end")
        end.appearAnimation = .pop
        end.groundAnchor = CGPoint(x: 0.5, y: 0.5)
        end.map = mapView;
    }
}

func imageWithImage(image:UIImage, scaledToSize newSize:CGSize) -> UIImage{
    UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0)
    image.draw(in: CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height))
    let newImage:UIImage = UIGraphicsGetImageFromCurrentImageContext()!
    UIGraphicsEndImageContext()
    return newImage
}


struct DriverPositionResponse: Codable {
    let distance: String?
    let lat: Double?
    let lng: Double?
    let status: String?
}
