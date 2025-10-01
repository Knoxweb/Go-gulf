//
//  ShortcutAutocomplete.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 24/07/2022.
//


import Foundation
import UIKit
import SwiftUI
import GooglePlaces
import CoreLocation
import MapKit


struct AddressPicker: UIViewControllerRepresentable {
    @StateObject var viewModel = AutoCompleteVM()
    
    var locationManager = LocationTracker.shared.locationManager
    
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    
    typealias UIViewControllerType = GMSAutocompleteViewController
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    @Environment(\.presentationMode) var presentationMode
    @Binding public var address: String
    @Binding public var lat: Double
    @Binding public var lng: Double
    
    func makeUIViewController(context: UIViewControllerRepresentableContext<AddressPicker>) -> GMSAutocompleteViewController {
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
    
    func updateUIViewController(_ uiViewController: GMSAutocompleteViewController, context: UIViewControllerRepresentableContext<AddressPicker>) {
    }
    
    class Coordinator: NSObject, UINavigationControllerDelegate, GMSAutocompleteViewControllerDelegate {
        
        var parent: AddressPicker
        
        init(_ parent: AddressPicker) {
            self.parent = parent
        }
        
        
        func viewController(_ viewController: GMSAutocompleteViewController, didAutocompleteWith place: GMSPlace) {
            if let placeID = place.placeID {
                GMSPlacesClient.shared().lookUpPlaceID(placeID) { place, error in
                    if let place = place {
                        DispatchQueue.main.async { [self] in
                            self.parent.address = checkRepeatText(name: place.name ?? "", addr: place.formattedAddress ?? "");
                            self.parent.lat = place.coordinate.latitude
                            self.parent.lng = place.coordinate.longitude
                            self.parent.presentationMode.wrappedValue.dismiss()
                        }
                    }
                }
            }
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
