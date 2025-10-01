//
//  PlacePicker.swift
//  GoGulf
//
//  Created by Mac on 1/10/22.
//

import Foundation
import SwiftUI
import Firebase
import FirebaseFirestore
import CoreLocation

struct PlacePickerView : View {
    @StateObject var viewModel = AutoCompleteVM()
    let identity = UserDefaults.standard.string(forKey: "identity")
    @State var shortcuts: [shortcutModel] = []
    let columns = [
        GridItem(.adaptive(minimum: 60))
    ]
    @Environment(\.presentationMode) var presentationMode
    @EnvironmentObject var router: Router
    @State var navigate = false
    @State var isPresented = false
    @State var address = ""
    @State var lat: Double = 0
    @State var lng: Double = 0
    
    @State var buttonDisabled = false
    
    @State var pAddress: String?
    @State var pLat: Double?
    @State var pLng: Double?
    @State var type = ""
    
    var body: some View {
        VStack {
            VStack {
            VStack {
                HStack {
                    VStack(alignment: .leading, spacing: 15) {
                        Button(action: {
                            viewModel.type = "pickup"
                            isPresented = true
                        }){
                            VStack {
                                
                                HStack{
                                    VStack(alignment: .leading, spacing: 8) {
                                        Text("Pick up Location")
                                            .foregroundStyle(.gray)
                                        Text(viewModel.pickup)
                                            .lineLimit(1)
                                            .foregroundStyle(.black)
                                    }
                                    
                                    Spacer()
                                }
                                .padding(.leading)
                                .frame(maxWidth: .infinity)
                            }
                            .padding(.top)
                        }
                        
                        Divider()
                        Button(action: {
                            viewModel.type = "dropoff"
                            isPresented = true
                        }){
                            HStack{
                                VStack(alignment: .leading, spacing: 8) {
                                    Text("Destination")
                                        .foregroundColor(.gray)
                                    Text("\(viewModel.dropoff)")
                                        .lineLimit(1)
                                        .foregroundStyle(.black)
                                    
                                }
                                Spacer()
                            }
                            .padding(.leading)
                            .padding(.bottom)
                            .frame(maxWidth: .infinity)
                            
                        }
                    }
                    .font(.system(size: 16))
                    Spacer()
                }
            }
            .background(Color("Card"))
            .cornerRadius(15)
            .padding(.horizontal)
            .padding(.top, 35)
            
            ZStack (alignment: .leading){
                LazyVGrid(columns: columns, spacing: 20) {
                    if let array = viewModel.addrData {
                        ForEach(Array((array.enumerated())), id: \.offset) { index, element in
                            Button(action: {
                                viewModel.dropoff = element.address ?? ""
                                viewModel.dLat = element.lat ?? 0
                                viewModel.dLng = element.lng ?? 0
                            }) {
                                VStack(spacing: 10) {
                                    Image(element.name ?? "")
                                        .renderingMode(.template)
                                        .resizable()
                                        .foregroundStyle(Color.accentColor)
                                        .scaledToFit()
                                        .frame(width: 30, height: 30 )
                                    Text(element.name?.capitalized ?? "")
                                        .font(.system(size: 12))
                                        .foregroundColor(Color.black)
                                }
                            }
                        }
                    }
                    
                    Button(action: {
                        router.navigateTo(.addShortcut(addr: nil))
                    }) {
                        VStack(spacing: 10) {
                            Image("add")
                                .frame(width: 30, height: 30 )
                            Text("Add")
                                .font(.caption)
                                .foregroundColor(Color.black)
                        }
                    }
                    
                    Spacer()
                }
                .padding(.horizontal, 15.0)
            }
            .padding(.top)
            Spacer()
        }
        VStack {
            Button(action: {
                if(self.pLat != 0 && viewModel.dLat != 0){
                    router.navigateTo(.dateTimeScreen(pickup: viewModel.pickup, pLat: viewModel.pLat, pLng: viewModel.pLng, via: viewModel.via, vLat: viewModel.vLat, vLng: viewModel.vLng, dropoff: viewModel.dropoff, dLat: viewModel.dLat, dLng: viewModel.dLng))
                }
                else{
                    let alertController = GlobalAlertController(title: "Error", message: "Please add all fields", preferredStyle: .alert)
                    alertController.addAction(UIAlertAction(title: "Ok", style: .destructive, handler: nil))
                    alertController.presentGlobally(animated: true, completion: nil)
                }
            }) {
                Text("Continue")
                    .fullWithButton()
            }
        }
        .padding()
        }
        .onAppear(){
            viewModel.getAddresses()
            print(pLat, pLng, "herererere")
            if viewModel.pickup.isEmpty {
                viewModel.pickup = pAddress ?? ""
                viewModel.pLat = pLat ?? 0
                viewModel.pLng = pLng ?? 0
            }
//            if viewModel.dropoff.isEmpty {
//                self.pickup = self.address ?? ""
//                self.pLat = self.lat ?? 0
//                self.pLng = self.lng ?? 0
//                
//                viewModel.dropoff =  self.dropoff ?? ""
//                viewModel.dLat =  self.dLat ?? 0
//                viewModel.dLng = self.dLng ?? 0
//            }
        }
        .onDisappear() {
            viewModel.stopListener()
        }
        .fullScreenCover(isPresented: $isPresented , onDismiss: fetchAddress) {
            NavigationView {
                SearchView(type: $viewModel.type, lat: $lat, lng: $lng, address: $address)
            }
        }
        
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//        .navigationBarBackButtonHidden(true)
        .toolbarRole(.editor)
        .navigationBarTitle("Enter Location", displayMode: .inline)
//        .sheet(isPresented: $viewModel.shouldOpenPlacePicker) {
//            PlacePicker(type: $viewModel.type, pickup: self.$pickup, via: $viewModel.via, dropoff: $viewModel.dropoff, pLat: self.$pLat, pLng: self.$pLng, vLat: $viewModel.vLat, vLng: $viewModel.vLng, dLat: $viewModel.dLat, dLng: $viewModel.dLng)
//        }
    }
    
    func fetchAddress() {
//        if pickup == "" && pAddress != "" {
//            viewModel.pickup = pAddress ?? ""
//            viewModel.pLat = pLat ?? 0
//            viewModel.pLat = pLng ?? 0
//        }
        
        if address != "" {
            switch viewModel.type {
            case "pickup" :
                viewModel.pickup = address
                viewModel.pLat = lat
                viewModel.pLng = lng
                break;
            case "dropoff" :
                viewModel.dropoff = address
                viewModel.dLat = lat
                viewModel.dLng = lng
                break;
            default:
                break;
            }
        }
        
    }
    
}

struct PlacePickerView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            PlacePickerView()
        }
    }
}

