//
//  dashbaord.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 10/3/21.
//

import SwiftUI
import Firebase
import AudioToolbox
import AVFoundation
import CoreLocation
import Combine
import Kingfisher

struct DashboardUIView: View {
    @StateObject var VM: DashboardVM = DashboardVM()
    
    @State var shouldPresentImagePicker = false
    @State var shouldPresentActionScheet = false
    @State var shouldPresentCamera = false
    @State var base64Image = ""
    @State var filePath: String?
    @State var uiImage: UIImage?
    @State var image: Image = Image("Default")
    @EnvironmentObject var appRootManager: AppRootManager
    let timer = Timer.publish(every: 15, on: .main, in: .common).autoconnect()
    @EnvironmentObject var appRoot: AppRootManager
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    let ref = Database.database().reference()
    let vibration = Timer.publish(every: 1, tolerance: 0.5, on: .main, in: .common).autoconnect()
    @State private var vibrator = 1
    
    var locationManager = LocationTracker.shared.locationManager
    
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    let hasLaunchedKey = "HasLaunched"
    let defaults = UserDefaults.standard
    @State private var isFirstVisit: Bool = UserDefaults.standard.bool(forKey: "isFirstVisit")
    @State var scrollNav = false
    var body: some View {
        VStack (spacing: 0){
            VStack {
                Text("Profile")
                    .fontWeight(.bold)
            }
            .frame(maxWidth: .infinity)
            .padding(.bottom)
            .padding(.horizontal)
            .background(.ultraThinMaterial.opacity(scrollNav ? 1: 0))
            
            ScrollView {
                VStack {
                    if let image = VM.profileData?.profile_picture_url {
                        KFImage(URL(string: image))
                            .placeholder {
                                Image("default")
                                    .resizable()
                                    .aspectRatio(contentMode: .fill)
                                    .frame(width: 100, height: 100)
                                    .clipShape(Circle())
                                    .opacity(0)
                            }
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 100, height: 100)
                            .clipShape(Circle())
                    }
                    else {
                        self.image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(width: 100, height: 100)
                            .clipShape(Circle())
                        
                    }
                }
                .onTapGesture { self.shouldPresentActionScheet = true }
                .padding(.top)
                
                VStack (spacing: 2){
                    HStack{
                        Text(VM.profileData?.name?.capitalized ?? "")
                            .font(.title3)
                    }
                    .padding(.top, 1)
                    
                    HStack(spacing: 2) {
                        let rating = Double(VM.profileData?.rating ?? "") ?? 0
                        let roundedRating = Int(rating.rounded())
                        ForEach(0..<roundedRating, id: \.self) { _ in
                            Image(systemName: "star.fill")
                                .font(.system(size: 20))
                                .foregroundStyle(Color.accentColor)
                        }
                        ForEach(0..<(5 - roundedRating), id: \.self) { _ in
                            Image(systemName: "star")
                                .font(.system(size: 20))
                                .foregroundStyle(Color.accentColor)
                        }
                    }
                    .padding(.bottom, 10)

                    
                    NavigationLink(destination: AccountUIView()){
                        Text("Edit Your Profile")
                            .font(.system(size: 14))
                    }
                    .padding(.bottom, 10)
                    
                    BadgeView(text: .constant(VM.profileData?.is_approved ?? true ? "Account Approved" : "Account Not Approved"), bgColor: .constant(VM.profileData?.is_approved ?? true ? .accentColor : .red.opacity(0.75)), foregroundColor: .constant(VM.profileData?.is_approved ?? true ? Color("ThemeColor") : Color.black))
                }
                .padding(.horizontal)
                
                if VM.profileData != nil {
                    if !(VM.profileData?.is_approved ?? false){
                        VStack (spacing: 20){
                            HStack (spacing: 15){
                                Image(systemName: "exclamationmark.shield")
                                    .foregroundStyle(Color.accentColor)
                                    .font(.system(size: 30))
                                
                                VStack {
                                    Text("Please upload your vehicle and driver documents. Once uploaded, wait for admin to approve and activate your account.")
                                        .foregroundStyle(Color.black.opacity(0.8))
                                        .fontWeight(.light)
                                        .multilineTextAlignment(.leading)
                                        .font(.system(size: 14))
                                        .fixedSize(horizontal: false, vertical: true)
                                }
                                Spacer()
                            }
                        }
                        .padding(.all, 20)
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(style: StrokeStyle(
                                    lineWidth: 1,
                                    dash:  [5, 4]
                                ))
                                .foregroundColor(Color.black.opacity(0.3))
                        )
                        .padding(.top)
                        .padding(.horizontal)
                    }
                }
                
                VStack(spacing: 20){
                    //                    profileStats
                    

                    if let statement = VM.weekStatement {
                        NavigationLink(destination: StatementsView()) {
                            VStack {
                                HStack {
                                    VStack(alignment: .leading, spacing: 8) {
                                        Text("Weekly earnings")
                                            .font(.system(size: 14))
                                        VStack (alignment: .leading){
                                            Text("$\(statement.amount ?? "")")
                                                .font(.system(size: 24))
                                                .fontWeight(.bold)
                                                .foregroundStyle(Color.accentColor)
                                            HStack (spacing: 8){
                                                Text("\(statement.start_date ?? "") -")
                                                Text("\(statement.end_date ?? "")")
                                                Spacer()
                                            }
                                            .font(.system(size: 16))
                                            .foregroundColor(Color.gray)
                                        }
                                    }
                                    Spacer()
                                    Image(systemName: "dollarsign")
                                        .foregroundStyle(Color.gray.opacity(0.1))
                                        .fontWeight(.bold)
                                        .font(.system(size: 50))
                                    
                                }
                            }
                            .cardStyleModifier()
                            .padding(.horizontal)
                            .padding(.top)
                        }
                    }
                    
                    if VM.currentData != nil {
                        Button(action: {
                            if VM.currentData?.current_status == "completed" {
                                self.appRoot.currentRoot = .ratingScreen(bookingId: "\(VM.currentData?.id ?? 0)")
                                self.router.popToRoot()
                                return;
                            }
                            self.appRoot.currentRoot = .currentRideScreen
                            self.router.popToRoot()
                        }) {
                            currentRideCardView
                                .padding()
                        }
                    }
                    if VM.upcomingData != nil {
                        VStack (alignment: .leading, spacing: 8){
                            Text("Upcoming Ride")
                                .font(.system(size: 20))
                                .fontWeight(.semibold)
                                .multilineTextAlignment(.leading)
                                .fixedSize(horizontal: false, vertical: true)
                                .padding(.horizontal)
                            
                            Button(action : {
                                tabRouter.selectedTab = .ride
                            }) {
                                UpcomingBookingView
                                    .padding(.horizontal)
                                    .padding(.bottom)
                            }
                        }
                        .padding(.top)
                    }
                    if VM.noticeData != nil {
                        AccountCompleteView(data: .constant(VM.noticeData ?? []), VM: VM)
                    }
                    
                    Spacer()
                }
                .padding(.top)
            }
            .simultaneousGesture(
                DragGesture()
                    .onChanged { value in
                        if value.translation.height < 100 {
                            scrollNav = true
                        } else {
                            scrollNav = false
                        }
                    }
                    .onEnded { value in
                        if value.translation.height > 100 {
                            scrollNav = false
                        } else {
                            scrollNav = true
                        }
                    }
            )
            .modifier(
                AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                    VM.showAlert = false
                })
            )
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationBarBackButtonHidden(true)
        .modifier(LoadingView(isPresented: $VM.loading))
//            .navigationBarTitle("Profile", displayMode: .inline)
        .toolbar(.hidden)
//            .navigationBarHidden(false)
        .sheet(isPresented: $VM.ShowBankAccountSheet) {
            NavigationView{
                AddEditBankAccount(showBankAccountSheet: $VM.ShowBankAccountSheet)
            }
        }
        .onDisappear() {
            VM.stopListner()
        }
        .toolbarRole(.editor)
        .sheet(isPresented: $shouldPresentImagePicker, onDismiss: loadImage) {
            UImagePickerView(sourceType: self.shouldPresentCamera ? .camera : .photoLibrary,  image: self.$image, isPresented: self.$shouldPresentImagePicker, uiImage: self.$uiImage,  filePath: self.$filePath)
        }.actionSheet(isPresented: $shouldPresentActionScheet) { () -> ActionSheet in
            ActionSheet(title: Text("Upload Picture"), buttons: [ActionSheet.Button.default(Text("Take Photo"), action: {
                self.shouldPresentImagePicker = true
                self.shouldPresentCamera = true
            }), ActionSheet.Button.default(Text("Choose Photo"), action: {
                self.shouldPresentImagePicker = true
                self.shouldPresentCamera = false
            }), ActionSheet.Button.cancel()])
        }
        .onAppear(){
            VM.initialize()
            print(UserDefaults.standard.string(forKey: "VOIPToken") as Any, "VOIPPPPPPPPPPPPPP")
            UINavigationBar.customizeBackButton()
        }
    }
    func stopTimer() {
        self.timer.upstream.connect().cancel()
    }
    func loadImage() {
        if self.filePath != "" {
            VM.profileImage = self.uiImage ?? UIImage()
            VM.updateProfilePic()
        }
    }
}




extension DashboardUIView {
    var UpcomingBookingView: some View {
        VStack {
            VStack {
                VStack (alignment: .leading, spacing: 10){
                    VStack (spacing: 8){
                        BadgeView(text: .constant("Ride will start soon"), bgColor: .constant(Color.accentColor), foregroundColor: .constant(Color("ThemeColor")))
                        
                    }
                    HStack (spacing: 20){
                        VStack {
                            Text(VM.upcomingData?.pickup_date_time ?? "")
                                .foregroundStyle(Color.accentColor)
                                .font(.system(size: 16))
                                .frame(maxWidth:.infinity, alignment: .leading)
                                .multilineTextAlignment(.leading)
                                .padding(.bottom, 5)
                            VStack (spacing: 8){
                                Text(VM.upcomingData?.drop?.name ?? "")
                                        .font(.system(size: 20))
                                        .lineLimit(3)
                                        .frame(maxWidth:.infinity, alignment: .leading)
                                        .multilineTextAlignment(.leading)
                                
                                HStack (spacing: 3){
                                    Text("Pickup at\(VM.upcomingData?.pickup?.name ?? "")")
                                }
                                .font(.system(size: 12))
                                .foregroundColor(Color.gray)
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                
                            }
                            
                            Text("$\(VM.upcomingData?.fare ?? "")")
                                .font(.system(size: 12))
                                .foregroundColor(Color.gray)
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.top, 3)
                        }
                        Spacer()
                    }
                }
            }
            .foregroundColor(Color.black)
            .padding()
        }
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.gray.opacity(0.5), lineWidth: 2))
        .contentShape(Rectangle())

    }
}



extension DashboardUIView {
    var currentRideCardView: some View {
        VStack {
            VStack {
                VStack (alignment: .leading, spacing: 10){
                    HStack {
                        BadgeView(text: .constant("Mercedes"), bgColor: .constant(Color("Card")), foregroundColor: .constant(Color.black))
                        
                        BadgeView(text: .constant("Ride in progress"), bgColor: .constant(Color.accentColor), foregroundColor: .constant(Color("ThemeColor")))
                        
                    }
                    HStack (spacing: 20){
                        VStack {
                            Text(VM.currentData?.pickup_date_time ?? "")
                                .foregroundStyle(Color.accentColor)
                                .font(.system(size: 16))
                                .frame(maxWidth:.infinity, alignment: .leading)
                                .multilineTextAlignment(.leading)
                                .padding(.bottom, 5)
                            VStack (spacing: 8){
                                Text(VM.currentData?.drop?.name ?? "")
                                        .font(.system(size: 20))
                                        .lineLimit(3)
                                        .frame(maxWidth:.infinity, alignment: .leading)
                                        .multilineTextAlignment(.leading)
                                
                                HStack (spacing: 3){
                                    Text("Pickup at\(VM.currentData?.pickup?.name ?? "")")
                                }
                                .font(.system(size: 12))
                                .foregroundColor(Color.gray)
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                
                            }
                            
                            Text("$\(VM.currentData?.fare ?? "")")
                                .font(.system(size: 12))
                                .foregroundColor(Color.gray)
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .padding(.top, 3)
                        }
                        Spacer()
//                        KFImage(URL(string: VM.currentData?.fleet?.image_url ?? ""))
//                            .placeholder {
//                                Image(.appLogo)
//                                    .resizable()
//                                    .scaledToFit()
//                                    .frame(width: 100)
//                                    .opacity(0)
//                            }
//                            .resizable()
//                            .scaledToFit()
//                            .frame(width: 100)
                    }
                }
            }
            .foregroundColor(Color.black)
            .padding()
        }
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.accentColor, lineWidth: 2))
        .contentShape(Rectangle())

    }
}



//extension DashboardUIView {
//    
//    var menuExtension: some View {
//        VStack {
//            VStack {
//                Text("Menu")
//                    .font(.system(size: 22))
//                    .frame(maxWidth: .infinity, alignment: .leading)
//                    .padding(.bottom)
//                VStack {
//                    
//                    
//                    NavigationLink(destination: InvoicesView()) {
//                        ListWithIcon(
//                            icon: "PdfIcon",
//                            title: "Invoices",
//                            caption: .constant("All Invoices Here")
//                        )
//                    }
//                    
//                    NavigationLink(destination: EmptyView()) {
//                        ListWithIcon(
//                            icon: "InfoCircle",
//                            title: "Terms & Condition",
//                            caption: .constant("")
//                        )
//                    }
//                    
//                    NavigationLink(destination: EmptyView()) {
//                        ListWithIcon(
//                            icon: "InfoCircle",
//                            title: "Policy Policy",
//                            caption: .constant("")
//                        )
//                    }
//                }
//            }
//            
//            HStack (spacing: 18){
//                HStack {
//                    Image("PowerIcon")
//                        .renderingMode(.template)
//                        .resizable()
//                        .scaledToFit()
//                        .frame(width: 14)
//                        .foregroundStyle(Color.gray)
//                }
//                .frame(maxWidth: 22)
//                
//                VStack {
//                    Button(action: {
////                        VM.showLogoutAlert = true
//                    }) {
//                        HStack {
//                            VStack (alignment: .leading){
//                                Text("log out")
//                                    .font(.system(size: 16))
//                                    .foregroundStyle(Color.white)
//                            }
//                            .frame(minHeight: 40)
//                            Spacer()
//                        }
//                    }
//                }
//            }
//            
//            
//            Divider()
//                .frame(height: 1)
//                .overlay(.gray.opacity(0.5))
//                .padding(.vertical, 8)
//                .padding(.trailing, -30)
//            
//            HStack (spacing: 18){
//                Image(.bank)
//                    .renderingMode(.template)
//                    .resizable()
//                    .scaledToFit()
//                    .frame(width: 18)
//                    .foregroundStyle(Color.red.opacity(0.8))
//                    .opacity(0.55)
//                    .padding(.leading, 5)
//                
//                VStack {
//                    Button(action: {
////                        VM.showDeleteAccountAlert = true
//                    }) {
//                        HStack {
//                            VStack (alignment: .leading){
//                                Text("Delete Account")
//                                    .font(.system(size: 16))
//                                    .foregroundStyle(Color.red.opacity(0.8))
//                                    .opacity(0.55)
//                            }
//                            .frame(minHeight: 40)
//                            Spacer()
//                        }
//                    }
//                }
//            }
//        }
//    }
//}


struct DashboardUIView_Previews: PreviewProvider {
    static var previews: some View {
        DashboardUIView()
    }
}

import AVFoundation

class Sound {

    static var player:AVAudioPlayer?

    static func playaudio(soundfile: String) {

        if let path = Bundle.main.path(forResource: soundfile, ofType: nil){
            print("<<<<<<<<<<<<<<<<<<<<<PATH >>>>>>>>>>>>>>>>>>>>>>>>>",path)
            do{

                player = try AVAudioPlayer(contentsOf: URL(fileURLWithPath: path))
                player?.prepareToPlay()
                player?.play()

            }catch {
                print("<<<<<<<<<<<<<<<<<<<<<Error >>>>>>>>>>>>>>>>>>>>>>>>")
            }
        }
    }
}
