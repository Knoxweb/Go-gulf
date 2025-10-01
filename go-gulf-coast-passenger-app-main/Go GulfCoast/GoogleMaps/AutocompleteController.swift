//
//  AutocompleteController.swift
//   GoGulf
//
//  Created by Mac on 6/23/22.
//

import Foundation
import UIKit
import SwiftUI
import GooglePlaces
import CoreLocation
import GoogleMaps
import MapKit


struct PlacePicker: UIViewControllerRepresentable {
    @StateObject var viewModel = AutoCompleteVM()
    typealias UIViewControllerType = GMSAutocompleteViewController
    
    var locationManager = LocationTracker.shared.locationManager
    
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    @Environment(\.presentationMode) var presentationMode
    @Binding public var type: String
    @Binding public var pickup: String
    @Binding public var via: String
    @Binding public var dropoff: String
    @Binding public var pLat: Double
    @Binding public var pLng: Double
    @Binding public var vLat: Double
    @Binding public var vLng: Double
    @Binding public var dLat: Double
    @Binding public var dLng: Double
    
    func makeUIViewController(context: UIViewControllerRepresentableContext<PlacePicker>) -> GMSAutocompleteViewController {
        
        let autocompleteController = GMSAutocompleteViewController()
      
        
        autocompleteController.delegate = context.coordinator
        
        // Set the autocomplete bounds to include the user's current location
           if let userLocation = locationManager.location {
               let filter = GMSAutocompleteFilter()
               filter.type = .noFilter
               autocompleteController.autocompleteFilter = filter
               filter.countries = ["au", "np"]
               let northEast = CLLocationCoordinate2D(latitude: userLocation.coordinate.latitude + 0.001, longitude: userLocation.coordinate.longitude + 0.001)
               let southWest = CLLocationCoordinate2D(latitude: userLocation.coordinate.latitude - 0.001, longitude: userLocation.coordinate.longitude - 0.001)
               
               let distance = 1000
               let region = MKCoordinateRegion(center: userLocation.coordinate, latitudinalMeters: CLLocationDistance(distance), longitudinalMeters: CLLocationDistance(distance))

               let locationBias = GMSPlaceRectangularLocationOption(
                region.center,
                region.center
               )
               filter.locationBias = locationBias
           }
        
        return autocompleteController
        
    }
    
    func updateUIViewController(_ uiViewController: GMSAutocompleteViewController, context: UIViewControllerRepresentableContext<PlacePicker>) {
    }
    
    class Coordinator: NSObject, UINavigationControllerDelegate, GMSAutocompleteViewControllerDelegate {
        
        var parent: PlacePicker
        
        init(_ parent: PlacePicker) {
            self.parent = parent
        }
        
        
        func viewController(_ viewController: GMSAutocompleteViewController, didAutocompleteWith place: GMSPlace) {
            if let placeID = place.placeID {
                           GMSPlacesClient.shared().lookUpPlaceID(placeID) { place, error in
                               if let place = place {
                                   print(place, "asdfasdfasdfsdafsdafsdf sdfasfasdfasdfasdfsdfds")
                                   
                                   switch self.parent.type {
                                   case "pickup" :
                                       self.parent.pickup = checkRepeatText(name: place.name ?? "", addr: place.formattedAddress ?? "");
                                       self.parent.pLat = place.coordinate.latitude;
                                       self.parent.pLng = place.coordinate.longitude
                                       print(self.parent.pickup, "printnntntntntntntntnntnt")
                                       break;
                                   case "via" :
                                       self.parent.via = checkRepeatText(name: place.name ?? "", addr: place.formattedAddress ?? "");
                                       self.parent.vLat = place.coordinate.latitude
                                       self.parent.vLng = place.coordinate.longitude
                                       break;
                                   default:
                                       self.parent.dropoff = checkRepeatText(name: place.name ?? "", addr: place.formattedAddress ?? "");
                                       self.parent.dLat = place.coordinate.latitude
                                       self.parent.dLng = place.coordinate.longitude
                                       break
                                   }
                                   self.parent.presentationMode.wrappedValue.dismiss()
                               }
                           }
                           viewController.dismiss(animated: true, completion: nil)
                       }
                    
            
            return
//            DispatchQueue.main.async {
//                let url = URL(string: "https://maps.googleapis.com/maps/api/place/details/json?placeid=\(place.placeID ?? "placeiddddd")&key=\(Env.websAPIKey)")!
//                let task = URLSession.shared.dataTask(with: url) {(data, response, error) in
//                    guard let data = data else { return }
//                    if let response = try? JSONDecoder().decode(AutoCompleteResponse.self, from: data) {
//                        DispatchQueue.main.async { [self] in
//
//                            switch self.parent.type {
//                            case "pickup" :
//                                self.parent.pickup = response.result?.formattedAddress ?? "";
//                                self.parent.pLat = response.result?.geometry.location.lat ?? 0;
//                                self.parent.pLng = response.result?.geometry.location.lng ?? 0;
//                                break;
//                            case "via" :
//                                self.parent.via = response.result?.formattedAddress ?? "";
//                                self.parent.vLat = response.result?.geometry.location.lat ?? 0;
//                                self.parent.vLng = response.result?.geometry.location.lng ?? 0;
//                                break;
//                            default:
//                                self.parent.dropoff = response.result?.formattedAddress ?? "";
//                                self.parent.dLat = response.result?.geometry.location.lat ?? 0;
//                                self.parent.dLng = response.result?.geometry.location.lng ?? 0;
//                                break
//                            }
//                            self.parent.presentationMode.wrappedValue.dismiss()
//                        }
//                    }
//                }
//                task.resume()
//
//            }
        }
        
        
        func viewController(_ viewController: GMSAutocompleteViewController, didFailAutocompleteWithError error: Error) {
            // TODO: handle the error.
            let alertController = GlobalAlertController(title: "Error", message: error.localizedDescription, preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Ok", style: .destructive, handler: nil))
        }
        
        func wasCancelled(_ viewController: GMSAutocompleteViewController) {
            parent.presentationMode.wrappedValue.dismiss()
        }
    }
    
}


func checkRepeatText(name: String, addr: String) -> String {
    var formattedAddr = "\(name) \(addr)"
    if addr.contains(name){
        formattedAddr = addr
    }
    return formattedAddr
}
