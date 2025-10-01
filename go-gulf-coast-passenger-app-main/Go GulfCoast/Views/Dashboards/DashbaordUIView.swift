//
//  dashbaord.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 10/3/21.
//

import SwiftUI
import UIKit
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
    //    @State var UIimage: UIImage?
    @State var cancellables = Set<AnyCancellable>()
    @State var navigateToLandingPage = false
    
    @State var shortcuts: [shortcutModel] = []
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    @EnvironmentObject var appRootManager: AppRootManager
    @State var showCardSheet = true
    @State var manageAddress = false
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
                                Image("AppLogo")
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
                VStack {
                    Text(VM.profileData?.name?.capitalized ?? "")
                        .font(.title3)
                }
                .padding(.horizontal)
                
                HStack(spacing: 2) {
                    let rating = Double(VM.profileData?.ratings ?? "") ?? 0
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
                
                Button(action: {
                    tabRouter.selectedTab = .ride
                }) {
                    Text("Book Now")
                        .fontWeight(.bold)
                        .frame(height: 50)
                        .frame(width: UIScreen.main.bounds.width - 30)
                        .font(.system(size: 18))
                        .background(Color.accentColor)
                        .foregroundColor(Color("ThemeColor"))
                        .cornerRadius(10.0)
                }
                //            profileStats()
                if VM.currentData != nil {
                    Button(action : {
                        if VM.currentData?.current_status == "completed" {
                            self.appRootManager.currentRoot = .ratingScreen(bookingId: "\(VM.currentData?.id ?? 0)")
                            self.router.popToRoot()
                            return;
                        }
                        self.appRootManager.currentRoot = .currentRideScreen
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
                            tabRouter.selectedTab = .schedule
                        }) {
                            UpcomingBookingView
                                .padding(.horizontal)
                                .padding(.bottom)
                        }
                    }
                    .padding(.top)
                    
                }
                ShortcutView
                Spacer()
                
            }
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
        .onAppear(){
            VM.initialize()
        }
        .onDisappear() {
            VM.stopListener()
        }
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert.toggle()
            })
        )
        
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
        .toolbar(.hidden)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//        .navigationTitle("Profile")
        .modifier(LoadingView(isPresented: $VM.loading))
//        .navigationBarTitleDisplayMode(.inline)
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
                        BadgeView(text: .constant("\(VM.currentData?.fleet?.class_name ?? "")"), bgColor: .constant(Color("Card")), foregroundColor: .constant(.white))
                        
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



struct profileStats: View {
    
    var body: some View {
        VStack {
            Divider()
            HStack (alignment: .center, spacing: 10) {
                NavigationLink(destination: BookingHistoryUIView()){
                    VStack (spacing: 5){
                        Text("12")
                            .font(.system(size: 25))
                            .fontWeight(.bold)
                            .foregroundColor(.black)
                        Text("Total Bookings")
                            .font(.system(size: 14))
                            .foregroundColor(Color.gray)
                    }
                }
                Spacer()
                NavigationLink(destination: ScheduledBookingsView()){
                    VStack (spacing: 5){
                        //                        if ((data?.data?.nextBooking) != nil) {
                        //                            Text(data?.data?.nextBooking ?? "-")
                        //                                .font(.system(size: 25))
                        //                                .fontWeight(.bold)
                        //                                .foregroundColor(.white)
                        //                        }
                        //                        else {
                        //                            Text("No Ride")
                        //                                .font(.system(size: 25))
                        //                                .fontWeight(.bold)
                        //                                .foregroundColor(.white)
                        //                        }
                        Text("Next Booking")
                            .font(.system(size: 14))
                            .foregroundColor(Color.gray)
                    }
                }
            }
            .padding(.vertical)
            
            Divider()
        }
        .padding(.horizontal, 40)
    }
}


extension DashboardUIView {
    var ShortcutView: some View {
        VStack  {
            HStack (alignment: .center){
                Text("Shortcuts")
                    .font(.headline)
                Spacer()
                Button(action: {
                    withAnimation {
                        manageAddress.toggle()
                    }
                }){
                    Text("Manage My Favourite")
                        .font(Font.system(size: 15))
                }
            }
            VStack(alignment: .leading, spacing: 25)  {
                if VM.addrData?.count ?? 0 > 0 {
                    ForEach(Array(((VM.addrData?.enumerated())!)), id: \.offset) { index, element in
                        HStack(spacing: 15){
                            Image("\(element.name ?? "")")
                                .renderingMode(.template)
                                .resizable()
                                .foregroundStyle(Color.accentColor)
                                .scaledToFit()
                                .frame(width: 30, height: 35)
                                .foregroundColor(.accentColor)
                            
                            VStack (alignment: .leading){
                                Text(element.name?.capitalized ?? "")
                                    .font(.body)
                                Text(element.address ?? "")
                                    .lineLimit(1)
                                    .foregroundColor(.gray)
                                    .font(.subheadline)
                                
                            }
                            
                            Spacer()
                            if manageAddress {
                                HStack(alignment: .center){
                                    Button(action: {
                                        router.navigateTo(.addShortcut(addr: element))
                                    }) {
                                        Image("reload")
                                            .padding(.all, 5)
                                    }
                                    Button(action: {
                                        VM.deleteAddr(addrId: element.id)
                                    }){
                                        Image("trash")
                                            .padding(.all, 5)
                                    }
                                }
                            }
                        }
                    }
                }
                Button(action: {
                    router.navigateTo(.addShortcut(addr: nil))
                }) {
                    HStack(spacing: 15){
                        Image("add")
                            .renderingMode(.template)
                            .resizable()
                            .foregroundStyle(Color.accentColor)
                            .scaledToFit()
                            .frame(width: 30, height: 35)
                        
                        VStack (alignment: .leading){
                            Text("Add")
                                .font(.body)
                        }
                    }
                }
            }
            .cardStyleModifier()
        }
        .padding()
        
    }
}

