//
//  RequestingDriverView.swift
//  GoGulf
//
//  Created by Mac on 12/25/21.
//

import SwiftUI
import Firebase
import UIKit
import Combine
import Kingfisher

struct CurrentRideView: View {
    @StateObject var VM: CurrentRideVM = CurrentRideVM()
    @Environment(\.scenePhase) var scenePhase
    
    @State var showOTPAlert = false
    @State var otpCode = ""
    @State var cancellables = Set<AnyCancellable>()
    let identity = UserDefaults.standard.string(forKey: "identity")
    let token = (UserDefaults.standard.string(forKey: "accessToken") ?? "");
    @State var bkId = ""
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    @State var showSheet = true
    
    
    var body: some View {
        
        VStack (spacing: 0){
            ZStack {
                VStack {
                    ZStack (alignment: .top){
                        if VM.currentData != nil {
                            GoogleMapPlotRoute(fromLat: VM.currentData?.pickup?.lat ?? 0, fromLng: VM.currentData?.pickup?.lng ?? 0, toLat: VM.currentData?.drop?.lat ?? 0, toLng: VM.currentData?.drop?.lng ?? 0,  route: VM.currentData?.route)
                                .edgesIgnoringSafeArea(.all)
                                .padding(.bottom, 120)
                        }
                        
                        
                    }
                }
                if VM.showOTPAlert {
                    OTPCodeAlert(data: $VM.otpData)
                        .padding(.top, -100)
                }
            }
        }
        .sheet(isPresented: $showSheet) {
            SheetCurrentRide(VM: VM, handle: {
            })
        }
        .onAppear(){
            self.showSheet = true
            VM.initialize()
        }
        .onDisappear() {
            self.showSheet = false
            VM.stopListener()
        }
        .toolbarRole(.editor)
        .background(.clear)
        .edgesIgnoringSafeArea(.all)
        .padding(.bottom, 0)
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                Button (action : {
                    self.showSheet = false
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.05) {
                        self.appRootManager.currentRoot = .tabs
                        self.router.popToRoot()
                    }
                }) {
                    Image("ProfileFill")
                        .renderingMode(.template)
                        .resizable()
                        .foregroundStyle(Color("ThemeColor"))
                        .padding(.all, 8)
                        .frame(width: 40, height: 40)
                        .background(Color.accentColor)
                        .clipShape(RoundedRectangle(cornerRadius: 50))
                }
            }
        }
    }
}


struct OTPCodeAlert: View {
    @Binding var data: OTPCodeModel?
    var body: some View{
        ZStack{
            Color(.systemBackground)
                .ignoresSafeArea()
                .opacity(0.75)
            VStack {
                VStack (alignment: .center, spacing: 20) {
                    Image(systemName: "shared.with.you")
                        .foregroundColor(.accentColor.opacity(0.8))
                        .font(.system(size: 60))
                    Text(data?.message ?? "")
                        .fontWeight(.semibold)
                        .foregroundStyle(.black.opacity(0.6))
                        .font(.system(size: 20))
                        .multilineTextAlignment(.center)
                    Divider()
                    HStack {
                        Text("\(String(data?.otp ?? 0))")
                            .font(.system(size: 32))
                            .kerning(15.0)
                            .fontWeight(.bold)
                    }
                }
                .padding(.vertical, 30)
            }
            .padding()
            .cardStyleModifier()
            .transition(AnyTransition.opacity.animation(.easeInOut(duration: 0.2)))
            .zIndex(1)
            .padding(.horizontal, 50)
        }
        .background(Color.black.opacity(0.25))
    }
}




struct SheetCurrentRide: View {
    @EnvironmentObject var router: Router
    @ObservedObject var VM: CurrentRideVM
    var handle: (() -> Void)
    @State private var settingsDetent = PresentationDetent.height(150)
    
    var body: some View {
        VStack {
            ScrollView {
                VStack()  {
                    VStack {
                        VStack(alignment: .center) {
                            
                            Text(VM.currentData?.status_title ?? "")
                                .font(.system(size: 20))
                                .textCase(.uppercase)
                                .foregroundColor(Color.accentColor)
                            
                            
                        }
                        .padding(.vertical, 8)
                        Divider()
                        HStack(spacing: 15){
                            KFImage(URL(string: VM.currentData?.driver?.profile_picture_url ?? ""))
                                .placeholder {
                                    Image("DefaultClientPic")
                                        .resizable()
                                        .aspectRatio(contentMode: .fill)
                                        .frame(width: 80, height: 80)
                                        .clipShape(Circle())
                                    
                                }
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(width: 80, height: 80)
                                .clipShape(Circle())
                            
                            VStack(alignment: .leading){
                                Text(VM.currentData?.driver?.name ?? "")
                                    .font(.system(size: 18))
                                    .fontWeight(.bold)
                                    .padding(.bottom, 4)
                                if VM.currentData?.current_status != "pob" {
                                    HStack (alignment: .center, spacing: 30){
                                        Button(action: {
                                            let phone = "tel://"
                                            let phoneNumberformatted = phone + (VM.currentData?.driver?.mobile ?? "")
                                            guard let url = URL(string: phoneNumberformatted) else { return }
                                            UIApplication.shared.open(url)
                                        }) {
                                            Image("Call")
                                        }
                                        Button(action: {
                                            let sms: String = "sms:\(VM.currentData?.driver?.mobile ?? "")&body="
                                            let strUrl: String = sms.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
                                            UIApplication.shared.open(URL.init(string: strUrl)!, options: [:], completionHandler: nil)
                                        }) {
                                            Image("Message")
                                        }
                                    }
                                }
                            }
                            Spacer()
                        }
                        Divider()
                        vehicleCard(currentData: VM.currentData)
                        Divider()
                        additionalInfoCurrent(currentData: VM.currentData)
                        VStack{
                            HStack(spacing: 8) {
                                HStack {
                                    Text("Distance")
                                    Text("\(VM.currentData?.distance ?? "")")
                                        .foregroundColor(.accentColor)
                                }
                                Spacer()
                                HStack {
                                    Text("Duration")
                                    Text("\(VM.currentData?.duration ?? "")")
                                        .foregroundColor(.accentColor)
                                }
                            }
                            .padding(.vertical, 8)
                            .font(.system(size: 16))
                        }
                        Divider()
                        VStack(alignment: .center, spacing: 5){
                            Text("$\(VM.currentData?.fare ?? "")")
                                .font(.system(size: 27))
                                .fontWeight(.bold)
                                .foregroundColor(Color("AccentColor"))
                            Text("Inclusive GST")
                                .font(.caption2)
                            Spacer()
                        }
                        .padding(.vertical)
                        .padding(.bottom)
                    }
                    .padding(.horizontal)
                    .padding(.top, 30)
                    //            .cornerRadius(30)
                    //                    .background(Color.linearGradient)
                }
                //                .cornerRadius(30)
                //                .shadow(color: Color("GrayShadow"), radius: 10)
                .edgesIgnoringSafeArea(.all)
            }
        }
        .disabled(VM.showOTPAlert)
        .presentationBackgroundInteraction(.enabled)
        .padding(.vertical, 5)
        .padding(.horizontal)
        .presentationContentInteraction(.resizes)
        .presentationDragIndicator(.visible)
        .presentationCompactAdaptation(.sheet)
        .interactiveDismissDisabled(true)
        .presentationDetents(
            [.height(180), .large],
            selection: $settingsDetent
        )
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.linearGradient.ignoresSafeArea(edges: .all))
        .onAppear() {
            print(VM.currentData as Any, "curerertererererefr ")
        }
    }
}


struct additionalInfoCurrent: View{
    var currentData: CurrentBookingModel?
    var body: some View{
        HStack {
            Text("Capacity")
            Spacer()
            HStack {
                HStack {
                    Image("PassengersWhite")
                        .renderingMode(.template)
                        .resizable()
                        .foregroundStyle(Color.black)
                        .scaledToFit()
                        .frame(width: 15, height: 15)
                    Text("\(currentData?.passenger_count ?? 0)")
                    
                }
                .padding(.horizontal, 4)
                HStack {
                    Image("PetsWhite")
                        .renderingMode(.template)
                        .resizable()
                        .foregroundStyle(Color.black)
                        .scaledToFit()
                        .frame(width: 15, height: 15)
                    Text("\(currentData?.pet_count ?? 0)")
                }
                .padding(.horizontal, 4)
                HStack {
                    Image("WheelchairWhite")
                        .renderingMode(.template)
                        .resizable()
                        .foregroundStyle(Color.black)
                        .scaledToFit()
                        .frame(width: 15, height: 15)
                    Text("\(currentData?.wheelchair_count ?? 0)")
                }
                .padding(.horizontal, 4)
            }
        }
        Divider()
        
        if currentData?.flight_number != nil {
            if currentData?.flight_number != "" {
                HStack(){
                    Text("Flight Number")
                    Spacer()
                    Text(currentData?.flight_number ?? "")
                }
                Divider()
            }
        }
        
        if currentData?.description != nil {
            HStack{
                Text("Driver Note")
                Spacer()
                Text(currentData?.description ?? "")
            }
        }
    }
}

struct vehicleCard: View{
    var currentData: CurrentBookingModel?
    var body: some View {
        HStack(alignment: .center){
            HStack (alignment: .bottom) {
                VStack (alignment: .leading, spacing: 10){
                    BadgeView(text: .constant("\(currentData?.fleet?.class_name ?? "")"), bgColor: .constant(Color.accentColor), foregroundColor: .constant(Color(.theme)))
                    
                    Text("\(currentData?.fleet?.type_name ?? "")")
                        .font(.system(size: 15))
                        .fontWeight(.bold)
                        .foregroundColor(Color.accentColor)
                    Text("\(currentData?.fleet?.color ?? "") | Rego: \(currentData?.fleet?.registration_number?.uppercased() ?? "")")
                        .font(.system(size: 15))
                }
            }
            Spacer()
            
            KFImage(URL(string: currentData?.fleet?.image_url ?? ""))
                .placeholder {
                    Image(.default)
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 120, height: 90)
                        .offset(x: 50, y: 0)
                        .opacity(0)
                }
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(width: 120, height: 90)
                .offset(x: 50, y: 0)
        }
    }
}
