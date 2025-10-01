//
//  GoogleMapsView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 10/30/21.
//

import SwiftUI
import GoogleMaps

struct GoogleMapsView: UIViewRepresentable {
    
    let marker: GMSMarker = GMSMarker()
    var locationManager = LocationManager()
    
    var userLatitude: Double {
        return locationManager.lastLocation?.coordinate.latitude ?? 0
    }
    
    var userLongitude: Double {
        return locationManager.lastLocation?.coordinate.longitude ?? 0
    }
    
    private let zoom: Float = 15.0
    
    /// Creates a `UIView` instance to be presented.
    func makeUIView(context: Self.Context) -> GMSMapView {
        let camera = GMSCameraPosition.camera(withLatitude: userLatitude, longitude: userLongitude, zoom: zoom)
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        mapView.settings.compassButton = true
        mapView.isMyLocationEnabled = true
        mapView.settings.myLocationButton = true
        mapView.padding = UIEdgeInsets(top: 0, left: 0, bottom: 20, right: 5)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.settings.scrollGestures = true
        mapView.settings.zoomGestures = true
        mapView.settings.rotateGestures = true
        mapView.camera = camera
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
        if((locationManager.lastLocation?.coordinate) != nil){
            marker.position = CLLocationCoordinate2D(latitude: userLatitude, longitude: userLongitude)
            mapView.animate(toLocation: CLLocationCoordinate2D(latitude: userLatitude, longitude: userLongitude))
        }
       
    }
    
}

struct GoogleMapsView_Previews: PreviewProvider {
    static var previews: some View {
        GoogleMapsView()
    }
}
