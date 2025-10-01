//
//  FlightsInfoView.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//

import SwiftUI


struct FlightInfo: View {
    @StateObject var viewModel: QuoteConfirmVM
 
    var body: some View {
        Section (header: Text("Enter Flight Number")){
            Toggle("Flight Information", isOn: $viewModel.flightInfo.animation())
                .tint(Color.accentColor)
            if viewModel.flightInfo {
                HStack {
                    TextField("Flight No", text: $viewModel.flightNumber)
                }
            }
        }
        .listRowBackground(Color("Card"))
    }
}
