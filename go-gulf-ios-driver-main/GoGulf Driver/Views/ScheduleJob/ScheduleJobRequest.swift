//
//  ScheduleJobRequest.swift
// SlyykDriverDriver
//
//  Created by Office on 26/07/2022.
//

//import Foundation
//import SwiftUI
//import AudioToolbox
//import Kingfisher
////
//struct ScheduleJobRequestView: View {
//    @StateObject var viewModel = ScheduleJobVM()
//    var currentResponse: ScheduleJobResponseData?
//
//    @State var animate = false
//    let timer = Timer.publish(every: 1, tolerance: 0.5, on: .main, in: .common).autoconnect()
//    let vibration = Timer.publish(every: 2, tolerance: 0.5, on: .main, in: .common).autoconnect()
//    @State private var counter = 120
//    @State private var vibrator = 1
//    @State private var animationAmount = 1.0
//    @State var loading = false
//    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
//
//    var body: some View {
//        ZStack {
//            Text("")
////            VStack{
////                ZStack {
////                    GoogleMapsRoutes(fromLat: currentResponse?.fromLat, fromLng: currentResponse?.fromLng, toLat: currentResponse?.toLat, toLng: currentResponse?.toLng, bookingId: "", polylines: curre)
////                        .edgesIgnoringSafeArea(.all)
////                        .offset(x: 0, y: 40)
////                        .blur(radius: 3)
////                    VStack (alignment: .center){
////                        VStack(spacing: 8){
////                            Text("Scheduled at")
////                                .font(.body)
////                            Text(currentResponse?.pickupDatetime ?? "")
////                                .fontWeight(.bold)
////                        }
////                        .padding(.top, 70)
////                        .font(.title3)
////                        Spacer()
////                        VStack {
////
////                            ZStack{
////                                ZStack {
////                                    Circle().fill(Color.white.opacity(0.08)).frame(width: 200, height:200).scaleEffect(self.animate ? 1 : 0)
////                                    Circle().fill(Color.white.opacity(0.16)).frame(width: 200, height: 150).scaleEffect(self.animate ? 1 : 0)
////                                }
////                                .onAppear{
////                                    self.animate.toggle()
////                                    animationAmount = 1.5
////                                }
////                                .animation(
////                                    .easeInOut(duration: 1.5)
////                                    .repeatForever(autoreverses: false),
////                                    value: animationAmount
////                                )
////                                KFImage(URL(string: currentResponse?.passengerUser?.imageLink ?? ""))
////                                    .placeholder {
////                                        Image("default")
////                                            .imageScale(.large)
////                                            .foregroundColor(.gray)
////                                            .frame(width: 90, height: 90)
////                                            .clipShape(Circle())
////                                            .shadow(radius: 20)
////                                            .opacity(0)
////                                    }
////                                    .resizable()
////                                    .aspectRatio(contentMode: .fill)
////                                    .frame(width: 90, height: 90)
////                                    .clipShape(Circle())
////                                    .shadow(radius: 20)
////
////                                VStack {
////                                    Text("\(self.counter)")
////                                        .font(.system(size: 44))
////                                        .fontWeight(.bold)
////                                        .offset(x: 0, y: -80)
////
////                                    Text("$\(String(format:"%.2f", currentResponse?.totalFare ?? 0.0))")
////                                        .font(.system(size: 36))
////                                        .fontWeight(.bold)
////                                        .foregroundColor(.accentColor)
////                                        .offset(x: 0, y: 80)
////                                }
////                            }
////                            Spacer()
////                        }
////                        .offset(x: 0, y: 90)
////                    }
////                    .frame(maxWidth: .infinity, maxHeight: .infinity)
////
////                }
////                Spacer()
////                VStack()  {
////                    VStack {
////                        VStack(alignment: .center) {
////                            Text("Scheduled Job Request")
////                                .font(.system(size: 15))
////                                .textCase(.uppercase)
////                                .foregroundColor(Color.accentColor)
////                        }
////                        .padding(.horizontal)
////                        .padding(.vertical, 8)
////                        Divider()
////
////                        VStack (alignment: .leading, spacing: 18){
////                            HStack {
////                                VStack (alignment: .leading, spacing: 10){
////                                    Text(currentResponse?.fromLocation ?? "")
////                                    Text(currentResponse?.toLocation ?? "")
////                                }
////                                .font(.system(size: 18))
////                                .opacity(0.80)
////                                Spacer()
////                            }
////                        }
////                        .padding(.horizontal)
////                        .padding(.vertical, 8)
////                        Divider()
////
////                        HStack {
////                            HStack {
////                                Text("Distance")
////                                Text(currentResponse?.distance ?? "")
////                                    .foregroundColor(.accentColor)
////                            }
////                            Spacer()
////                            HStack {
////                                Text("Duration")
////                                Text(currentResponse?.duration ?? "")
////                                    .foregroundColor(.accentColor)
////                            }
////                        }
////                        .font(.system(size: 16))
////                        .padding(.horizontal)
////                        .padding(.vertical)
////
////                        HStack (alignment: .center, spacing: 30){
////                            Button(action: {
////                                viewModel.rejectJob(bookingId: currentResponse?.bookingId ?? "")
////                            }){
////                                UIButton(label: "Reject", bgColor: .red, color: .white)
////                            }
////                            Button(action: {
////                                viewModel.acceptJob(bookingId: currentResponse?.bookingId ?? "")
////                            }){
////                                UIButton(label: "Accept", bgColor: .green, color: .white)
////                            }
////                        }
////                        .padding(.horizontal)
////                        .padding(.bottom, 45)
////                        .onReceive(viewModel.viewDismissalModePublisher) { shouldDismiss in
////                            if shouldDismiss {
////                                self.presentationMode.wrappedValue.dismiss()
////                            }
////                        }
////                    }
////                    .background(Color.linearGradient)
////                }
////                .cornerRadius(30)
////                .edgesIgnoringSafeArea(.all)
//////                NavigationLink(destination: ScheduledBookingsView(showMenu: true), isActive: $viewModel.navigate) {}.opacity(0)
////            }
////            .background(.clear)
////            .navigationBarBackButtonHidden(viewModel.loading)
////            .edgesIgnoringSafeArea(.all)
////            .navigationBarBackButtonHidden(true)
////            .onReceive(vibration) { v in
////                if vibrator == 10 {
////                    vibration.upstream.connect().cancel()
////                    return;
////                } else {
////                    AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
////                }
////                vibrator += 1
////            }
////            .onAppear(){
////                AudioServicesPlayAlertSoundWithCompletion(SystemSoundID(kSystemSoundID_Vibrate)) {}
////            }
////            .onDisappear() {
////                vibration.upstream.connect().cancel()
////                timer.upstream.connect().cancel()
////            }
////            .onReceive(timer) { time in
////                if counter == 0 {
////                    timer.upstream.connect().cancel()
////                    viewModel.rejectJob(bookingId: currentResponse?.bookingId ?? "")
////                    return;
////                } else {
////                    self.getRemainingSecond()
////                }
////                counter -= 1
////            }
////            if viewModel.loading {
////                ActivityIndicator()
////            }
////        }
////        .disabled(viewModel.loading)
//    }
//
//    func getRemainingSecond(){
//        let createdDate = currentResponse?.createdAtFormatUTC;
//        let createdFormatter = DateFormatter()
//        createdFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss a Z"
//        let createdInterval = createdFormatter.date(from: createdDate ?? "")!.timeIntervalSince1970
//        let created = Int(createdInterval * 1000)
//
//
//        let dateFormatter = DateFormatter()
//        dateFormatter.dateFormat = "yyyy-MM-dd hh:mm:ss a Z"
//        dateFormatter.timeZone = TimeZone(identifier: "UTC") // fixes nil if device time in 24 hour format
//        let date = dateFormatter.string(from: Date())
//        //        print(date, "dddddddddd")
//        let currentFormatter = DateFormatter()
//        currentFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss a Z"
//        let currentInterval = currentFormatter.date(from: date)!.timeIntervalSince1970
//        //        print(currentInterval, "dfdffdfdfdfdfdrrrrrrrrr")
//
//        let now = Int(currentInterval * 1000);
//        let diffences = (now - created) / 1000
//        print(diffences, "differencesssss")
//        if(diffences >= 120) {
//            print("greater than 120 -----------")
//            viewModel.rejectJob(bookingId: currentResponse?.bookingId ?? "")
//            timer.upstream.connect().cancel()
//        }
//        else{
//            self.counter = 120 - diffences
//        }
//    }
//}
//struct ScheduleJobRequestView_Previews: PreviewProvider {
//    static var previews: some View {
//        ScheduleJobRequestView()
//    }
//}
