//
//  NotificationsView.swift
//  CTP Limo
//
//  Created by Office on 09/02/2023.
//

import SwiftUI
import Combine

struct NotificationsView: View {
    @StateObject var VM: NotificationVM = NotificationVM()
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    @State var cancellables = Set<AnyCancellable>()
    var isFcm: Bool?
    var type: String?
    var bookingId: String?
    @State var scrollNav = false
    
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
                        Text("No any notification")
                            .cardStyleModifier()
                    }
                }
                .padding()
                .frame(maxWidth: .infinity)
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
        .toolbar(.hidden)
//        .navigationBarTitle("Notifications", displayMode: .inline)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .onAppear(){
            VM.ObserveNotification()
        }
        .onDisappear(){
            VM.stopListener()
        }
    }
}

struct  singleNotificationCard: View {
    var el: NotificationListModel
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    @ObservedObject var VM: NotificationVM
    var body: some View {
        Button(action: {
            if !(el.is_seen ?? false) {
                VM.markAsRead(for: el.id)
            }
            if el.type == "INVOICE" {
                self.router.navigateTo(.invoicesScreen)
            }
            else if el.type == "BOOKING" {
                if el.target != "" {
                    self.router.navigateTo(.bookingDetail(bookingId: "\(el.target ?? "")", type: "history"))
                }
            }
            else if el.type == "SCHEDULED" {
                if el.target != "" {
                    self.router.navigateTo(.bookingDetail(bookingId: "\(el.target ?? "")", type: "schedule"))
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
        NotificationsView()
    }
}
