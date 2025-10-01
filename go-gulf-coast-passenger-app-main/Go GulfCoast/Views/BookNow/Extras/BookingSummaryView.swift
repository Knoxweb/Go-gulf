//
//  BookingSummaryView.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//

import SwiftUI


struct BookingSummary: View {
    @State var quoteData: QuoteResponse?
    @State var fleetData: FleetList?
    @State var passenger: Int?
    var body: some View {
        
        Section{
            VStack(alignment: .leading) {
                HStack(alignment: .top, spacing: 15) {
                    HStack {
                        BadgeView(text: .constant("\(fleetData?.className ?? "")"), bgColor: .constant(Color.accentColor), foregroundColor: .constant(Color(.theme)))
                        Text("\(fleetData?.typeName ?? "")")
                            .fontWeight(.medium)
                            .font(.system(size: 18))
                    }
                    Spacer()
                    HStack (spacing: 5){
                        Spacer()
                        Image(systemName: "person.2.fill")
                        Text("\(passenger ?? 1)")
                    }
                    .fontWeight(.bold)
                    .foregroundStyle(Color.accentColor)
                }
                .padding(.top, 10)
                .listRowBackground(Color("Card"))
                .listRowSeparator(.hidden)
                Text("\(quoteData?.quote?.distance ?? "") - \(quoteData?.quote?.duration ?? "")")
                    .font(.system(size: 18))
                    .fontWeight(.medium)
                    .foregroundStyle(Color.black)
                
                List{
                    VStack (alignment: .leading, spacing: 18){
                        VStack (alignment: .leading, spacing: 20){
                            HStack(alignment: .top, spacing: 15) {
                                Image(systemName: "mappin.and.ellipse")
                                    .font(.title)
                                    .foregroundColor(Color.accentColor)
                                    .frame(maxWidth: 30)
                                VStack(alignment: .leading, spacing: 8) {
                                    Text("Pick Up")
                                        .font(.system(size: 16))
                                        .fontWeight(.medium)
                                        .foregroundColor(.accentColor)
                                    Text(quoteData?.quote?.pickupAddress?.name ?? "")
                                        .font(.system(size: 16))
                                }
                            }
                            HStack(alignment: .top, spacing: 15) {
                                Image(systemName: "flag")
                                    .font(.title)
                                    .foregroundColor(Color.accentColor)
                                    .frame(maxWidth: 30)
                                
                                VStack(alignment: .leading, spacing: 8) {
                                    Text("Destination")
                                        .font(.system(size: 16))
                                        .fontWeight(.medium)
                                        .foregroundColor(.accentColor)
                                    Text(quoteData?.quote?.dropAddress?.name ?? "")
                                        .font(.system(size: 16))
                                }
                            }
                        }
                    }
                    .padding(.vertical, 8)
                    
                }
            }
            .padding(.horizontal)
            .padding(.bottom)
        }
        .listRowBackground(Color("Card"))
        .listRowSeparator(.hidden)
    }
}
