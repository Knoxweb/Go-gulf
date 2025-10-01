//
//  ExtrasView.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//

import SwiftUI

struct ExtrasView: View {
    @StateObject var viewModel: QuoteConfirmVM
    
    struct Extra: Identifiable {
        let id = UUID()
        let type: String
        let title: String
        let info: String
        let price: String
    }
    public var extras = [
        Extra(type: "infant", title: "Infant Seat", info: "approx 0 to 12 months", price: "25"),
        Extra(type: "child", title: "Infant Seat", info: "approx 0 to 12 months", price: "25"),
        Extra(type: "booster", title: "Infant Seat", info: "approx 0 to 12 months", price: "25")
    ]
 
    let step = 1
    let range = 0...10
    var body: some View {
        
        Section {
            Toggle("Add Extras", isOn: $viewModel.addExtras.animation())
                .tint(Color.accentColor)
            if viewModel.addExtras {
                HStack {
                    Stepper(value: $viewModel.infant,
                            in: range,
                            step: step) {
                        HStack {
                            VStack(alignment: .leading) {
                                Text("Infant Seat $25")
                                    .font(.subheadline)
                                Text("(approx 0 to 12 months)")
                                    .font(.caption)
                                    .foregroundColor(Color.gray)
                            }
                            Spacer()
                            Text("Seats \(viewModel.infant)")
                                .font(.subheadline)
                        }
                    }
                }
                .padding(.vertical, 10)
                
                HStack {
                    Stepper(value: $viewModel.child,
                            in: range,
                            step: step) {
                        HStack {
                            VStack(alignment: .leading) {
                                Text("Child Charge $25")
                                    .font(.subheadline)
                                Text("(approx 1 to 4 years)")
                                    .font(.caption)
                                    .foregroundColor(Color.gray)
                            }
                            Spacer()
                            Text("Seats \(viewModel.child)")
                                .font(.subheadline)
                        }
                    }
                    
                }
                .padding(.vertical, 10)
                
                HStack {
                    Stepper(value: $viewModel.booster,
                            in: range,
                            step: step) {
                        HStack {
                            VStack(alignment: .leading) {
                                Text("Booster Seat $25")
                                    .font(.subheadline)
                                Text("(approx 4 to 8 years)")
                                    .font(.caption)
                                    .foregroundColor(Color.gray)
                            }
                            Spacer()
                            Text("Seats \(viewModel.booster)")
                                .font(.subheadline)
                        }
                    }
                    
                }
                .padding(.vertical, 10)
            }
        }
        .listRowBackground(Color("Card"))
    }
}
