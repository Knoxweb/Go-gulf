//
//  BookingDetailView.swift
//  SwiftProject
//
//  Created by Mac on 12/15/21.
//

import SwiftUI
import Kingfisher

struct BookingDetailView: View {
    @StateObject var VM = BookingsDetailVM()
    var bookingId: String?
    var type: String?
    var body: some View {
        
        
        ScrollView {
            if VM.bookingData != nil {
                VStack {
                    if VM.bookingData?.driver != nil {
                        DriverUserView(driverData: VM.bookingData?.driver)
                    }
                    
                    Divider()
                    
                    HStack(alignment: .center){
                        HStack (alignment: .bottom) {
                            VStack (alignment: .leading){
                                BadgeView(text: .constant(VM.bookingData?.fleet?.type_name), bgColor: .constant(Color.accentColor), foregroundColor: .constant(Color("ThemeColor")))
                                Text("\(VM.bookingData?.fleet?.name ?? "")")
                                    .padding(.top, 0.0)
                                //                                    if data?.fleet.vehicleRegistrationNumber != "" {
                                //                                        Text("Vec No. \(data?.fleet.vehicleRegistrationNumber ?? "")")
                                //                                            .font(.system(size: 15))
                                //                                    }
                            }
                        }
                        Spacer()
                        KFImage(URL(string: VM.bookingData?.fleet?.image_url ?? ""))
                            .placeholder {
                                Image("Camera")
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 150)
                                    .offset(x: 0, y: 0)
                                    .opacity(0)
                            }
                            .resizable()
                            .scaledToFit()
                            .frame(width: 150)
                            .offset(x: 0, y: 0)
                    }
                    
                    Divider()
                    
                    HStack {
                        HStack {
                            Text("Distance")
                            Text(VM.bookingData?.distance ?? "")
                                .foregroundColor(.accentColor)
                        }
                        Spacer()
                        HStack {
                            Text("Duration")
                            Text(VM.bookingData?.duration ?? "")
                                .foregroundColor(.accentColor)
                        }
                    }
                    //
                    VStack {
                        VStack {
                            HStack {
                                HStack {
                                    Text("Booking Id")
                                    Text(VM.bookingData?.reference ?? "")
                                        .foregroundColor(.accentColor)
                                }
                                Spacer()
                                HStack {
                                    Text(VM.bookingData?.type == "on_demand" ? "Ondemand" : "Scheduled")
                                        .foregroundColor(.accentColor)
                                }
                            }
                            
                            Divider()
                            
                            HStack {
                                HStack {
                                    Text("Pickup Date/Time")
                                }
                                Spacer()
                                HStack {
                                    Text(VM.bookingData?.pickup_date_time ?? "")
                                        .lineLimit(1)
                                        .foregroundColor(.accentColor)
                                }
                            }
                        }
                        .padding()
                        .cornerRadius(8.0)
                        .background(Color("Card").cornerRadius(8.0))
                        
                        //                            invoiceDetail(VM: viewModel)
                        
                        VStack{
                            HStack{
                                Text("Start")
                                Spacer()
                                Text(VM.bookingData?.pickup?.name ?? "")
                                    .foregroundColor(.accentColor)
                            }
                            Divider()
                            HStack{
                                Text("Destination")
                                Spacer()
                                Text(VM.bookingData?.drop?.name ?? "")
                                    .lineLimit(1)
                                    .foregroundColor(.accentColor)
                            }
                        }
                        .padding()
                        .cornerRadius(8.0)
                        .background(Color("Card").cornerRadius(8.0))
                        //
                        if let flight =  VM.bookingData?.flight_number{
                            flightDetail(flightNumber: flight)
                        }
                        Spacer()
                        VStack (alignment: .leading){
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
                                        Text("\(VM.bookingData?.passenger_count ?? 0)")
                                            .foregroundColor(.accentColor)
                                    }
                                    .padding(.horizontal, 4)
                                    Text("")
                                    HStack {
                                        Image("PetsWhite")
                                            .renderingMode(.template)
                                            .resizable()
                                            .foregroundStyle(Color.black)
                                            .scaledToFit()
                                            .frame(width: 15, height: 15)
                                        Text("\(VM.bookingData?.pet_count ?? 0)")
                                            .foregroundColor(.accentColor)
                                    }
                                    .padding(.horizontal, 4)
                                    Text("")
                                    HStack {
                                        Image("WheelchairWhite")
                                            .renderingMode(.template)
                                            .resizable()
                                            .foregroundStyle(Color.black)
                                            .scaledToFit()
                                            .frame(width: 15, height: 15)
                                        Text("\(VM.bookingData?.wheelchair_count ?? 0)")
                                            .foregroundColor(.accentColor)
                                    }
                                    .padding(.horizontal, 4)
                                }
                            }
                            .padding()
                            .cornerRadius(8.0)
                            .background(Color("Card").cornerRadius(8.0))
                            
                            if let note = VM.bookingData?.description {
                                HStack{
                                    VStack(alignment: .leading){
                                        Text("Passenger Note")
                                        Text(note)
                                            .foregroundColor(.accentColor)
                                    }
                                    Spacer()
                                }
                                .padding()
                                .frame(maxWidth: .infinity)
                                .cornerRadius(8.0)
                                .background(Color("Card").cornerRadius(8.0))
                            }
                            
                            chargeDetail(fare: VM.bookingData?.fare)
                        }
                    }
                    
                    if let card = VM.bookingData?.card_masked {
                        HStack {
                            Image(systemName: "creditcard")
                                .font(.title)
                                .foregroundColor(.accentColor)
                            Text(card)
                                .font(.headline)
                                .foregroundColor(.black)
                                .opacity(0.75)
                            Spacer()
                        }
                        .padding(.vertical, 10.0)
                        .padding(.horizontal)
                        .background(Color("Card").cornerRadius(8.0))
                        .frame(maxWidth: .infinity)
                        .buttonStyle(.bordered)
                    }
                }
                .padding()
            }
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .toolbarRole(.editor)
        .modifier(LoadingView(isPresented: $VM.loading))
        .onAppear(perform: {
            UINavigationBar.customizeBackButton()
            print(bookingId as Any, "boookii")
            VM.getBookingDetail(bookingId: "\(bookingId ?? "")", type: type)
        })
        .onDisappear() {
            VM.stopListener()
        }
      
        .navigationBarTitle("Detail", displayMode: .inline)
        .padding(.bottom, 0)
    }
}


struct DriverUserView: View{
    var driverData: DriverModel?
    var body: some View{
        HStack{
            KFImage(URL(string: driverData?.profile_picture_url ?? ""))
                .placeholder {
                    Image("default")
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 80, height: 80)
                        .clipShape(Circle())
                        .shadow(radius: 4)
                        .padding(.trailing)
                        .padding(.bottom)
                }
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(width: 80, height: 80)
                .clipShape(Circle())
                .shadow(radius: 4)
                .padding(.trailing)
                .padding(.bottom)
            
            VStack(alignment: .leading){
                Text(driverData?.name ?? "")
                    .font(.title3)
                    .padding(.bottom, 4.0)
                HStack {
                    if let ratingString = driverData?.rating, let rating = Int(ratingString), rating > 0 {
                        ForEach(1...rating, id: \.self) { star in
                            Image(systemName: "star.fill")
                                .font(.caption)
                                .foregroundColor(Color.accentColor)
                        }
                    }
                }
            }
            Spacer()
        }
    }
}

//struct invoiceDetail: View{
//    var VM: BookingsDetailVM
//    var body: some View{
//        let data = VM.response?.data
//        if(data?.invoice.invoiceID != "") {
//            VStack {
//                HStack {
//                    HStack {
//                        Text("Inv. Id")
//                        Spacer()
//                        Text(data?.invoice.invoiceID ?? "")
//                            .foregroundColor(.accentColor)
//                    }
//                }
//                Divider()
//                HStack {
//                    HStack {
//                        Text("Date/Time")
//                    }
//                    Spacer()
//                    HStack {
//                        Text(data?.invoice.createdAt ?? "")
//                            .lineLimit(1)
//                            .foregroundColor(.accentColor)
//                    }
//                }
//            }
//            .padding()
//            .cornerRadius(8.0)
//            .background(Color("Card").cornerRadius(8.0))
//        }
//    }
//}

struct flightDetail: View{
    @State var flightNumber: String?
    var body: some View{
        VStack {
            HStack(){
                Text("Flight Info")
                Spacer()
                Text(flightNumber ?? "")
            }
        }
        .padding()
        .cornerRadius(8.0)
        .background(Color("Card").cornerRadius(8.0))
    }
}



struct chargeDetail: View{
    @State var fare: String?
    var body: some View{
        VStack(alignment: .leading, spacing: 10){
            VStack{
                HStack{
                    Text("Total Fare")
                    Spacer()
                    VStack (alignment: .trailing){
                        Text("$\(fare ?? "")")
                            .foregroundColor(.accentColor)
                        Text("Inclusive GST")
                            .font(.caption2)
                    }
                }
            }
        }
        .padding()
        .cornerRadius(8.0)
        .background(Color("Card").cornerRadius(8.0))
    }
}

struct BookingDetailView_Previews: PreviewProvider {
    static var previews: some View {
        BookingDetailView()
    }
}
