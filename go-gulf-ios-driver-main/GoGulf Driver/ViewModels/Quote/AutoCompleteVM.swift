//
//  AutoCompleteVM.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/23/22.
//

import Foundation
import UIKit

class AutoCompleteVM: BaseObservableObject {
    @Published var responseData: AutoCompleteResponse?
    @Published var quote: QuoteRequest?
    
    @Published public var pickup = "Start"
    @Published public var via = "VIA"
    @Published public var dropoff = "Destination"
    @Published public var type = ""
    @Published public var address = ""
    @Published public var lat: Double = 0
    @Published public var lng: Double = 0
    @Published public var pLat: Double = 0
    @Published public var pLng: Double = 0
    @Published public var vLat: Double = 0
    @Published public var vLng: Double = 0
    @Published public var dLat: Double = 0
    @Published public var dLng: Double = 0
    
    @Published public var navigate = false
    @Published public var viaToggle = false
    @Published public var data = []
    @Published public var shouldOpenPlacePicker = false
    
}
