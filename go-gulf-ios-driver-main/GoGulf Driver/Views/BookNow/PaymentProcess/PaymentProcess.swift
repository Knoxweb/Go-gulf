//
//  PaymentProcess.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/28/21.
//

import SwiftUI

struct Farebreakdown: Identifiable {
    let id = UUID()
    let title: String
    let amount: String
}
private var farebreakdowns = [
    Farebreakdown(title: "Tolls", amount: "205.0"),
    Farebreakdown(title: "Levy", amount: "1.1"),
    Farebreakdown(title: "Baby Seats (X2)", amount: "2.0"),
    Farebreakdown(title: "Surcharge", amount: "15.0")
]

struct PaymentProcessView: View {
    @State var showSheetView = false
    
    var body: some View {
        VStack {
            PaymentCardView()
            HStack {
                HStack {
                    Text("Booking Id")
                    Text("#3434233")
                        .foregroundColor(.accentColor)
                }
                Spacer()
                HStack {
                    Text("OnDemand")
                        .foregroundColor(.accentColor)
                }
            }
            .padding()
            .cornerRadius(8.0)
            .background(Color("Card").cornerRadius(8.0))
            
            HStack {
                HStack {
                    Text("Pickup Date/Time")
                }
                Spacer()
                HStack {
                    Text("22 Dec 2023 / 09:45 AM")
                        .foregroundColor(.accentColor)
                }
            }
            .padding()
            .cornerRadius(8.0)
            .background(Color("Card").cornerRadius(8.0))
            
            VStack(alignment: .leading, spacing: 10){
                HStack{
                    Text("Base Fare")
                    Spacer()
                    Text("$456.88")
                        .foregroundColor(.accentColor)
                }
                
                
                
                ForEach(farebreakdowns) { item in
                    HStack {
                        Text("\(item.title)")
                        Spacer()
                        HStack {
                            Text("\(item.amount) $")
                        }
                    }
                    .font(.caption)
                }
                
                HStack{
                    Text("Base Fare")
                    Spacer()
                    Text("$456.88")
                        .foregroundColor(.accentColor)
                }
                
            }
            .padding()
            .cornerRadius(8.0)
            .background(Color("Card").cornerRadius(8.0))
            
            Button(action: { showSheetView.toggle()}){
                HStack {
                    Image(systemName: "creditcard")
                        .font(.title)
                    Text("XXXX XXXX XXXX XX29")
                        .font(.headline)
                        .foregroundColor(.black)
                        .opacity(0.75)
                    Spacer()
                    Image(systemName: "chevron.right")
                }
                .frame(maxWidth: .infinity)
            }
            .sheet(isPresented: $showSheetView) {
                AddCardView(showSheetView: self.$showSheetView)
            }
            .background(Color("Card").cornerRadius(8.0))
            .frame(maxWidth: .infinity)
            .buttonStyle(.bordered)
            Spacer()
            Group {
                VStack{
//                    NavigationLink(destination: RatingsView(bookingId: "bookingId")) {
                        Text("Process Payment")
                            .fontWeight(.bold)
                            .frame(width: 200, height: 55)
                            .background(Color.accentColor)
                            .cornerRadius(10.0)
                            .font(.body)
                            .shadow(radius: 15)
                            .foregroundColor(Color("ThemeColor"))
//                    }
                }
                .padding(.top, 30.0)
            }        }
        .padding(.horizontal)
        .navigationBarBackButtonHidden(true)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationBarTitle("Invoice", displayMode: .inline)
    }
}


struct PaymentCardView: View {
    var body: some View {
        VStack(alignment: .leading) {
            VStack {
                Text("$350.18")
                    .fontWeight(.bold)
                    .font(.title2)
                    .foregroundColor(Color.accentColor)
                Text("Total Fare".uppercased())
                    .font(.caption)
                    .opacity(0.7)
            }
            .padding(.all, 12.0)
            Divider()
            HStack() {
                VStack(alignment: .leading) {
                    Text("Invoice Id".uppercased())
                        .font(.caption)
                        .opacity(0.7)
                    Text("INV343343")
                        .fontWeight(.bold)
                        .font(.subheadline)
                }
                Spacer()
                VStack(alignment: .leading) {
                    Text("Date / Time".uppercased())
                        .font(.caption)
                        .opacity(0.7)
                    Text("22 Dec 2023 / 09:45 AM")
                        .fontWeight(.bold)
                        .font(.subheadline)
                }
            }
            .padding(.all, 12.0)
            .cornerRadius(6.0)
        }
        .shadow(radius: 15)
        .frame(maxWidth: .infinity)
        .background(Color("Card"))
        .cornerRadius(6.0)
    }
}


struct PaymentProcessView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            PaymentProcessView()
        }
    }
}
