//
//  NotificationsView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/28/21.
//

import SwiftUI

struct NotificationsView: View {
    @StateObject var VM: NotificationVM = NotificationVM()
    @State var responseData: CurrentRideResponse?
    @State var navigateToCurrent = false
    @State var navigateToDetail = false
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    @State var scrollNav = false
    
    var isFcm: Bool?
    var type: String?
    var bookingId: String?
    var fcm: String?
    var body: some View {
        VStack (spacing: 0){
            VStack {
                Text("Notifications")
                    .fontWeight(.bold)
                    .foregroundStyle(Color.black)
            }
            .frame(maxWidth: .infinity)
            .padding(.bottom)
            .padding(.horizontal)
            .background(.ultraThinMaterial.opacity(scrollNav ? 1: 0))
            
            
            ScrollView {
                VStack (alignment: .leading, spacing: 20){
                    if VM.notificationList.count > 0 {
                        ForEach(Array((((VM.notificationList.enumerated())))), id: \.offset) { i, el in
                            singleNotificationCard(el: el, VM: VM)
                        }
                    }
                    else {
                        Text("No any notifications")
                            .cardStyleModifier()
                    }
                }
                .padding()
                .frame(maxWidth: .infinity)
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
        }
//        .navigationBarTitle("Notifications", displayMode: .inline)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .modifier(LoadingView(isPresented: $VM.loading))
        .toolbar(.hidden)
        .navigationDestination (isPresented: self.$navigateToCurrent) {
            CurrentRideView(bookingId: bookingId ?? "")
        }
        .background(Color("Card"))
        .onAppear() {
            VM.ObserveNotification()
        }
    }
}

struct  singleNotificationCard: View {
    var el: NotificationListModel
    @ObservedObject var VM: NotificationVM
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    var body: some View {
        Button(action: {
            if !(el.is_seen ?? false) {
                VM.markAsRead(for: el.id)
            }
            if el.type == "EARNING" {
                self.router.navigateTo(.statementScreen)
            }
            else if el.type == "BOOKING" {
                if el.target != "" {
                    self.router.navigateTo(.bookingDetail(bookingId: el.target ?? "", type: "history"))
                }
            }
            else if el.type == "SCHEDULED" {
                if el.target != "" {
                    self.router.navigateTo(.bookingDetail(bookingId: el.target ?? "", type: "pickups"))
                }
            }
        }) {
            Section {
                
                VStack(alignment: .leading, spacing: 4){
                    HStack{
                        Text("\(el.title?.capitalized ?? "")")
                            .multilineTextAlignment(.leading)
                        Spacer()
                        ZStack{
                            Text(convertTimestampToDate(Double(el.timestamp ?? 0)))
                                .font(.system(size: 12))
                                .opacity(0.7)
                            if !(el.is_seen ?? false) {
                                HStack {
                                    
                                    Circle()
                                        .fill(.red)
                                        .frame(width: 8, height: 8)
                                        .offset(x: 35, y: -18)
                                }
                            }
                        }
                    }
                    Text(el.message ?? "")
                        .font(.system(size: 14))
                        .opacity(0.7)
                        .multilineTextAlignment(.leading)
                }
                .foregroundColor(.black)
            }
            .cardStyleModifier()
        }
    }
}

struct NotificationsView_Previews: PreviewProvider {
    static var previews: some View {
        //        NavigationView {
        NotificationsView()
        //        }
    }
}
