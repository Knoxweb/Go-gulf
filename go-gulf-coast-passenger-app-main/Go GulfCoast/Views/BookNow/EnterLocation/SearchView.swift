//
//  SearchView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 18/09/2024.
//

import Foundation
import SwiftUI
import GooglePlaces

struct SearchView: View {
    @State private var isSearching = false
    @State private var showCloseIcon = false
    @Environment(\.presentationMode) var presentationMode
    @State private var searchResults: [GMSAutocompletePrediction] = []
    @State var predictions: [Prediction] = []
    @State var loading = false
    @State var isLoading = false
    @FocusState private var nameInFocus: Bool
    @State var show = false
    @Binding var type: String
    @Binding var lat: Double
    @Binding var lng: Double
    @Binding var address:String
    @State var latitude: Double = 0
    @State var longitude: Double = 0
    @State var searchField = ""
    @State var showAutoComplete = true
    @State var placeholderText = "Pickup"
//    @StateObject var VM: AddressVM = AddressVM()
    let locationManager = LocationTracker.shared.locationManager
    var userLatitude: Double {
        return locationManager.location?.coordinate.latitude ?? 0
    }
    var userLongitude: Double {
        return locationManager.location?.coordinate.longitude ?? 0
    }
    @State var addressSheet = false
    
    init(type: Binding<String>, lat: Binding<Double>, lng: Binding<Double>, address: Binding<String>) {
        _type = type
        _lat = lat
        _lng = lng
        _address = address
        UINavigationBar.customizeBackButton()
    }
    
    var body: some View {
        VStack (spacing: 0){
            ScrollView {
                VStack {
                    VStack (spacing: 30){
                        Button(action: {
                            self.lat = userLatitude
                            self.lng = userLongitude
                            self.isLoading = true
                            showAutoComplete = false
                            getAddressFromCoordinates(location: CLLocationCoordinate2D(latitude: userLatitude, longitude: userLongitude)) { address in
                                if let address = address {
                                    self.address = address
                                    self.isLoading = false
                                    self.presentationMode.wrappedValue.dismiss()
                                } else {
                                    print("Failed to fetch address")
                                }
                            }
                        })
                        {
                            FullWidthListWithIcon(icon: .pickupOutlineIcon, localizedTitle: "Current Location", secondaryIcon: .chevronRight)
                        }
                    }
                }
                .padding(.horizontal, 22)
                .padding(.bottom)
                .padding(.top)
                Divider()
                    .frame(height: 1.08)
                    .overlay(.gray.opacity(0.2))

                
                VStack (spacing: 20){
                    if !loading {
                        if predictions.count > 0  {
                            ForEach(Array((predictions.enumerated())), id: \.offset) { index, prediction in
                                Button(action: {
                                    self.isLoading = true
                                    if let placeID = prediction.placeID {
                                        getCoordinatesFromPlaceID(placeID: placeID) { coordinate in
                                            if let coordinate = coordinate {
                                                self.lat = coordinate.coordinate?.lat ?? 0
                                                self.lng = coordinate.coordinate?.lng ?? 0
                                                print("Latitude: \(coordinate.coordinate?.lat ?? 0), Longitude: \(coordinate.coordinate?.lng ?? 0)")
//                                                self.address = "\(coordinate.zipcode ?? "") \(prediction.structuredFormatting?.mainText ?? "") \(prediction.structuredFormatting?.secondaryText ?? "")"
                                                self.address = "\(prediction.structuredFormatting?.mainText ?? "") \(prediction.structuredFormatting?.secondaryText ?? "")"
                                                self.isLoading = false
                                                self.presentationMode.wrappedValue.dismiss()
                                            } else {
                                                print("Failed to fetch coordinates")
                                            }
                                        }
                                    }
                                    
                                }) {
                                    FullWidthListWithIcon(
                                        icon: .markerIcon,
                                        title: "\(prediction.structuredFormatting?.mainText ?? "")",
                                        caption: "\(prediction.structuredFormatting?.secondaryText ?? "")",
                                        iconColor: Color.accentColor,
                                        secondaryIcon: .chevronRight
                                    )
                                }
                            }
                        }
                    }
                    else {
                        ForEach(0..<3) { _ in
                            FullWidthListWithIcon(
                                icon: .markerIcon,
                                title: "main title",
                                caption: "This is the full desctiption text",
                                iconColor: Color.accentColor,
                                secondaryIcon: .chevronRight
                            )
                            .redacted(reason: .placeholder)
                            .animatePlaceholder(isLoading: $loading)
                        }
                    }
                }
                .padding(.top)
                .padding(.bottom)
                .padding(.horizontal, 22)
            }
        }
        .onAppear {
            switch type {
            case "pickup":
                placeholderText = "Pick up Location"
            case "dropoff":
                placeholderText = "Destination"
            default:
                placeholderText = "Address"
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
                self.nameInFocus = true
            }
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient, ignoresSafeAreaEdges: .all)
        .navigationBarBackButtonHidden()
        .navigationBarTitleDisplayMode(.inline)
        .modifier(LoadingView(isPresented: self.$isLoading))
        .toolbar {
            ToolbarItem(placement: .cancellationAction) {
                Button(action: {
                    self.presentationMode.wrappedValue.dismiss()
                }) {
                    Image(.arrowLeft)
                }
                .padding(.leading, 3)
                .padding(.trailing, 15)
            }
            ToolbarItem(placement: .principal) {
                TextField(placeholderText, text: $searchField)
                    .focused($nameInFocus)
                    .onChange(of: searchField) { item in
                        showCloseIcon =  !searchField.isEmpty ? true : false
                        if self.showAutoComplete {
                            if searchField.count > 1 {
                                print("herererere")
                                fetchAutocompleteResults(userLatitude: userLatitude, userLongitude: userLongitude)
                            }
                            if searchField.isEmpty {
                                predictions = []
                            }
                        }
                        self.showAutoComplete = true
                    }
                    .padding(.trailing, 40)
                    .padding(.vertical, 8)
                    .padding(.horizontal, 15)
                    .background(Color("Card"))
                    .cornerRadius(8)
                    .font(.system(size: 16))
                    .overlay(
                        HStack {
                            Spacer()
                            if showCloseIcon {
                                Button(action: {
                                    searchField = ""
                                    showCloseIcon = false
                                    predictions = []
                                    UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                                    
                                }) {
                                    Image(systemName: "xmark")
                                        .padding()
                                        .font(.system(size: 12))
                                        .foregroundColor(Color.gray)
                                }
                            }
                        }
                    )
            }
        }
    }
    
    func onAddressDismiss() {
        self.presentationMode.wrappedValue.dismiss()
    }
    
    func dismiss() {
//        print(self.address,self.lat, self.lng, self.type, "tyepeppepepep")
        self.lat = self.latitude
        self.lng = self.longitude
        self.presentationMode.wrappedValue.dismiss()
    }
    
    func fetchAutocompleteResults(userLatitude: Double, userLongitude: Double, searchRadius: Int = 5000) {
        let location = "\(userLatitude),\(userLongitude)" // User's location
//        let countries = "country:AU|country:NP" // Filter for Australia and Nepal
        let countries = ""
        guard let url = URL(string: "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=\(searchField)&location=\(location)&radius=\(searchRadius)&components=\(countries)&key=\(Env.websAPIKey)") else { return }
        
        predictions = []
        loading = true
        
        URLSession.shared.dataTask(with: url) { data, response, error in
            loading = false
            
            if let error = error {
                print("Error fetching autocomplete results:", error.localizedDescription)
                return
            }
            
            guard let data = data else {
                print("No data received")
                return
            }
            
            do {
                let autocompleteResponse = try JSONDecoder().decode(PredictionsData.self, from: data)
                DispatchQueue.main.async {
                    self.predictions = autocompleteResponse.predictions ?? []
                }
            } catch {
                print("Error decoding autocomplete response:", error.localizedDescription)
            }
        }.resume()
    }


}

#Preview {
    NavigationView {
        SearchView(type: .constant("transfer"), lat: .constant(0), lng: .constant(0), address: .constant("address"))
    }
}



struct FullWidthListWithIcon: View {
    @State var icon: ImageResource?
    @State var title: String?
    @State var localizedTitle: LocalizedStringKey?
    @State var caption: String?
    @State var iconColor: Color?
    @State var secondaryIcon: ImageResource?
    var body: some View {
        HStack (alignment: .center, spacing: 20){
            if let icon = icon {
                Image(icon)
                    .renderingMode(.template)
                    .resizable()
                    .foregroundStyle(iconColor ?? Color.black)
                    .scaledToFit()
                    .frame(width: 18, height: 16)
            }
            VStack (alignment: .leading){
                Group {
                    if let title = title {
                        Text(title)
                    }
                    if let localTitle = localizedTitle{
                        Text(localTitle)
                    }
                }
                .font(.system(size: 16))
                .foregroundStyle(Color.black)
                .frame(maxWidth: .infinity, alignment: .leading)
                .multilineTextAlignment(.leading)
                if let caption = caption {
                    Text(caption)
                        .foregroundStyle(.gray)
                        .font(.system(size: 14))
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .multilineTextAlignment(.leading)
                }
            }
            Spacer()
            if let secondIcon = secondaryIcon {
                Image(secondIcon)
                    .renderingMode(.template)
                    .resizable()
                    .scaledToFit()
                    .foregroundStyle(Color.black)
                    .frame(width: 18, height: 12)
            }
        }
    }
}


