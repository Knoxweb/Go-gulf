//
//  GoogleMapsRoutes.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 15/07/2022.
//

import SwiftUI
import GoogleMaps
import CoreLocation
import Alamofire
//import SwiftyJSON
import FirebaseDatabase


struct GoogleMapsRoutes: UIViewRepresentable {
    let fromLat: Double?
    let fromLng: Double?
    let toLat: Double?
    let toLng: Double?
    let route: Route?
    
    @State var polyline = GMSPolyline()
    
    let marker: GMSMarker = GMSMarker()
    
    let camera = GMSCameraPosition.camera(
        withLatitude: -33.868820,
        longitude: 151.209290,
        zoom: 8
    )
    
    /// Creates a `UIView` instance to be presented.
    func makeUIView(context: Self.Context) -> GMSMapView {
        
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        mapView.padding = UIEdgeInsets(top: 0, left: 0, bottom: 30, right: 5)
        mapView.settings.myLocationButton = false
        mapView.isMyLocationEnabled = true
        mapView.settings.scrollGestures = true
        mapView.settings.zoomGestures = true
        mapView.settings.rotateGestures = true
        mapView.animate(toLocation: CLLocationCoordinate2D(latitude: fromLat ?? 0, longitude: fromLng ?? 0))
        
        
        let start = GMSMarker();
        let end = GMSMarker();
        
            
       
        DispatchQueue.main.async {
            
            let southBound = CLLocationCoordinate2D(latitude: self.route?.bounds?.southwest?.lat ?? 0, longitude: self.route?.bounds?.southwest?.lng ?? 0)
            let northBound = CLLocationCoordinate2D(latitude: self.route?.bounds?.northeast?.lat ?? 0, longitude: self.route?.bounds?.northeast?.lng ?? 0)
            let overlayBounds = GMSCoordinateBounds(coordinate: southBound, coordinate: northBound)
            let cameraUpdate = GMSCameraUpdate.fit(overlayBounds)
            mapView.animate(with: cameraUpdate)
            
            //                           Gradient Stroke Fill Color
            let points = self.route?.overviewPolyline?.points
            let path = GMSPath.init(fromEncodedPath: points ?? "")
            self.polyline = GMSPolyline(path: path)
            
            let gradientBlue = GMSStrokeStyle.gradient(from: #colorLiteral(red: 0.05882352941, green: 0.8470588235, blue: 0.5882352941, alpha: 1), to: #colorLiteral(red: 0.4666666667, green: 0.8196078431, blue: 0.168627451, alpha: 1))
            self.polyline.spans = [GMSStyleSpan(style: gradientBlue)]
            
            //                            Set Polyline to Map
            self.polyline.strokeColor = UIColor.routeColor
            self.polyline.strokeWidth = 6.0
            self.polyline.map = mapView
            
            CATransaction.commit()
            
            
            //                        Start Marker
            start.position = CLLocationCoordinate2D(latitude: fromLat ?? 0, longitude: fromLng ?? 0)
            start.icon = UIImage(named: "start")
            start.appearAnimation = .pop
            start.groundAnchor = CGPoint(x: 0.5, y: 0.5)
            start.map = mapView
            
            //                       End marker
            end.position = CLLocationCoordinate2D(latitude: self.toLat ?? 0, longitude: self.toLng ?? 0)
            end.icon = UIImage(named: "end")
            end.appearAnimation = .pop
            end.groundAnchor = CGPoint(x: 0.5, y: 0.5)
            end.map = mapView;
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
    
}
