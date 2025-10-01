//
//  TransactionsView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/28/21.
//

import SwiftUI

struct EarningsView: View {
    @StateObject var vm: EarningsVM = EarningsVM()
    @State private var report = "Day"
    var type = ["Day", "Week", "Month", "Year"]
    
    var columns: [GridItem] =
    Array(repeating: .init(.flexible()), count: 4)
    
    var body: some View {
        ZStack {
            ScrollView {
                EarningsCardView(totalEarnings: vm.res?.data?.details?.amount ?? 0, startDate: $vm.startDate, endDate: $vm.endDate)
                    .padding(.bottom, 10)
                
                Picker("Generate Report", selection: $report) {
                    ForEach(type, id: \.self) {
                        Text($0)
                    }
                }
                .padding(.horizontal)
                .pickerStyle(.segmented)
                .onChange(of: report, perform: { val in
                    print(val)
                    vm.OnChangePicker(val);
                })
                
                VStack {
                    LazyVGrid(columns: columns, alignment: .leading) {
                        Text("Date")
                        Text("Job Id")
                        Text("Earned")
                        Text("Booking")
                    }
                    .font(.subheadline)
                    .padding(.bottom, 8)
                    .foregroundColor(Color.accentColor)
                    
                    if (vm.res?.data?.earnings?.count ?? 0 > 0) {
                        ForEach(Array((vm.res?.data?.earnings?.enumerated())!), id: \.offset) { i, el in
                            Divider()
                            LazyVGrid(columns: columns, alignment: .leading) {
                                Text(el.createdAt ?? "")
                                Text(el.bookingID ?? "")
                                Text("$\(String(format:"%.2f", el.amount ?? 0))")
                                Text(el.status ?? "")
                            }
                            .font(.footnote)
                        }
                    }
                    else {
                        Text("No data")
                            .cardStyleModifier()
                    }
                }
                .padding()
//                    .navigationDestination(isPresented: $vm.openPDF) {
//                        PDFKitView(url: URL(fileURLWithPath: vm.path))
//                    }
            }
            .onAppear(){
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "dd/MM/YYYY"
                vm.startDate = dateFormatter.string(from: Date())
                vm.endDate = dateFormatter.string(from: Date())
                vm.getEarningsData(start: vm.startDate, end: vm.endDate)
            }
            .navigationBarTitle("Earnings", displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//            .toolbar {
//                ToolbarItem(placement: .navigationBarTrailing) {
//                    Button (action : {
//                        vm.earningDownload(start: vm.startDate, end: vm.endDate)
//                    }) {
//                        HStack {
//                            Image(systemName: "square.and.arrow.down")
//                            Text("PDF").bold()
//                        }
//                    }
//                }
//            }
            if vm.loading {
                ActivityIndicator()
            }
        }
        .disabled(vm.loading)
    }
}



struct EarningsCardView: View {
    var totalEarnings: Double
    @Binding var startDate: String
    @Binding var endDate: String
    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                VStack {
                    Text("$\(String(format:"%.2f", totalEarnings))")
                        .fontWeight(.bold)
                        .font(.title2)
                        .foregroundColor(Color.accentColor)
                    Text("Total Earning".uppercased())
                        .font(.caption)
                        .opacity(0.7)
                }
                .padding(.all, 12.0)
                Spacer()
                Divider()
                Spacer()
                VStack {
                    VStack(alignment: .leading) {
                        Text("From".uppercased())
                            .font(.caption)
                            .opacity(0.7)
                        Text(self.startDate)
                            .fontWeight(.bold)
                            .font(.subheadline)
                    }
                    Spacer()
                    VStack(alignment: .leading) {
                        Text("To".uppercased())
                            .font(.caption)
                            .opacity(0.7)
                        Text(self.endDate)
                            .fontWeight(.bold)
                            .font(.subheadline)
                    }
                }
                .padding(.all, 12.0)
            }
            //            .background(Color("Gradient2"))
        }
        .shadow(radius: 15)
        .frame(maxWidth: .infinity)
        .background(Color("Card"))
        .cornerRadius(6.0)
        .padding()
    }
}

struct Earnings_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            EarningsView()
        }
    }
}
