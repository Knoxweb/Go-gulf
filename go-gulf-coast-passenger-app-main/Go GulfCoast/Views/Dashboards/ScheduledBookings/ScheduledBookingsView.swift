//
//  ScheduledBookingsView.swift
//  GoGulf
//
//  Created by Mac on 12/28/21.
//

import SwiftUI
import Firebase
import FirebaseDatabase
import UIKit


struct ScheduledBookingsView: View {
    @StateObject var VM = ScheduleListVM()
    @State var loading = false
    let identity = UserDefaults.standard.string(forKey: "identity")
    @State var scrollNav = false
    
    var body: some View {
        VStack (spacing: 0){
            VStack {
                Text("My Bookings")
                    .fontWeight(.bold)
            }
            .frame(maxWidth: .infinity)
            .padding(.bottom)
            .padding(.horizontal)
            .background(.ultraThinMaterial.opacity(scrollNav ? 1: 0))
            
            ScrollView {
                VStack{
                    if VM.myJobsList.count > 0 {
                        ForEach(Array((VM.myJobsList.enumerated())), id: \.offset) { index, element in
                            NavigationLink(destination: BookingDetailView(bookingId: "\(element.id ?? 0)", type: "scheduled")) {
                                ScheduleCardView(element: element, VM: VM)
                            }
                        }
                    }
                    else {
                        Text("No scheduled bookings")
                            .cardStyleModifier()
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .padding(.horizontal)
                .padding(.top, 20)
            }
        }
        .modifier(
            AlertView(isPresented: $VM.cancellationAlert, primaryButtonText: .constant("Yes"), secondaryButtonText: "No", title: .constant("Cancel booking?"), desc: .constant("Are you sure you want to cancel this booking. *50% of fare will be charged."), primaryAction: {
                VM.cancelBooking(id: VM.bookingId)
            }, secondaryAction: {
                VM.cancellationAlert = false
            })
        )
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
                    VM.showAlert.toggle()
                })
            )
            .modifier(LoadingView(isPresented: $VM.loading))
            .toolbarRole(.editor)
//            .navigationTitle("My Bookings")
//            .navigationBarTitleDisplayMode(.inline)
            .onAppear(){
                VM.dispatchListener()
                UINavigationBar.customizeBackButton()
            }
            .onDisappear(){
                VM.stopListener()
            }
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
    }
}


struct ScheduleCardView: View{
    var element: CurrentBookingModel?
    @ObservedObject var VM: ScheduleListVM
    
    var body: some View{
        
        VStack (spacing: 15){
            ZStack {
                VStack  (alignment: .leading){
                    VStack (alignment: .leading){
                        if element?.current_status == "stand_by" {
                            BadgeView(text: .constant("Ride will start soon"), bgColor: .constant(Color.accentColor), foregroundColor: .constant(Color("ThemeColor")))
                        }
                        
                        VStack(alignment: .leading, spacing: 10){
                            HStack{
                                Image(systemName: "circle.fill")
                                    .font(.system(size: 10))
                                    .foregroundColor(.green)
                                Text(element?.pickup?.name ?? "")
                                    .font(.system(size: 16))
                                    .multilineTextAlignment(.leading)
                                    .fixedSize(horizontal: false, vertical: true)
                                Spacer()
                            }
                            
                            HStack{
                                Image(systemName: "circle.fill")
                                    .font(.system(size: 10))
                                    .foregroundColor(.red)
                                Text(element?.drop?.name ?? "")
                                    .font(.system(size: 16))
                                    .multilineTextAlignment(.leading)
                                    .fixedSize(horizontal: false, vertical: true)
                                Spacer()
                            }
                        }
                        .padding(.vertical, 5)
                        Divider()
                            HStack {
                                HStack {
                                    Image(.calendar)
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 20)
                                    Text(element?.pickup_date_time ?? "")
                                        .lineLimit(1)
                                        .foregroundStyle(Color.accentColor)
                                }
                                Spacer()
                                VStack(alignment: .trailing) {
                                    Text("$\(element?.fare ?? "")")
                                        .fontWeight(.bold)
                                        .lineLimit(1)
                                        .font(.title2)
                                        .foregroundColor(Color("AccentColor"))
                                    Text("Inclusive GST")
                                        .font(.caption2)
                                }
                            }
                       
                            Button(action: {
                                if element?.current_status != "stand_by" {
                                    VM.cancelBooking(id: element?.id)
                                }
                                else {
                                    VM.bookingId = element?.id ?? 0
                                    VM.cancellationAlert = true
                                }
                            }){
                                Text("Cancel")
                                    .fontWeight(.bold)
                                    .frame(maxWidth: .infinity)
                                    .frame(height: 50)
                                    .background(.red)
                                    .cornerRadius(10.0)
                                    .font(.system(size: 18))
                                    .shadow(radius: 12)
                                    .foregroundColor(Color("ThemeColor"))
                            }
                        Text("Note: Free cancellation before 90 min of pickup time")
                            .multilineTextAlignment(.leading)
                            .fixedSize(horizontal: false, vertical: true)
                            .font(.caption)
                            .foregroundStyle(Color.gray)

//                        }
                    }
                    .cardStyleModifier()
                    .overlay(RoundedRectangle(cornerRadius: 12).stroke(element?.current_status == "stand_by" ? Color.accentColor : Color.clear, lineWidth: 2))
                    .contentShape(Rectangle())
                }
            }
            Spacer()
        }
    }
}

struct ScheduledBookingsView_Previews: PreviewProvider {
    static var previews: some View {
        ScheduledBookingsView()
    }
}
