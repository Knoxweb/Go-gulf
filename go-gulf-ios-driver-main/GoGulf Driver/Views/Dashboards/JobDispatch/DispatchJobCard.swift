//
//  DispatchJobCard.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 28/08/2024.
//

import SwiftUI


struct DispatchJobCard : View {
    @ObservedObject var VM: DispatchVM
    var index: Int
    var item: DispatchJobModel
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    
    let timer = Timer.publish(every: 1, tolerance: 0.5, on: .main, in: .common).autoconnect()
    let initialCounter = 45
    @State private var counter = 45
    @State var bookingId = ""
    
    
    var body: some View {
        VStack {
            //            if (((item.remainingTime ?? 0) <= 45
            //                  ) && ((item.remainingTime ?? 0) > 0)) {
            VStack {
                ZStack {
                    VStack {
                        VStack{
                            HStack{
                                VStack {
                                    Text("\(item.duration ?? "")".capitalized)
                                        .fontWeight(.bold)
                                        .font(.system(size: 24))
 
                                }
                                Spacer()
                                VStack {
                                    Text("\(item.distance ?? "")".capitalized)
                                        .fontWeight(.bold)
                                        .font(.system(size: 24))
                                    
                                }
                            }
                            .opacity(0.7)
                            Divider()
                            VStack(alignment: .leading, spacing: 10){
                                HStack(alignment: .top){
                                    Image(systemName: "circle.fill")
                                        .font(.system(size: 10))
                                        .foregroundColor(.green)
                                    Text("\(item.pickup_address ?? "")".capitalized)
                                        .font(.system(size: 16))
                                        .opacity(0.8)
                                    Spacer()
                                }
                                
                                HStack(alignment: .top){
                                    Image(systemName: "circle.fill")
                                        .font(.system(size: 10))
                                        .foregroundColor(.red)
                                    Text("\(item.drop_address ?? "")".capitalized)
                                        .font(.system(size: 16))
                                        .opacity(0.8)
                                    Spacer()
                                }
                            }
                            .padding(.vertical, 5)
                            Divider()
                            HStack(alignment: .bottom){
                                VStack{
                                    Text("$\(item.fare ?? "")")
                                        .foregroundColor(Color("AccentColor"))
                                        .fontWeight(.bold)
                                    Text("(inc. GST)")
                                        .font(.system(size: 13))
                                        .opacity(0.7)
                                }
                                Spacer()
                                Button(action: {
                                    VM.acceptJob(id: item.id, type: item.type, tabRouter: tabRouter)
                                }){
                                    Text("Accept")
                                        .fontWeight(.bold)
                                        .frame(width: 150, height: 50)
                                        .background(Color.accentColor)
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
                                VM.rejectJob(id: item.id, type: item.type, tabRouter: tabRouter)
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
        .frame(maxWidth: .infinity)
        
    }
    
}
