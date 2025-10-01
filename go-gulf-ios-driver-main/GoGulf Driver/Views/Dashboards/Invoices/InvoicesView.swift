//
//  TransactionsView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/28/21.
//

import SwiftUI

struct InvoicesView: View {
    @State private var fromDate = Date()
    @State private var toDate = Date()
    @State private var report = "Day"
    @State private var openPDF = false
    @State private var path = ""
    var type = ["Day", "Week", "Month", "Year"]
    
    var columns: [GridItem] =
    Array(repeating: .init(.flexible()), count: 4)
    var body: some View {
        ScrollView {
            InvoicesCardView()
                .padding(.bottom, 10)
            
            Picker("Generate Report", selection: self.$report) {
                ForEach(type, id: \.self) {
                    Text($0)
                }
            }
            
            .pickerStyle(.segmented)
            .padding(.horizontal)
            
            VStack {
                LazyVGrid(columns: columns, alignment: .leading) {
                    Text("Date")
                    Text("Invoice Id")
                    Text("Amount")
                    Text("Status")
                }
                .font(.subheadline)
                .padding(.bottom, 8)
                .foregroundColor(Color.accentColor)
                Divider()
                LazyVGrid(columns: columns, alignment: .leading) {
                    Text("04 Dec 2022")
                    Text("#367666")
                    Text("$64.08")
                    Text("Paid")
                }
                .font(.footnote)
                Divider()
                LazyVGrid(columns: columns, alignment: .leading) {
                    Text("06 Dec 2022")
                    Text("#33433")
                    Text("$33.08")
                    Text("Unpaid")
                }
                .font(.footnote)
                Divider()
            }
            .padding()
//                .navigationDestination(isPresented: $openPDF) {
//                    PDFKitView(url: URL(fileURLWithPath: self.path))
//                }
        }
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationBarTitle("Invoices", displayMode: .inline)
        .toolbar {
            ToolbarItem(placement: .navigationBarTrailing) {
                Button (action : {
                   
                }) {
                        HStack {
                            Image(systemName: "square.and.arrow.down")
                            Text("PDF").bold()
                        }
                }
            }
        }
    }
    
  
    
    
}


struct InvoicesCardView: View {
    var body: some View {
        VStack(alignment: .leading) {
            VStack {
                Text("$350.18")
                    .fontWeight(.bold)
                    .font(.title2)
                    .foregroundColor(Color.accentColor)
                Text("Total Invoice".uppercased())
                    .font(.caption)
                    .opacity(0.7)
            }
            .padding(.all, 12.0)
            Divider()
            HStack() {
                VStack(alignment: .leading) {
                    Text("Total Paid".uppercased())
                        .font(.caption)
                        .opacity(0.7)
                    Text("$57.55")
                        .fontWeight(.bold)
                        .font(.subheadline)
                }
                Spacer()
                VStack(alignment: .leading) {
                    Text("Unpaid".uppercased())
                        .font(.caption)
                        .opacity(0.7)
                    Text("$115.55")
                        .fontWeight(.bold)
                        .font(.subheadline)
                }
            }
            .padding(.all, 12.0)
            .cornerRadius(6.0)
//            .background(Color("Gradient2"))
        }
        .shadow(radius: 15)
        .frame(maxWidth: .infinity)
        .background(Color("Card"))
        .cornerRadius(6.0)
        .padding()
    }
}

struct InvoicesView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            InvoicesView()
        }
    }
}
