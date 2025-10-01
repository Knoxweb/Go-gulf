//
//  LeadPassengerView.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//

import SwiftUI

struct LeadPassenger: View {
    @StateObject var viewModel: QuoteConfirmVM
   
    var body: some View {
        Section(header: Text("Booking for someone else")){
            Toggle("Lead Passenger", isOn: $viewModel.leadPassenger.animation())
                .tint(Color.accentColor)
            if viewModel.leadPassenger {
                HStack {
                    TextField("Full Name", text: $viewModel.fullname)
                }
                HStack {
                    TextField("Phone", text: $viewModel.phone)
                        .keyboardType(.numberPad)
                }
            }
        }
        .listRowBackground(Color("Card"))
    }
}
