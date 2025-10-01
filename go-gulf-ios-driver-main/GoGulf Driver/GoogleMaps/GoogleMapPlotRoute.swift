
import Foundation
import SwiftUI
import GoogleMaps
import CoreLocation
import MapKit
import Alamofire
import Combine


struct GoogleMapPlotRoute: UIViewRepresentable {
    
    @State var loadMap = false
    
    @State var fromLat: Double
    @State var fromLng: Double
    @State var toLat: Double
    @State var toLng: Double
    @State var route: Route?
    @State private var cancellable: AnyCancellable?
    var locationManager = LocationTracker.shared.locationManager
    
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    @Environment(\.colorScheme) var colorScheme
    @State private var lastAppliedColorScheme: ColorScheme?
    
    var stopped = false
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    @State var polyline = GMSPolyline()
    @State var startMarker: GMSMarker = GMSMarker()
    @State var viaMarker: GMSMarker = GMSMarker()
    @State var endMarker: GMSMarker = GMSMarker()
    
    @State  var start = GMSMarker();
    @State  var end = GMSMarker();
    
    let mapView = GMSMapView()
    
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
        
        var myLocationMarker: GMSMarker!
        
        CATransaction.begin()
        CATransaction.setAnimationDuration(0.0)
        CATransaction.commit()
        var _: GMSPolyline?
        var _: GMSMarker!
        
        DispatchQueue.main.async {
            if toLat != 0 {
                let points = self.route?.overviewPolyline?.points
                let path = GMSPath.init(fromEncodedPath: points ?? "")
                self.polyline = GMSPolyline(path: path)
                let overlayBounds = GMSCoordinateBounds(coordinate: CLLocationCoordinate2D(latitude: fromLat, longitude: fromLng), coordinate: CLLocationCoordinate2D(latitude: toLat, longitude: toLng))
//                print(fromLat, fromLng, toLat, toLng,  points, "sdfasfsadfadsfsadfasdfadsd")
                let cameraUpdate = GMSCameraUpdate.fit(overlayBounds)
                mapView.animate(with: cameraUpdate)
                
                polyline.strokeWidth = 4.0
                polyline.strokeColor = UIColor(Color("AccentColor"))
                polyline.map = mapView
                
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
            
            
                LocationTracker.shared.locateMeOnLocationChange { [self]  _ in
                    print(LocationTracker.shared.lastLocation as Any, "lasst location---------------")
                    if let myLocation = LocationTracker.shared.lastLocation,
                       myLocationMarker == nil {
                        myLocationMarker = GMSMarker(position: myLocation.coordinate)
                        myLocationMarker.icon = UIImage(named: "vehicle")
                        myLocationMarker.map = mapView
                        mapView.updateMap(toLocation: myLocation, zoomLevel: 16)
                        
                    } else if let myLocation = LocationTracker.shared.lastLocation?.coordinate, let myLastLocation = LocationTracker.shared.previousLocation?.coordinate {
                        let carAnimator = CarAnimator(carMarker: myLocationMarker, mapView: mapView)
                        if !stopped {
                            carAnimator.animate(from: myLastLocation, to: myLocation)
                        }
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
        
    }
}


func imageWithImage(image:UIImage, scaledToSize newSize:CGSize) -> UIImage{
    UIGraphicsBeginImageContextWithOptions(newSize, false, 0.0)
    image.draw(in: CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height))
    let newImage:UIImage = UIGraphicsGetImageFromCurrentImageContext()!
    UIGraphicsEndImageContext()
    return newImage
}
