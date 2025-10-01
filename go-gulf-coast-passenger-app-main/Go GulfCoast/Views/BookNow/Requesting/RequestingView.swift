////
////  RequestingView.swift
////  Amheer
////
////  Created by Office on 29/06/2022.
////
//


import Foundation
import SwiftUI
import AudioToolbox
import Combine
import AVFoundation

struct RequestingRideView: View {
    @StateObject var VM: requestingRideVM = requestingRideVM()
//    @StateObject private var timerManager = TimerManager()
    
    @State var quoteData: QuoteResponseData?
    @State public var navigateToCurrent = false
    let systemSoundID: SystemSoundID = 101
    @Environment(\.scenePhase) var scenePhase
    @State var animate = false
    @State var navigateToHome = false
    @State var actionPerformed = false
    @State private var retryAlertShown = false
    @State var cancellables = Set<AnyCancellable>()
    @State var counter = 0
    @State var bookingId = ""
    @State var loading = false
    @State var bookingCancelled = false
    @State var bookingAccepted = false
    @Environment(\.presentationMode) var presentationMode
    
    @State var alertShown = false
    @State private var vibrator = 1
    @State private var shouldAnimate = false
    @State var navigateToLandingPage = false
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    @EnvironmentObject var appRootManager: AppRootManager
    
    var body: some View {
            VStack{
                VStack {
                    ZStack() {
                        GoogleMapsRoutes(
                            fromLat: quoteData?.pickup?.lat,
                            fromLng: quoteData?.pickup?.lng,
                            toLat: quoteData?.drop?.lat,
                            toLng: quoteData?.drop?.lng,
                            route: quoteData?.route
                        )
                            .edgesIgnoringSafeArea(.all)
                            .padding(.top, -65)
                            .blur(radius: 5)
                        
                        VStack {
                            Spacer()
                            Text("\(VM.timeString(from: VM.remainingTime))")
                                .font(.system(size: 44))
                                .fontWeight(.bold)
                            ThreeBounceAnimation()
                            
                            Spacer()
                            VStack(spacing: 8){
                                Text("Pickup at")
                                    .font(.system(size: 20))
                                Text(quoteData?.pickup_date_time ?? "")
                                    .fontWeight(.bold)
                                    .font(.largeTitle)
                            }
                            .padding(.bottom, 40)
                            .font(.title3)
                        }
                    }
                    .padding(.bottom, -26)
                    
                    VStack()  {
                        VStack {
                            VStack(alignment: .center) {
                                Text("Requesting Driver")
                                    .font(.system(size: 20))
                                    .textCase(.uppercase)
                                    .foregroundColor(Color.accentColor)
                            }
                            .padding(.vertical, 8)
                            .padding(.top, 30)
                            Divider()
                            
                            VStack (alignment: .leading, spacing: 18){
                                HStack {
                                    VStack (alignment: .leading, spacing: 20){
                                        VStack(alignment: .leading) {
                                            Text("Pick Up")
                                                .font(.system(size: 14))
                                                .foregroundColor(.accentColor)
                                            Text(quoteData?.pickup?.name ?? "")
                                                .font(.system(size: 16))
                                        }
                                        VStack(alignment: .leading) {
                                            Text("Destination")
                                                .font(.system(size: 14))
                                                .foregroundColor(.accentColor)
                                            Text(quoteData?.drop?.name ?? "")
                                                .font(.system(size: 16))
                                        }
                                    }
                                    .font(.system(size: 18))
                                    .opacity(0.80)
                                    Spacer()
                                }
                            }
                            .padding(.vertical, 8)
                            Divider()
                            
                            HStack {
                                HStack {
                                    Text("Distance")
                                    Text(quoteData?.distance ?? "")
                                        .foregroundColor(.accentColor)
                                }
                                Spacer()
                                HStack {
                                    Text("Duration")
                                    Text(quoteData?.duration ?? "")
                                        .foregroundColor(.accentColor)
                                }
                            }
                            .font(.system(size: 16))
                            .padding(.vertical, 8)
                            
                            Divider()
                            
                            Text("$\(quoteData?.fare ?? "")")
                                .font(.system(size: 36))
                                .fontWeight(.bold)
                                .foregroundColor(Color("AccentColor"))
                            
                            HStack (alignment: .center, spacing: 30){
                                Spacer()
                                Button(action: {
                                    VM.rejectBooking()
                                }){
                                    UIButton(label: "Cancel", bgColor: .red, color: .white)
                                }
                                .padding(.bottom)
                                Spacer()
                            }
                            .padding(.bottom)
                        }
                        .padding(.horizontal)
                        .background(Color.linearGradient)
                        
                    }
                    .cornerRadius(30)
                    .shadow(color: Color("GrayShadow") , radius: 10)
                    .edgesIgnoringSafeArea(.all)
                }
            }
            .alert(isPresented: $VM.showRetryAlert) {
                Alert(
                    title: Text(VM.quoteData?.title ?? ""),
                    message: Text(VM.quoteData?.message ?? ""),
                    primaryButton: .destructive(Text("Retry")) {
                        VM.retryBooking()
                    },
                    secondaryButton: .cancel(){
                        self.appRootManager.currentRoot = .tabs
                        self.router.popToRoot()
                    }
                )
            }
            .onReceive(VM.viewDismissalModePublisher) { shouldDismiss in
                if shouldDismiss {
                    self.presentationMode.wrappedValue.dismiss()
                }
            }
        
            .modifier(
                AlertView(isPresented: $VM.showSuccess, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, primaryAction: {
                    tabRouter.selectedTab = .ride
                })
            )
        
            .modifier(
                AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                    VM.showAlert = false
                })
            )
            
        
            .padding(.bottom, 0)
            .background(.clear)
            .edgesIgnoringSafeArea(.all)
            .navigationBarBackButtonHidden(true)
            .modifier(LoadingView(isPresented: $VM.loading))
            .onAppear(){
                VM.initialize(tabRouter: tabRouter)
            }
            .onDisappear() {
                VM.stopListener()
            }
    }
 
}
