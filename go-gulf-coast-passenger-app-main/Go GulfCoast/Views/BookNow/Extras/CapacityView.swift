//
//  CapacityView.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//

import SwiftUI


struct FleetCapacityView: View {
    @State var pax: Int?
    @State var pet: Int?
    @State var wheelchair: Int?
    var body: some View {
        HStack (spacing: 8){
            HStack (spacing: 3){
                Image(.passengersWhite)
                    .renderingMode(.template)
                    .resizable()
                    .scaledToFit()
                    .frame(height: 12)
                    .foregroundStyle(Color.black)
                Text("\(pax ?? 0)")
            }
            HStack (spacing: 3){
                Image(.petsWhite)
                    .renderingMode(.template)
                    .resizable()
                    .scaledToFit()
                    .frame(height: 12)
                    .foregroundStyle(Color.black)
                Text("\(pet ?? 0)")
            }
            HStack (spacing: 3){
                Image(.wheelchairWhite)
                    .renderingMode(.template)
                    .resizable()
                    .scaledToFit()
                    .frame(height: 12)
                    .foregroundStyle(Color.black)
                Text("\(wheelchair ?? 0)")
            }
        }
        .font(.system(size: 10))
    }
}


struct CapacityView: View {
    @StateObject var viewModel: QuoteConfirmVM
    var element: FleetList?
    var body: some View {
//        Picker("Passenger", selection: ) {
//            ForEach(1...25, id: \.self) { number in
//                Text("\(number)")
//            }
//            .disabled(true)
//        }
//        .listRowBackground(Color("Card"))
        
        Picker("Pet", selection: $viewModel.pet) {
            ForEach(0...(element?.pet ?? 1), id: \.self) { number in
                Text("\(number)")
            }
            
        }
        .listRowBackground(Color("Card"))
        Picker("Wheel Chair", selection: $viewModel.wheelChair) {
            ForEach(0...(element?.wheelchair ?? 1), id: \.self) { number in
                Text("\(number)")
            }
            
        }
        .listRowBackground(Color("Card"))
    }
}
