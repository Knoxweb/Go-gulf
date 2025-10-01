//
//  AccountCompleteView.swift
//  SlyykDriver
//
//  Created by Office on 22/03/2023.
//

import SwiftUI

struct AccountCompleteView: View{
    private let timer = Timer.publish(every: 3, on: .main, in: .common).autoconnect()
    @State private var index = 1
    @State var navigateToDocument = false
    @State var navigateToFleet = false
    @State private var selectedNum: String = ""
    @Binding var data: [NoticesModel]
    @ObservedObject var VM: DashboardVM
    
    var body: some View{
        
            VStack (alignment: .leading){
              
                GeometryReader { proxy in
                    TabView {
                        ForEach(Array(data.enumerated()), id: \.offset) { index, element in
                                VStack (alignment: .leading){
                                Button(action: {
                                    if element.type == "no-fleet" {
                                        self.navigateToFleet = true
                                    }
                                    else if element.type == "no-document" {
                                        self.navigateToDocument = true
                                    }
                                    else if element.type == "no-bank" {
                                        VM.ShowBankAccountSheet = true
                                    }
                                }) {
                                    HStack(alignment: .top, spacing: 10){
                                        Image(systemName: element.type == "no-document" ? "arrow.up.doc.fill" : (element.type == "no-vehicle" ? "car.fill" : "building.2.crop.circle"))
                                            .font(.system(size: 28))
                                            .foregroundStyle(Color.accentColor.opacity(0.2))
                                        
                                        VStack(alignment: .leading, spacing: 2){
                                            HStack {
                                                Text("\(element.title ?? "")")
                                                    .fontWeight(.bold)
                                                    .opacity(0.8)
                                                Spacer()
                                            }
                                            Text("\(element.message ?? "")")
                                                .opacity(0.5)
                                                .multilineTextAlignment(.leading)
                                                .fixedSize(horizontal: false, vertical: true)
                                            
                                            Text("Upload")
                                                .padding(.top, 10)
                                                .padding(.trailing, 8)
                                                .foregroundColor(.accentColor)
                                        }
                                    }
                                    .foregroundColor(.black)
                                }
                            }
                                .cardStyleModifier()
                                .padding()
                        }
                        
                    }
                    Spacer()
                }
                .tabViewStyle(PageTabViewStyle(indexDisplayMode: .automatic))
                .frame(minHeight: 250)
                .navigationDestination(isPresented: $navigateToDocument) {
                    DriverDocumentView()
                }
                .navigationDestination(isPresented: $navigateToFleet) {
                    VehicleListedView()
                }
                .padding(.top, -20)
            }
    }
}
