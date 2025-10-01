//
//  setDateTimeView.swift
//  GoGulf
//
//  Created by Mac on 12/21/21.
//

import SwiftUI
import CoreLocation

struct setDateTimeView: View {
    @StateObject var viewModel: SetDateTimeVM = SetDateTimeVM()
    @State var timezone = TimeZone(identifier: "Europe/Paris")!
    @State private var isEditing = false
    @EnvironmentObject var router: Router
    @State var scheduled = false
    var pickup: String
    var pLat: Double
    var pLng: Double
    var via: String
    var vLat: Double
    var vLng: Double
    var dropoff: String
    var dLat: Double
    var dLng: Double
    let dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMM yyyy" // Format for the date
        return formatter
    }()
    
    // Create a date formatter for the time
    let timeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm" // 24-hour format for the time
        return formatter
    }()
    
    
    var body: some View {
        let currentDate = Date()
        VStack {
            ScrollView {
                VStack (spacing: 30){
                    Button(action: {
                        withAnimation {
                            self.scheduled = false
                        }
                    }) {
                        VStack {
                            HStack {
                                Text("Now")
                                    .font(.system(size: 16))
                                    .foregroundStyle(Color.black)
                                Spacer()
                                HStack {
                                    Text(dateFormatter.string(from: currentDate))
                                        .padding(8)
                                        .background(Color("Gradient5").opacity(0.3))
                                        .clipShape(RoundedRectangle(cornerRadius: 12))
                                    Text(timeFormatter.string(from: currentDate))
                                        .padding(8)
                                        .background(Color("Gradient5").opacity(0.3))
                                        .clipShape(RoundedRectangle(cornerRadius: 12))
                                }
                            }
                        }
                        .padding(.top, 20)
                        .padding(.horizontal, 25)
                        .padding(.bottom, 20)
                        .foregroundColor(.black)
                        .background(!self.scheduled ? Color.accentColor.opacity(0.1) : Color("FormField"))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.gray.opacity(0.1), lineWidth: 1))
                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(!self.scheduled ? Color.accentColor : Color.clear, lineWidth: 2))
                    }
                    
                    Section {
                        
                        VStack{
                            Toggle("Schedule", isOn: self.$scheduled.animation())
                                .tint(Color.accentColor)
                            
                            if self.scheduled {
//                                Divider()
                                DatePicker("Date", selection: $viewModel.bookingDate, in: Date.now..., displayedComponents: [.date, .hourAndMinute])
                                    .datePickerStyle(GraphicalDatePickerStyle())
                            }
                        }
                        .padding(.top, 20)
                        .padding(.horizontal, 25)
                        .padding(.bottom, 20)
                        .foregroundColor(.black)
                        .background(self.scheduled ? Color.accentColor.opacity(0.1) : Color("FormField"))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(self.scheduled ? Color.accentColor : Color.clear, lineWidth: 2))
                       
                    }
                    HStack {
                        Text("Passenger Capacity")
                        Spacer()
                        Picker("Passenger", selection: $viewModel.passenger) {
                            ForEach(1...25, id: \.self) { number in
                                Text("\(number)")
                            }
                        }
                    }
                    .padding(.top, 20)
                    .padding(.horizontal, 25)
                    .padding(.bottom, 20)
                    .foregroundColor(.black)
                    .background(self.scheduled ? Color.accentColor.opacity(0.1) : Color("FormField"))
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                    .overlay(RoundedRectangle(cornerRadius: 12).stroke(self.scheduled ? Color.accentColor : Color.clear, lineWidth: 2))
                }
                .padding()
            }
            VStack {
                Button(action: {
                    print(viewModel.bookingDate, "bookingdate")
                    if(self.scheduled){
                        viewModel.date = viewModel.bookingDate
                        viewModel.bookingType = "scheduled"
                    }
                    viewModel.submitQuoteRequest(pickup: pickup, pLat: "\(pLat)", pLng: "\(pLng)", dropoff: dropoff, dLat: "\(dLat)", dLng: "\(dLng)")
                }) {
                    Text("Book Now")
                        .fullWithButton()
                }
            }
            .padding()
        }
        .onAppear(){
            UINavigationBar.customizeBackButton()
        }
        .navigationTitle("Booking")
        .toolbarRole(.editor)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .modifier(LoadingView(isPresented: $viewModel.loading))
        .modifier(
            AlertView(isPresented: $viewModel.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, secondaryAction: {
                viewModel.showAlert = false
            })
        )
        .disabled(viewModel.loading)
    }
    
    //    Converting The Coordinates to the TimeZone
    func getTimeZoneFromCoordinates()  {
        let location = CLLocation(latitude: self.pLat, longitude: self.pLng)
        let geoCoder = CLGeocoder()
        geoCoder.reverseGeocodeLocation(location) { (placemarks, err) in
            if let placemark = placemarks?[0] {
                self.timezone = placemark.timeZone!
            }
        }
    }
}

struct setDateTimeView_Previews: PreviewProvider {
    static var previews: some View {
        setDateTimeView(pickup: "String", pLat: 0.0, pLng: 0.0, via: "String", vLat: 0.0, vLng: 0.0, dropoff: "String", dLat: 0.0, dLng: 0.0
        )
    }
}
