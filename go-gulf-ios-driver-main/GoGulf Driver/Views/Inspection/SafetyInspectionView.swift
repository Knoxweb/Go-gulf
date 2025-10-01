//
//  SafetyInspectionView.swift
// SlyykDriverDriver
//
//  Created by Office on 24/11/2022.
//

import SwiftUI

struct DriverInspect: Identifiable {
    let id = UUID()
    var text: String
    var value: Bool
}


struct SafetyInspectionView: View {
    @State public var elements = [
        DriverInspect(text: "Suit", value: false),
        DriverInspect(text: "Tie", value: false),
        DriverInspect(text: "White Shirt", value: false),
        DriverInspect(text: "City Shoes", value: false),
        DriverInspect(text: "Hat", value: false),
        DriverInspect(text: "Gloves", value: false),
    ]
    @State var navigate = false
    var body: some View {
        VStack(alignment: .center){
            SafetyInspectionBody(elements: $elements, image: "suit", heading: "Dress Code and Mandatory Equipment", subHeading: "(Before and after each customer assumption)", typo: "uppercase")
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
                    VehicleInspectionView()
                }
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 30)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationBarBackButtonHidden(true)
    }
}


struct SafetyInspectionBody: View {
    @Binding var elements: Array<DriverInspect>
    var image: String
    var heading: String
    var subHeading: String
    var typo: String
    
    var body: some View {
        
        Image(self.image)
            .resizable()
            .scaledToFit()
            .frame(width: 120, height: 120)
            .padding(.top)
        
        VStack(alignment: .center, spacing: 8){
            Text(self.heading.uppercased())
                .font(.system(size: 20))
                .fontWeight(.black)
                .foregroundColor(Color("AccentColor"))
                .multilineTextAlignment(.center)
            if !self.subHeading.isEmpty {
                Text(self.subHeading.uppercased())
                    .font(.system(size: 16))
                    .italic()
                    .fontWeight(.light)
                    .foregroundColor(Color("AccentColor"))
                    .multilineTextAlignment(.center)
            }
        }
        VStack(alignment: .leading, spacing: 15){
            ForEach($elements) { $element in
                checkListView(element: $element, typo: typo)
            }
        }
        .padding(.vertical, 30)
        
    }
}


struct checkListView: View {
    @Binding var element: DriverInspect
    var typo: String
    var body: some View{
        Button(action: {element.value.toggle()}) {
            HStack (alignment: .top){
                Image(systemName: "checkmark.circle.fill")
                    .font(.title)
                    .foregroundColor(Color(element.value ? "AccentColor" : "Checkmark"))
                
                if typo.isEmpty {
                    Text(element.text)
                        .font(.system(size: 18))
                        .fontWeight(.light)
                        .foregroundColor(.black)
                        .padding(.leading)
                        .multilineTextAlignment(.leading)
                }
                else{
                    Text(element.text.uppercased())
                        .font(.system(size: 18))
                        .fontWeight(.light)
                        .foregroundColor(.black)
                        .padding(.leading)
                        .multilineTextAlignment(.leading)
                }
            }
        }
    }
}


struct SafetyInspectionView_Previews: PreviewProvider {
    static var previews: some View {
        SafetyInspectionView()
    }
}
