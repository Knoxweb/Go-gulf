//
//  BookingHistoryUIView.swift
//  SwiftProject
//
//  Created by Mac on 12/15/21.
//

import SwiftUI

struct BookingHistoryUIView: View {
    @StateObject var VM = BookingsListVM()
    
    var body: some View {
        
            ScrollView {
                VStack (spacing: 25){
                    if let list =  VM.bookingData {
                        ForEach(Array(list.enumerated()), id: \.offset) { index, element in
                            NavigationLink(destination: BookingDetailView(bookingId: "\(element.id ?? 0)", type: "history")) {
                                BookingCardView(element: .constant(element))
                            }
                        }
                    }
                    else {
                        Text("No any jobs")
                            .cardStyleModifier()
                    }
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .padding(.horizontal)
                .padding(.top)
            }
            .toolbarRole(.editor)
            .navigationBarTitle("Jobs History", displayMode: .inline)
            .modifier(LoadingView(isPresented: $VM.loading))
            .onAppear(){
                UINavigationBar.customizeBackButton()
                VM.initialize()
            }
            .onDisappear() {
                VM.stopListener()
            }
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            
    }
}


struct BookingCardView: View{
    @Binding var element: CurrentBookingModel
    var body: some View{
        VStack (alignment: .leading){
            HStack{
                HStack {
                    Text("BKID")
                        .opacity(0.8)
                    Text(element.reference ?? "")
                        .foregroundStyle(Color.accentColor)
                }
                Spacer()
                BadgeView(text: .constant(element.current_status?.capitalized ?? ""), bgColor: .constant(element.current_status == "completed" ? Color.accentColor : Color.red), foregroundColor: .constant(Color("ThemeColor")))
            }
            Divider()
            VStack(alignment: .leading, spacing: 10){
                HStack{
                    Image(systemName: "circle.fill")
                        .font(.system(size: 10))
                        .foregroundColor(.green)
                    Text(element.pickup?.name ?? "")
                        .font(.system(size: 16))
//                        .opacity(0.8)
                        .multilineTextAlignment(.leading)
                        .fixedSize(horizontal: false, vertical: true)
                    Spacer()
                }
                
                HStack{
                    Image(systemName: "circle.fill")
                        .font(.system(size: 10))
                        .foregroundColor(.red)
                    Text(element.drop?.name ?? "")
                        .font(.system(size: 16))
//                        .opacity(0.8)
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
                    Text(element.pickup_date_time ?? "")
                        .lineLimit(1)
                        .foregroundStyle(Color.accentColor)
                }
                Spacer()
                VStack(alignment: .trailing) {
                    Text("$\(element.fare ?? "")")
                        .fontWeight(.bold)
                        .lineLimit(1)
                        .font(.title2)
                        .foregroundColor(Color("AccentColor"))
                    Text("Inclusive GST")
                        .font(.caption2)
                }
            }
        }
        .foregroundColor(.black)
        .cardStyleModifier()
    }
}

struct BookingHistoryUIView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            BookingHistoryUIView()
        }
    }
}
