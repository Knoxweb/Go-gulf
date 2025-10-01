//
//  ChooseLocationView.swift
//  GoGulf
//
//  Created by Mac on 12/19/21.
//

import SwiftUI
import Kingfisher

struct VehicleListView: View {
    @State var data: QuoteResponse?
    @State var passenger: Int?
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    @State var selection = 0
    @State var showSheet = true
    @StateObject var VM: SetDateTimeVM = SetDateTimeVM()
    
    var body: some View {
        VStack {
            GoogleMapsRoutes(fromLat: data?.quote?.pickupAddress?.lat, fromLng: data?.quote?.pickupAddress?.lng, toLat: data?.quote?.dropAddress?.lat ?? 0, toLng: data?.quote?.dropAddress?.lng ?? 0, route: data?.quote?.route)
                .edgesIgnoringSafeArea(.all)
                .padding(.bottom, -20)
                .ignoresSafeArea()
            
            FleetsListView(VM: VM, data: data, selection: $selection, passenger: passenger)
            
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .toolbarRole(.editor)
        .onAppear() {
            if selection == 0 {
                VM.choosedFleet = data?.fleets?.first
            }
            UINavigationBar.circularBakButton()
        }
        
    }
}

struct FleetsListView: View {
    @ObservedObject var VM: SetDateTimeVM
    @State var data: QuoteResponse?
    @Binding var selection: Int
    @State var passenger: Int?
    @EnvironmentObject var router: Router
    var body: some View {
        VStack {
            VStack (spacing: 20){
//                Text("Select a vehicle to continue")
//                    .font(.system(size: 16))
//                    .padding(.top)
//                Divider()
                    ScrollView {
                        VStack {
                            VStack (spacing: 15){
                                if let fleets = data?.fleets {
                                    ForEach(Array((fleets.enumerated())), id: \.offset) { index, element in
                                        SingleFleetRow(data: data, element: element)
                                            .background(selection == index ? Color.accentColor.opacity(0.2) : Color("Card"))
                                            .cornerRadius(8)
                                            .overlay(RoundedRectangle(cornerRadius: 12).stroke(selection == index ? Color.accentColor : .clear, lineWidth: 2))
                                            .contentShape(Rectangle())
                                            .onTapGesture {
                                                if self.selection != index {
                                                    VM.choosedFleet = element
                                                    self.selection = index
                                                }
                                            }
                                        
                                    }
                                }
                            }
                        }
                        .padding()
                }
            }
            
            Button(action: {
                if let fleetData = VM.choosedFleet {
                    router.navigateTo(.quoteConfirm(quoteData: data, fleetData: fleetData, passenger: passenger))
                }
            }){
                Text("Continue")
                    .fullWithButton()
            }
            .padding(.horizontal)
        }
        .frame(maxWidth: .infinity)
        .frame(height: 350)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }
}


struct SingleFleetRow: View {
    @State var data: QuoteResponse?
    @State var element: FleetList?
    var body: some View {
        HStack (spacing: 10){
            KFImage(URL(string: element?.smallImagePath ?? ""))
                .placeholder {
                    Image(.appLogo)
                        .resizable()
                        .scaledToFit()
                        .frame(height: 40)
                        .opacity(0)
                }
                .resizable()
                .scaledToFit()
                .frame(height: 40)
            
            VStack (alignment: .leading, spacing: 8){
                HStack {
                    BadgeView(text: .constant("\(element?.className ?? "")"), bgColor: .constant(Color.accentColor), foregroundColor: .constant(Color(.theme)))
                    FleetCapacityView(pax: element?.passengers, pet: element?.pet, wheelchair: element?.wheelchair)
                    Spacer()
                }
                HStack {
                    Text(element?.typeName ?? "")
                        .foregroundStyle(Color.black)
                        .font(.system(size: 18))
                    Spacer()
                    VStack {
                        if element?.fare != element?.offerFare {
                            Text("$\(element?.fare ?? "")")
                                .font(.system(size: 14))
                                .foregroundStyle(Color.gray)
                                .strikethrough(true)
                        }
                        
                        Text("$\(element?.offerFare ?? "")")
                            .font(.system(size: 18))
                            .fontWeight(.bold)
                            .foregroundStyle(Color.accentColor)
                    }
                }
                Text("\(data?.quote?.distance ?? "") \(data?.quote?.duration ?? "")")
                    .font(.system(size: 14))
                    .foregroundColor(Color.black.opacity(0.5))
            }
        }
        .foregroundColor(Color.black)
        .padding(.horizontal, 10)
        .padding(.vertical, 10)
    }
}
