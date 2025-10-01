//
//  VehicleListVM.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/24/22.
//

import Foundation
import SwiftUI

//private var vehicleLists = [
//    VehicleModel(id: 1, type:"Luxury Berline", fleetClass: "EQS", fleetType: "Mercedes-Benz", image: "fleet1",  price: "211,70", distance: "211.70", duration: "2 hr 30 min", passengers: 2, luggage: 4),
//    VehicleModel(id: 2, type: "Limousine", fleetClass: "Maybach S Class",  fleetType: "Mercedes-Benz", image: "fleet2", price: "211,70", distance: "748.3", duration: "2 hr 30 min", passengers: 4, luggage: 2),
//    VehicleModel(id: 3, type: "Van", fleetClass: "EQV", fleetType: "Mercedes-Benz", image: "fleet3", price: "211,70", distance: "778.3", duration: "2 hr 30 min", passengers: 4, luggage: 2),
//]

class VehicleListVM: ObservableObject {
//    @Published var vehicleModel: [VehicleModel]?
    @Published public var selection = 0
    @Published public var showingAdvancedOptions = false
    @Published public var enableLogging = false
    @Published public var flight_name:String = ""
    @Published public var count = 0
    @Published public var plainColor: Color = .white
    @Published public var color = LinearGradient(gradient: Gradient(colors: [Color("Linear1"), Color("Linear2")]), startPoint: .top, endPoint: .bottom)
    
    init(){
//        print("-----------", vehicleLists, "vehicle modelllllll")
    }
    
}
