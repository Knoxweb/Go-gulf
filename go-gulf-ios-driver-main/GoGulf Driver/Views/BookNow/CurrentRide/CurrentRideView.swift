//
//  RequestingDriverView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/25/21.
//

import SwiftUI
import Firebase
import UIKit
import Kingfisher
import Combine


struct CurrentRideView: View {
    @StateObject var VM: CurrentRideVM = CurrentRideVM()
    @State var bookingId: String?
    @State var animate = false
    
    @State var buttonLabel = ""
    @State var mode = ""
    @State var showPickupDateTime = true
    @State private var animationAmount = 1.0
    @State public var appliedLabel = "Arrived at Pickup"
    @State var locationManager = LocationManager()
    @State var showSheet = true
    @State private var counter = 0
    //    @State private var timer: AnyCancellable?
    private let debouncer = Debouncer(delay: 15)
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    let timer = Timer.publish(every: 10, on: .main, in: .common).autoconnect()
    
    @State private var isFirstVisit: Bool = UserDefaults.standard.bool(forKey: "isFirstVisit")
    let identity = UserDefaults.standard.string(forKey: "identity")
    let ref = Database.database().reference()
    
    var userLatitude: Double {
        return locationManager.lastLocation?.coordinate.latitude ?? -33.868820
    }
    
    var userLongitude: Double {
        return locationManager.lastLocation?.coordinate.longitude ?? 151.209290
    }
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        VStack (spacing: 0){
            ZStack{
                ZStack (alignment: .top){
                    if VM.currentData != nil {
                        GoogleMapPlotRoute(fromLat: VM.currentData?.pickup?.lat ?? 0, fromLng: VM.currentData?.pickup?.lng ?? 0, toLat: VM.currentData?.drop?.lat ?? 0, toLng: VM.currentData?.drop?.lng ?? 0,  route: VM.currentData?.route)
                            .edgesIgnoringSafeArea(.all)
                            .padding(.bottom, 120)
                    }
                    HStack {
                        Spacer()
                        VStack{
                            if VM.noShowUpButton {
                                Button(action: {
                                    VM.noShowAlert = true
                                }){
                                    Image(systemName: "eye.slash.fill")
                                        .frame(width: 55, height: 55)
                                        .foregroundColor(.black)
                                        .background(Color.red)
                                        .clipShape(Circle())
                                }
                                .padding(.bottom, 10)
                            }
                            
                            if VM.showGoogleNavigation {
                                Button(action: {
                                    VM.navigateOnGoogleMap(fromLat: VM.currentData?.pickup?.lat ?? 0, fromLng: VM.currentData?.pickup?.lng ?? 0, toLat: VM.currentData?.drop?.lat ?? 0, toLng: VM.currentData?.drop?.lng ?? 0)
                                }){
                                    Image(systemName: "paperplane.fill")
                                        .frame(width: 55, height: 55)
                                        .foregroundColor(Color("Color"))
                                        .background(Color("AccentColor"))
                                        .clipShape(Circle())
                                }
                                Button(action: {
                                    if VM.currentData?.current_status == "pob" {
                                        if(VM.currentData?.drop?.lat != 0 && VM.currentData?.drop?.lng != 0){
                                            VM.openWazeWithDirections(toLat: VM.currentData?.drop?.lat ?? 0, toLng: VM.currentData?.drop?.lng ?? 0)
                                        }
                                    }
                                    else {
                                        if(VM.currentData?.pickup?.lat != 0 && VM.currentData?.pickup?.lng != 0){
                                            VM.openWazeWithDirections(toLat: VM.currentData?.pickup?.lat ?? 0, toLng: VM.currentData?.pickup?.lng ?? 0)
                                        }
                                    }
                                }){
                                    Image("waze-logo")
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 50)
                                }
                            }
                            Spacer()
                        }
                        .padding(.trailing)
                    }
                    
                }
                if VM.showOTPAlert {
                    OTPAlert(VM: VM)
                        .padding(.top, -150)
                }
                if VM.noShowAlert {
                    NoShowUpUIView(noShowAlert: $VM.noShowAlert, VM: VM)
                        .padding(.top, -150)
                }
                if VM.loading {
                    ActivityIndicator()
                }
                
                
            }
            .disabled(VM.loading)
            .sheet(isPresented: $showSheet) {
                SheetCurrentRide(VM: VM, handle: {
                })
                .modifier(
                    AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                        VM.showAlert = false
                    })
                )
            }
        }
        .onAppear(){
            self.showSheet = true
            VM.initialize()
            VM.updatePosition()
        }
        .onReceive(timer) { input in
            VM.updatePosition()
        }
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
        //        .modifier(LoadingView(isPresented: $VM.loading))
        .onDisappear(){
            self.showSheet = false
            VM.stopListener()
            stopTimer()
        }
    }
    func stopTimer() {
        self.timer.upstream.connect().cancel()
    }
}


struct NoShowUpUIView: View {
    @Binding var noShowAlert: Bool
    @ObservedObject var VM: CurrentRideVM
    @EnvironmentObject var tabRouter: TabRouter
    
    var body: some View{
        ZStack{
            Color(.systemBackground)
                .ignoresSafeArea()
                .opacity(0.75)
            VStack {
                VStack (alignment: .center, spacing: 10) {
                    Image(systemName: "eye.slash.circle")
                        .foregroundColor(.accentColor.opacity(0.8))
                        .font(.system(size: 60))
                    Text("No show up")
                        .fontWeight(.semibold)
                        .foregroundStyle(.white.opacity(0.9))
                        .font(.system(size: 20))
                        .multilineTextAlignment(.center)
                    
                    Text("Are you sure, Passenger not shown up at pickup location?")
                        .multilineTextAlignment(.center)
                        .fixedSize(horizontal: false, vertical: true)
                        .foregroundStyle(Color.gray)
                    Divider()
                    HStack {
                        Button(action: {
                            self.noShowAlert = false
                        }) {
                            Text("Cancel")
                                .frame(maxWidth: .infinity)
                                .foregroundStyle(.red)
                        }
                        HStack {
                            Spacer()
                            Divider()
                            Spacer()
                        }
                        .frame(height: 30)
                        
                        Button(action: {
                            VM.noShowUp(for: VM.currentData?.id, tabRouter: tabRouter)
                        }) {
                            Text("Yes")
                                .padding()
                                .fontWeight(.semibold)
                                .frame(width: 120)
                                .frame(height: 50)
                                .foregroundColor(Color("ThemeColor"))
                                .background(Color.accentColor)
                                .cornerRadius(8)
                        }
                    }
                }
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






struct OTPAlert: View {
    @ObservedObject var VM: CurrentRideVM
    let textBoxWidth = UIScreen.main.bounds.width / 12
    let textBoxHeight = UIScreen.main.bounds.width / 12
    let spaceBetweenLines: CGFloat = 5
    let paddingOfBox: CGFloat = 1
    var textFieldOriginalWidth: CGFloat {
        (textBoxWidth*6)+(spaceBetweenLines*3)+((paddingOfBox*2)*3)
    }
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    @State private var keyboardHeight: CGFloat = 0
    @FocusState private var isTextFieldFocused: Bool
    
    var body: some View{
        ZStack{
            Color(.systemBackground)
                .ignoresSafeArea()
                .opacity(0.75)
                .onTapGesture {
                    VM.isEditing = false
                    VM.showOTPAlert = false
                    UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
                }
            VStack {
                VStack (alignment: .center, spacing: 10) {
                    Image(systemName: "lock.app.dashed")
                        .foregroundColor(.accentColor.opacity(0.8))
                        .font(.system(size: 60))
                    Text("Please enter an OTP code sent on your passenger's app.")
                        .fontWeight(.semibold)
                        .foregroundStyle(.black)
                        .font(.system(size: 16))
                        .multilineTextAlignment(.center)
                    ZStack {
                        HStack (spacing: spaceBetweenLines){
                            otpText(text: VM.otp1, isNextTyped: $VM.isNextTypedArr[0])
                            otpText(text: VM.otp2, isNextTyped: $VM.isNextTypedArr[1])
                            otpText(text: VM.otp3, isNextTyped: $VM.isNextTypedArr[2])
                            otpText(text: VM.otp4, isNextTyped: $VM.isNextTypedArr[3])
                        }
                        
                        
                        TextField("", text: $VM.otpField) { isEditing in
                            VM.isEditing = isEditing
                        }
                        .textContentType(.oneTimeCode)
                        .foregroundColor(.clear)
                        .accentColor(.clear)
                        .background(Color.clear)
                        .keyboardType(.numberPad)
                    }
                    
                    Divider()
                    HStack {
                        Button(action: {
                            VM.isEditing = false
                            VM.showOTPAlert = false
                        }) {
                            Text("Cancel")
                                .frame(maxWidth: .infinity)
                                .foregroundStyle(.red)
                        }
                        HStack {
                            Spacer()
                            Divider()
                            Spacer()
                        }
                        .frame(height: 30)
                        
                        Button(action: {
                            VM.POB(for: VM.currentData?.id)
                        }) {
                            Text("Confirm")
                                .padding()
                                .fontWeight(.semibold)
                                .frame(width: 120)
                                .frame(height: 50)
                                .foregroundColor(Color("ThemeColor"))
                                .background(Color.accentColor)
                                .cornerRadius(8)
                        }
                    }
                }
            }
            .padding(.all, 10)
            .cardStyleModifier()
            .transition(AnyTransition.opacity.animation(.easeInOut(duration: 0.2)))
            .zIndex(1)
            .padding(.horizontal, 50)
        }
        .background(Color.black.opacity(0.25))
        
    }
    
    func otpText(text: String, isNextTyped: Binding<Bool>) -> some View {
        return Text(text)
            .font(.title)
            .fontWeight(.bold)
            .frame(width: textBoxWidth, height: textBoxHeight)
            .background(VStack{
                Spacer()
                RoundedRectangle(cornerRadius: 1)
                    .frame(height: 2)
                    .foregroundColor(isNextTyped.wrappedValue ? Color.accentColor : .gray.opacity(0.5))
            })
            .padding(paddingOfBox)
        
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
                            Text(VM.currentData?.status_title ?? "Ride in progress")
                                .font(.system(size: 20))
                                .textCase(.uppercase)
                                .foregroundColor(Color.accentColor)
                            
                            
                        }
                        .padding(.vertical, 8)
                        Divider()
                        HStack(spacing: 15){
                            if let image = VM.currentData?.passenger?.profile_picture_url{
                                
                                KFImage(URL(string: image))
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
                            }
                            
                            VStack(alignment: .leading){
                                Text(VM.currentData?.passenger?.name ?? "")
                                    .font(.system(size: 18))
                                    .fontWeight(.bold)
                                    .padding(.bottom, 4)
                                
                                if VM.currentData?.current_status != "pob" {
                                    HStack (alignment: .center, spacing: 30){
                                        Button(action: {
                                            let phone = "tel://"
                                            let phoneNumberformatted = phone + (VM.currentData?.passenger?.mobile ?? "")
                                            guard let url = URL(string: phoneNumberformatted) else { return }
                                            UIApplication.shared.open(url)
                                        }) {
                                            Image("Call")
                                        }
                                        Button(action: {
                                            let sms: String = "sms:\(VM.currentData?.passenger?.mobile ?? "")&body="
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
                    .background(Color.linearGradient)
                }
                .cornerRadius(30)
                .shadow(color: Color("GrayShadow"), radius: 10)
                .edgesIgnoringSafeArea(.all)
            }
            
            Button(action: {
                if VM.currentData?.current_status == "dow" {
                    VM.DOD(for: VM.currentData?.id)
                }  else if VM.currentData?.current_status == "dod" {
                    VM.showOTPAlert = true
                    //                    VM.POB(for: VM.currentData?.id)
                    //                }  else if VM.currentData?.current_status == "pob" {
                }
                else {
                    VM.endTrip(for: VM.currentData?.id)
                }
            }) {
                if VM.currentData?.current_status == "dow" {
                    Text("Reached at Pickup")
                        .fullWithButton()
                }
                else if VM.currentData?.current_status == "dod" {
                    Text("Passenger Onboard")
                        .fullWithButton()
                }
                else if VM.currentData?.current_status == "pob" {
                    Text("End Trip")
                        .fullWithButton()
                }
            }
            .disabled(VM.isLoading)
            .padding(.horizontal)
        }
        .disabled(VM.showOTPAlert)
        .presentationBackgroundInteraction(.enabled)
        .padding(.horizontal)
        .presentationContentInteraction(.resizes)
        .presentationDragIndicator(.visible)
        .modifier(LoadingView(isPresented: $VM.isLoading))
        .presentationCompactAdaptation(.sheet)
        .interactiveDismissDisabled(true)
        .presentationDetents(
            [.height(240), .large],
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
                        .resizable()
                        .scaledToFit()
                        .frame(width: 15, height: 15)
                    Text("\(currentData?.passenger_count ?? 0)")
                    
                }
                .padding(.horizontal, 4)
                HStack {
                    Image("PetsWhite")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 15, height: 15)
                    Text("\(currentData?.pet_count ?? 0)")
                }
                .padding(.horizontal, 4)
                HStack {
                    Image("WheelchairWhite")
                        .resizable()
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
        
        if currentData?.description != "" {
            HStack{
                Text("Passenger Note")
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
