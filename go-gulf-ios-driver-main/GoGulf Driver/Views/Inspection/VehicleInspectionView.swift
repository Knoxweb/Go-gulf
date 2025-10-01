//
//  VehicleInspectionView.swift
// SlyykDriverDriver
//
//  Created by Office on 24/11/2022.
//

import SwiftUI
import PDFKit


struct VehicleInspectionView: View {
    @State public var elements = [
        DriverInspect(text: "Check that body, rims, and tires are in excellent condition", value: false),
        DriverInspect(text: "Check that the interior is impeccable", value: false),
        DriverInspect(text: "Check that the water bottles are in place", value: false),
        DriverInspect(text: "Check that the rear shelf is turned on", value: false),
        DriverInspect(text: "Check that customers have at their disposal what to load their mobile", value: false)
    ]
    @State var navigate = false
    var body: some View {
        VStack(alignment: .center){
            SafetyInspectionBody(elements: $elements, image: "vehicle-icon", heading: "Check Vehicle Conndition", subHeading: "(Before and after each customer assumption)", typo: "")
            Spacer()
            Button(action: {
                withAnimation(.default){
                    var condition: Bool = true
                    for d in self.elements {
                        if !d.value {
                            condition = false
                            break;
                        }
                    }
                    if (condition) {
                        self.navigate.toggle()
                    }
                }
            }, label: {
                Image(systemName: "chevron.right")
                    .font(.system(size: 20, weight: .semibold))
                    .foregroundColor(Color("ThemeColor"))
                    .frame(width: 60, height: 60)
                    .background(Color.accentColor)
                    .clipShape(Circle())
                    .overlay(
                        ZStack {
                            Circle()
                                .stroke(Color.accentColor.opacity(0.04), lineWidth: 4)
                            
                            Circle()
                                .trim(from: 0, to: 1)
                                .stroke(Color.accentColor, lineWidth: 4)
                                .rotationEffect(.init(degrees: -90))
                        }
                            .padding(-15)
                    )
            })
            .padding(.vertical)
            .padding(.bottom)
                .navigationDestination(isPresented: self.$navigate) {
                    ImpInfoView()
                }
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 30)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationBarBackButtonHidden(true)
    }
}

struct VehicleInspectionView_Previews: PreviewProvider {
    static var previews: some View {
        VehicleInspectionView()
    }
}
