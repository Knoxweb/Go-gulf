//
//  AvaialableCardUI.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 28/08/2024.
//

import SwiftUI

struct AvailableCardView: View{
    @Binding var element: DispatchJobModel
    @ObservedObject var VM: DispatchVM
    @EnvironmentObject var tabRouter: TabRouter
    
    var body: some View{
        
        VStack (spacing: 15){
            ZStack {
                VStack {
                    VStack{
                        HStack{
                            HStack {
                                Image(.calendar)
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 20)
                                Text("\(element.pickup_date_time ?? "")".capitalized)
                                    .fontWeight(.bold)
                                    .font(.system(size: 20))
                            }
                        }
                        .opacity(0.7)
                        Divider()
                        VStack(alignment: .leading, spacing: 10){
                            HStack{
                                Image(systemName: "circle.fill")
                                    .font(.system(size: 10))
                                    .foregroundColor(.green)
                                Text(element.pickup_address ?? "")
                                    .font(.system(size: 16))
                                    .opacity(0.8)
                                Spacer()
                            }
                            
                            HStack{
                                Image(systemName: "circle.fill")
                                    .font(.system(size: 10))
                                    .foregroundColor(.red)
                                Text(element.drop_address ?? "")
                                    .font(.system(size: 16))
                                    .opacity(0.8)
                                Spacer()
                            }
                        }
                        .padding(.vertical, 5)
                        Divider()
                        HStack(alignment: .bottom){
                            VStack{
                                Text("$\(element.fare ?? "")")
                                    .foregroundColor(Color("AccentColor"))
                                    .fontWeight(.bold)
                                Text("(inc. GST)")
                                    .font(.system(size: 13))
                                    .opacity(0.7)
                            }
                            Spacer()
                              
                                Button(action: {
                                    VM.acceptJob(id: element.id ?? 0, type: element.type, tabRouter: tabRouter)
                                }){
                                    Text("Accept")
                                        .fontWeight(.bold)
                                        .frame(width: 150, height: 50)
                                        .background(Color.accentColor)
                                        .foregroundStyle(Color("ThemeColor"))
                                        .cornerRadius(10.0)
                                        .font(.system(size: 18))
                                        .shadow(radius: 8)
                                        .foregroundColor(.black)
                                }
                            
                        }
                        .font(.system(size: 28))
                        
                    }
                    .cardStyleModifier()
                }
                VStack {
                    
                    HStack {
                        Spacer()
                        Button(action: {
                            VM.rejectJob(id: element.id, type: element.type, tabRouter: tabRouter)
                        }){
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.red)
                                .font(.title)
                        }
                        .opacity(1)
                    }
                    Spacer()
                }
                .offset(x: 0, y: -15)
            }
            Spacer()
        }
    }
}
