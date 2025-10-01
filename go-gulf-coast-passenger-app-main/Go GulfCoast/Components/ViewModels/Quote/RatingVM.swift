//
//  RatingVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 18/07/2022.
//

import Foundation
import UIKit

class RatingVM: BaseObservableObject {
    
    @Published var bookingResponse: BookingLess?
    @Published var ratings = 0
    @Published var message = ""
    
    @Published var navigateToDashboard = false
    @Published var showSuccess = false
    @Published var defaultResponse: DefaultResponse?
//    @Published var tips = ["", "2", "3", "5", "10", "20"]
    @Published var selectedTip: Int? = nil
    @Published var selection = ""
    @Published var tip = ""
    @Published public var amount = ""
    @Published var locationManager = LocationTracker.shared.locationManager
    var userLatitude: Double {
        return locationManager.location?.coordinate.latitude ?? -33.868820
    }
    
    var userLongitude: Double {
        return locationManager.location?.coordinate.longitude ?? 151.209290
    }
    
    
    func skipRating(id: String?, router: Router, appRoot: AppRootManager, tabRouter: TabRouter) {
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
    
    
    func submitRating(id: String?, router: Router, appRoot: AppRootManager, tabRouter: TabRouter) {
        
//        self.shouldDismissView = true
        
        self.loading = true
        let json = RatingModel(rating: self.ratings, review: self.message, lat: userLatitude, lng: userLongitude, tip: Double("\(tip)") ?? 0)
        
        NetworkManager.shared.POST(to: APIEndpoints.bookingReview(id ?? ""), body: json)
            .sink { completion in
                self.loading = false
                
                switch completion {
                case .finished:
                    self.appRoot?.currentRoot = .tabs
                    self.router?.popToRoot()
                    
                case .failure(let error):
//                    appRoot.currentRoot = .bookNowScreen
//                    router.popToRoot()
//                    self.shouldDismissView = true
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultResponse = response;
                
            }
            .store(in: &cancellables)
    }
}



struct RatingModel: Codable {
    let rating: Int?
    let review: String?
    let lat: Double?
    let lng: Double?
    let tip: Double?
}

