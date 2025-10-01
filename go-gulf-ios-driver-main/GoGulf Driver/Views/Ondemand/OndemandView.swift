//
//  RequestingView.swift
// SlyykDriver
//
//  Created by Office on 29/06/2022.
//
//
//import Foundation
//import SwiftUI
//import AudioToolbox
//import AVFoundation
//
//struct OnDemandView: View {
//    @StateObject var viewModel = OnDemandVM()
//    var currentResponse: FirebaseJobResponse?
//    
//    @State var animate = false
//    let timer = Timer.publish(every: 1, tolerance: 0.5, on: .main, in: .common).autoconnect()
//    let vibration = Timer.publish(every: 2, tolerance: 0.5, on: .main, in: .common).autoconnect()
//    @State private var counter = 40
//    let initialCounter = 40
//    @State private var vibrator = 1
//    @State private var animationAmount = 1.0
//    @State var loading = false
//    @State private var shouldAnimate = false
//    
//    var body: some View {
//        ZStack {
//            VStack{
//                ZStack {
//                    GoogleMapsRoutes(fromLat: currentResponse?.fromLat, fromLng: currentResponse?.fromLng, toLat: currentResponse?.toLat, toLng: currentResponse?.toLng, bookingId: "")
//                        .edgesIgnoringSafeArea(.all)
//                        .padding(.top, -65)
//                        .blur(radius: 5)
//                    
//                    VStack {
//                        Spacer()
//                        Text("\(self.counter)")
//                            .font(.system(size: 44))
//                            .fontWeight(.bold)
//                        HStack(spacing: 30) {
//                            Circle()
//                                .fill(Color("AccentColor"))
//                                .frame(width: 20, height: 20)
//                                .scaleEffect(shouldAnimate ? 1.0 : 0.5)
//                                .animation(Animation.easeInOut(duration: 0.5).repeatForever())
//                            Circle()
//                                .fill(Color("AccentColor"))
//                                .frame(width: 20, height: 20)
//                                .scaleEffect(shouldAnimate ? 1.0 : 0.5)
//                                .animation(Animation.easeInOut(duration: 0.5).repeatForever().delay(0.3))
//                            Circle()
//                                .fill(Color("AccentColor"))
//                                .frame(width: 20, height: 20)
//                                .scaleEffect(shouldAnimate ? 1.0 : 0.5)
//                                .animation(Animation.easeInOut(duration: 0.5).repeatForever().delay(0.6))
//                        }
//                        .onAppear {
//                            self.shouldAnimate = true
//                        }
//                        
//                        Spacer()
////                        VStack(spacing: 8){
////                            Text("Pickup at")
////                                .font(.system(size: 20))
////                            Text(currentResponse?.pickupDatetime ?? "")
////                                .fontWeight(.bold)
////                                .font(.largeTitle)
////                        }
////                        .padding(.bottom, 40)
////                        .font(.title3)
//                    }
//                }
//                .padding(.bottom, -26)
//                
//                
//                VStack()  {
//                    VStack {
//                        VStack(alignment: .center) {
//                            Text("New Job Request")
//                                .font(.system(size: 20))
//                                .textCase(.uppercase)
//                                .foregroundColor(Color.accentColor)
//                        }
//                        .padding(.vertical, 8)
//                        .padding(.top, 30)
//                        
////                        Divider()
////                        HStack {
////                            AsyncImage(url: URL(string: currentResponse?.passengerImageLink ?? "")) { image in
////                                image
////                                    .resizable()
////                                    .aspectRatio(contentMode: .fill)
////                            } placeholder: {
////                                Image(systemName: "photo")
////                                    .imageScale(.large)
////                                    .foregroundColor(.gray)
////                            }
////                            .frame(width: 80, height: 80)
////                            .clipShape(Circle())
////                            .shadow(radius: 4)
////                            .padding(.trailing)
////
////                            VStack(alignment: .leading){
////                                Text("Passenger")
////                                    .font(.system(size: 15))
////                                    .foregroundColor(Color.accentColor)
////
////                                Text(currentResponse?.passengerName ?? "")
////                                    .font(.system(size: 18))
////                                    .fontWeight(.bold)
////                                    .padding(.bottom, 4)
////                            }
////                            Spacer()
////                        }
//                        Divider()
//                        
//                        VStack (alignment: .leading, spacing: 18){
//                            HStack {
//                                VStack (alignment: .leading, spacing: 10){
//                                    VStack(alignment: .leading) {
//                                        Text("Pick Up")
//                                            .font(.system(size: 14))
//                                            .foregroundColor(.accentColor)
//                                        Text(currentResponse?.fromLocation ?? "")
//                                            .font(.system(size: 16))
//                                    }
//                                    VStack(alignment: .leading) {
//                                        Text("Destination")
//                                            .font(.system(size: 14))
//                                            .foregroundColor(.accentColor)
//                                        Text(currentResponse?.toLocation ?? "")
//                                            .font(.system(size: 16))
//                                    }
//                                }
//                                .font(.system(size: 18))
//                                .opacity(0.80)
//                                Spacer()
//                            }
//                        }
//                        .padding(.vertical, 8)
//                        Divider()
//                        additionalInfoOndemand(currentData: currentResponse)
//                        Divider()
//                        VStack(alignment: .center, spacing: 5){
//                            Text("$\(String(format:"%.2f", currentResponse?.driverfare ?? 0.0))")
//                                .font(.system(size: 27))
//                                .fontWeight(.bold)
//                                .foregroundColor(Color("AccentColor"))
//                            Text("Inclusive GST")
//                                .font(.caption2)
//                        }
//                        
//                        HStack (alignment: .center, spacing: 30){
//                            Button(action: {
//                                viewModel.rejectJob(bookingId: currentResponse?.bookingId ?? "", docId: currentResponse?.docId ?? "", auto: false)
//                            }){
//                                UIButton(label: "Reject", bgColor: .red, color: .white)
//                            }
//                            Button(action: {
//                                viewModel.acceptJob(bookingId: currentResponse?.bookingId ?? "")
//                            }){
//                                UIButton(label: "Accept", bgColor: .accentColor)
//                            }
//                        }
//                        .padding(.bottom, 45)
//                    }
//                    .padding(.horizontal)
//                    .background(Color.linearGradient)
//                }
//                .cornerRadius(30)
//                .shadow(color: Color("GrayShadow"), radius: 15)
//                .edgesIgnoringSafeArea(.all)
//                NavigationLink(destination: DashboardUIView(), isActive: $viewModel.navigate) {}.opacity(0)
////                NavigationLink(destination: CurrentRideView(currentResponse: viewModel.jobAcceptResponse), isActive: $viewModel.navigateToCurrentRide){}.opacity(0)
//            }
//            .background(.clear)
//            .navigationBarBackButtonHidden(viewModel.loading)
//            .edgesIgnoringSafeArea(.all)
//            .navigationBarBackButtonHidden(true)
//            .onReceive(vibration) { v in
//                if vibrator == 30 {
//                    vibration.upstream.connect().cancel()
//                    return;
//                } else {
//                    AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
//                    AudioServicesPlayAlertSound(UInt32(1009))
//                }
//                vibrator += 1
//            }
//            .onAppear(){
//                AudioServicesPlayAlertSound(UInt32(1009))
//                AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
//            }
//            .onDisappear() {
//                vibration.upstream.connect().cancel()
//                timer.upstream.connect().cancel()
//            }
//            .onReceive(timer) { time in
//                if(viewModel.navigateToCurrentRide || viewModel.navigate){
//                    timer.upstream.connect().cancel()
//                }
//                if counter == 0 {
//                    timer.upstream.connect().cancel()
//                    viewModel.rejectJob(bookingId: currentResponse?.bookingId ?? "", docId: currentResponse?.docId ?? "", auto: true)
//                    return;
//                } else {
//                    self.getRemainingSecond()
//                }
//                counter -= 1
//            }
//            if viewModel.loading {
//                ActivityIndicator()
//            }
//        }
//        .disabled(viewModel.loading)
//    }
//    
//    func getRemainingSecond(){
//        
//        let createdDate = currentResponse?.createdAtUTC;
//        let createdFormatter = DateFormatter()
//        createdFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss a Z"
//        let createdInterval = createdFormatter.date(from: createdDate ?? "")!.timeIntervalSince1970
//        let created = Int(createdInterval * 1000)
//        
//        let dateFormatter = DateFormatter()
//        dateFormatter.dateFormat = "yyyy-MM-dd hh:mm:ss a Z"
//        dateFormatter.timeZone = TimeZone(identifier: "UTC") // fixes nil if device time in 24 hour format
//        let date = dateFormatter.string(from: Date())
//        print(date, "dddddddddd")
//        
//        let currentFormatter = DateFormatter()
//        currentFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss a Z"
//        let currentInterval = currentFormatter.date(from: date)!.timeIntervalSince1970
//        print(currentInterval, "dfdffdfdfdfdfdrrrrrrrrr")
//        
//        let now = Int(currentInterval * 1000);
//        let diffences = (now - created) / 1000
//        print(diffences, "differencesssss")
//        if(diffences >= initialCounter) {
//            print("greater than 120 -----------")
//            viewModel.rejectJob(bookingId: currentResponse?.bookingId ?? "", docId: currentResponse?.docId ?? "", auto: true)
//            timer.upstream.connect().cancel()
//        }
//        else{
//            self.counter = initialCounter - diffences
//        }
//    }
//}
//
//
//
//struct additionalInfoOndemand: View{
//    var currentData: FirebaseJobResponse?
//    var body: some View{
//        HStack {
//            Text("Capacity")
//            Spacer()
//            HStack {
//                HStack {
//                    Image("PassengersWhite")
//                        .resizable()
//                        .scaledToFit()
//                        .frame(width: 15, height: 15)
//                    Text("\(currentData?.passenger ?? 0)")
//                        .foregroundColor(.accentColor)
//                    
//                }
//                .padding(.horizontal, 4)
//                HStack {
//                    Image("PetsWhite")
//                        .resizable()
//                        .scaledToFit()
//                        .frame(width: 15, height: 15)
//                    Text("\(currentData?.pet ?? 0)")
//                        .foregroundColor(.accentColor)
//                }
//                .padding(.horizontal, 4)
//                HStack {
//                    Image("WheelchairWhite")
//                        .resizable()
//                        .scaledToFit()
//                        .frame(width: 15, height: 15)
//                    Text("\(currentData?.wheelChair ?? 0)")
//                        .foregroundColor(.accentColor)
//                }
//                .padding(.horizontal, 4)
//            }
//        }
//        Divider()
//        
//        if currentData?.flightNumber != nil {
//            if currentData?.flightNumber != "" {
//                HStack(){
//                    Text("Flight Number")
//                    Spacer()
//                    Text(currentData?.flightNumber ?? "")
//                }
//                Divider()
//            }
//        }
//        
//        if currentData?.driverNote != nil {
//            if currentData?.driverNote != "" {
//                HStack{
//                    Text("Driver Note")
//                    Spacer()
//                    Text(currentData?.driverNote ?? "")
//                }
//            }
//        }
//        
//        HStack(spacing: 8) {
//            HStack {
//                Text("Distance")
//                Text(currentData?.distance ?? "")
//                    .foregroundColor(.accentColor)
//            }
//            Spacer()
//            HStack {
//                Text("Duration")
//                Text(currentData?.duration ?? "")
//                    .foregroundColor(.accentColor)
//            }
//        }
//        .padding(.vertical, 8)
//        .font(.system(size: 16))
//    }
//}
//
//
//struct OnDemandView_Previews: PreviewProvider {
//    static var previews: some View {
//        OnDemandView()
//    }
//}
