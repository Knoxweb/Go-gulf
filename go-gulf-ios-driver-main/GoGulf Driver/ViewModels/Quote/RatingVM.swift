//
//  RatingVM.swift
// SlyykDriverDriver
//
//  Created by Office on 18/07/2022.
//

import Foundation

class RatingVM: BaseObservableObject {
    @Published var ratings = 0
    @Published var message = ""
    @Published var navigateToDashboard = false
    @Published var showSuccess = false
    @Published var defaultResponse: DefaultResponse?
    
    @Published var locationManager = LocationTracker.shared.locationManager
    var userLatitude: Double {
        return locationManager.location?.coordinate.latitude ?? -33.868820
    }
    
    var userLongitude: Double {
        return locationManager.location?.coordinate.longitude ?? 151.209290
    }
    
//    let bookingId = UserDefaults.standard.string(forKey: "bookingId")
    
    
    func skipRating(id: String?, tabRouter: TabRouter) {
        self.loading = true
        let json = CoordinatesData(lat: userLatitude, lng: userLongitude)
        NetworkManager.shared.POST(to: APIEndpoints.skipRating(id ?? ""), body: json)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.appRoot?.currentRoot = .tabs
                    self.router?.popToRoot()
                    
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultResponse = response;
                
            }
            .store(in: &cancellables)
    }
    
    func submitRating(id: String?, tabRouter: TabRouter) {
        self.loading = true
        let json = RatingModel(rating: self.ratings, review: self.message, lat: userLatitude, lng: userLongitude)
        NetworkManager.shared.POST(to: APIEndpoints.jobReview(id ?? ""), body: json)
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.appRoot?.currentRoot = .tabs
                    self.router?.popToRoot()
                    
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert.toggle()
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultResponse = response;
                
            }
            .store(in: &cancellables)
    }
   
}

struct GeoLocationData: Codable {
    let coordinate: CoordinatesData?
    let zipcode: String?
}
struct CoordinatesData: Codable {
    let lat: Double?
    let lng: Double?
}

struct RatingModel: Codable {
    let rating: Int?
    let review: String?
    let lat: Double?
    let lng: Double?
}
