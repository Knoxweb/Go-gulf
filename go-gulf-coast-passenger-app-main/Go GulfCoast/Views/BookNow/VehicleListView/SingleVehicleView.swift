//
//  SingleVehicleView.swift
//   GoGulf
//
//  Created by Office on 6/24/22.
//

import SwiftUI
//
//struct singleVehicleSlider: View {
//    var VM: SetDateTimeVM
//    var element: QuoteResponse
//    @Binding var currentSlide: Int
//    @State var navigate = false
//    
//    var body: some View {
//        let fleetId = element.fleet_id
//        VStack{
//            VStack{
//                Text("Slide to choose")
//                    .font(.system(size: 20  ))
//                    .padding(.vertical, 5)
//            }
//            Divider()
//            HStack(alignment: .top, spacing: 0) {
//                VStack{
//                    HStack{
//                        VStack (alignment: .leading, spacing: 5){
//                            Text("\(VM.responseData?.data?.distance ?? "") | \(VM.responseData?.data?.duration ?? "")")
//                                .font(.system(size: 15))
////                            HStack {
////                                    Button(action: {
////                                        if currentSlide > 0 {
////                                            withAnimation {
////                                                currentSlide -= 1
////                                            }
////                                        }
////                                    }) {
////                                        Image(systemName: "chevron.left")
////                                            .font(.title)
////                                            .opacity(1)
////                                            .foregroundColor(Color("AccentColor"))
////                                    }
////
////                                    Image(systemName: "circle")
////                                        .font(.subheadline)
////                                        .foregroundColor(.white)
////                                        .opacity(0.3)
////
////                                    Button(action: {
////                                        if currentSlide < 2 {
////                                            withAnimation {
////                                                currentSlide += 1
////
////                                            }
////                                        }
////                                    }) {
////                                        Image(systemName: "chevron.right")
////                                            .font(.title)
////                                            .foregroundColor(Color("AccentColor"))
////                                            .opacity(1)
////                                    }
////                            }
////                            .padding(.bottom, 10)
//                        
//                            VStack (alignment: .leading, spacing: 3){
//                                Text(element.className ?? "")
//                                    .font(.system(size: 20))
//                                    .fontWeight(.bold)
//                                    .foregroundColor(Color.accentColor)
//                                HStack {
//                                    Text(element.typeName ?? "")
//                                    .font(.system(size: 16))
//                                    
//                                }
//                            }
//                            .foregroundColor(.white)
//                            HStack{
//                                HStack(alignment: .center){
//                                    Image("PassengersWhite")
//                                        .resizable()
//                                        .scaledToFit()
//                                        .frame(width: 15, height: 15)
//                                    Text("\(element.passenger ?? 0)")
//                                        .foregroundColor(.accentColor)
//                                }
//                                .foregroundColor(.white)
//                                HStack(alignment: .center){
//                                    Image("PetsWhite")
//                                        .resizable()
//                                        .scaledToFit()
//                                        .frame(width: 15, height: 15)
//                                    Text("\(element.pet ?? 0)")
//                                        .foregroundColor(.accentColor)
//                                }
//                                .foregroundColor(.white)
//                                
//                                HStack(alignment: .center){
//                                    Image("WheelchairWhite")
//                                        .resizable()
//                                        .scaledToFit()
//                                        .frame(width: 15, height: 15)
//                                    Text("\(element.wheelChair ?? 0)")
//                                        .foregroundColor(.accentColor)
//                                }
//                                .foregroundColor(.white)
//                                Spacer()
//                            }
//                            .padding(.top, 8)
//                        }
//                        Spacer()
//                    }
//                       
//                }
//                VStack{
//                        Image(fleetId == 1 ? "fleeta" : (fleetId == 2 ? "fleeta" : (fleetId == 3 ? "fleetb" : "fleetb")))
//                            .resizable()
//                            .aspectRatio(contentMode: .fill)
//                            .frame(width: fleetId == 0 ? 220 : (fleetId == 1 ? 220 : 200), height: fleetId == 0 ? 140 : (fleetId == 1 ? 140 : 130))
//                            .offset(x: 16, y: fleetId == 0 ? 0 : (fleetId == 1 ? 0 : 0))
//                    Spacer()
//                }
//            }
//            .padding(.top, 20)
//            
//            VStack{
//                VStack(alignment: .center, spacing: 5){
//                    if element.fare == element.offerFare{
//                        Text("$\(element.fare ?? "")")
//                            .foregroundColor(Color.accentColor)
//                            .font(.system(size: 22))
//                            .fontWeight(.bold)
//                        Text("Inclusive GST")
//                            .font(.caption2)
//                    }
//                    else{
//                        Text("$\(element.offerFare ?? "")")
//                                .foregroundColor(Color.accentColor)
//                                .font(.system(size: 22))
//                                .fontWeight(.bold)
//                        
//                        Text("$\(element.fare ?? "")")
//                            .foregroundColor(.red)
//                                .font(.system(size: 16))
//                        Text("Inclusive GST")
//                            .font(.caption2)
//                    }
//                    
//                }
//                
//                Button(action: {
//                    self.navigate.toggle()
//                }){
//                        UIButton(label: "Select")
//                }
//            }
//        }
//    }
//}
