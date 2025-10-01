//
//  GoogleMapsView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 10/30/21.
//

import SwiftUI
import GoogleMaps
import Firebase

struct GoogleMapsView: UIViewRepresentable {
    @State var driverMarkers: [DriverMarkers] = []
    @State var markerLists: [MarkersList] = []
    let marker: GMSMarker = GMSMarker()
    private var stopped = false
    let locationManager = LocationTracker.shared.locationManager
    private var driversListener: ListenerRegistration?
    let db = Firestore.firestore()
    @State var updatedMarkers = [DriverMarkers]()
    var userLatitude: Double {
        return locationManager.location?.coordinate.latitude ?? 0
    }
    
    var userLongitude: Double {
        return locationManager.location?.coordinate.longitude ?? 0
    }
    
    let camera = GMSCameraPosition.camera(
        withLatitude: -33.868820,
        longitude: 151.209290,
        zoom: 12
    )
    
    struct DriverMarkers {
        var id: Int?
        var lat: Double?
        var lng: Double?
        var marker: GMSMarker
    }
    
    struct MarkersList:Codable {
        let id: Int?
        let image: String?
        let lat: Double?
        let lng: Double?
        let name: String?
        let status: String?
    }
    
    func imageWithImage(image:UIImage, scaledToSize newSize:CGSize) -> UIImage{
        UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0)
        image.draw(in: CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height))
        let newImage:UIImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return newImage
    }
    
    /// Creates a `UIView` instance to be presented.
    func makeUIView(context: Self.Context) -> GMSMapView {
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        mapView.isMyLocationEnabled = true
        mapView.padding = UIEdgeInsets(top: 0, left: 0, bottom: 20, right: 5)
        mapView.settings.myLocationButton = true
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.settings.scrollGestures = true
        mapView.settings.zoomGestures = true
        mapView.settings.rotateGestures = true
        
        
        db.collection("nearby_drivers").addSnapshotListener { querySnapshot, error in
            guard let querySnapshot = querySnapshot else {
                if let error = error {
                    print("Error fetching documents: \(error)")
                }
                return
            }
            
            let currentKeys = Set(querySnapshot.documents.map { $0.documentID })
            
            self.driverMarkers.removeAll { marker in
                if !currentKeys.contains("\(marker.id ?? 0)") {
                    marker.marker.map = nil
                    return true
                }
                return false
            }
            
            for document in querySnapshot.documents {
                let documentData = document.data()
                let key = document.documentID
                let lat = documentData["lat"] as? Double ?? 0
                let lng = documentData["lng"] as? Double ?? 0

                let currentLocation = CLLocationCoordinate2D(latitude: lat, longitude: lng)
                
                if let isMarker = self.driverMarkers.first(where: { "\($0.id ?? 0)" == key }) {
                    let oldMarker = isMarker.marker
                    if oldMarker.position.latitude != currentLocation.latitude || oldMarker.position.longitude != currentLocation.longitude {
                        let carAnimator = BrandAnimator(carMarker: oldMarker, mapView: mapView)
                        carAnimator.animate(from: oldMarker.position, to: currentLocation)
                        oldMarker.position = currentLocation
                    }
                    
                } else {
                    let driverMarker = GMSMarker(position: currentLocation)
                    driverMarker.icon = UIImage(named: "vehicle")
                    driverMarker.map = mapView
                    self.driverMarkers.append(DriverMarkers(id: Int(key), lat: lat, lng: lng, marker: driverMarker))
                }
            }
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
        marker.position = CLLocationCoordinate2D(latitude: userLatitude, longitude: userLongitude)
        mapView.animate(toZoom: 16)
        mapView.animate(toLocation: CLLocationCoordinate2D(latitude: userLatitude, longitude: userLongitude))
    }
}

struct BrandAnimator {
    let carMarker: GMSMarker
    let mapView: GMSMapView
    
    func animate(from source: CLLocationCoordinate2D, to destination: CLLocationCoordinate2D) {
        // Keep Rotation Short
        //        print(source, destination, "animatetetteettetete-------------------->>>>>>>")
        CATransaction.begin()
        CATransaction.setAnimationDuration(1)
        CATransaction.setCompletionBlock({
            // you can do something here
        })
        
        // Rotate marker to the destination point
        carMarker.rotation = source.bearing(to: destination)
        carMarker.groundAnchor = CGPoint(x: CGFloat(0.5), y: CGFloat(0.5))
        CATransaction.commit()
        
        // Movement
        CATransaction.begin()
        CATransaction.setAnimationDuration(1)
        carMarker.position = destination
        
        // Center Map View Animate Camera View To the Target
//        let camera = GMSCameraUpdate.setTarget(destination)
//        mapView.animate(with: camera)
        
        CATransaction.commit()
    }
    
    func pauseLayer(layer: CALayer) {
        let pausedTime = layer.convertTime(CACurrentMediaTime(), from: nil)
        layer.speed = 0.0
        layer.timeOffset = pausedTime
    }
    
    func resumeLayer(layer: CALayer) {
        let pausedTime = layer.timeOffset
        layer.speed = 1.0
        layer.timeOffset = 0.0
        layer.beginTime = 0.0
        let timeSincePause = layer.convertTime(CACurrentMediaTime(), from: nil) - pausedTime
        layer.beginTime = timeSincePause
    }
}

struct GoogleMapsView_Previews: PreviewProvider {
    static var previews: some View {
        GoogleMapsView()
    }
}
