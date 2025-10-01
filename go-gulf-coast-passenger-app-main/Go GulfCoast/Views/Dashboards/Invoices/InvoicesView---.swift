//
//  TransactionsView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 12/28/21.
//

import SwiftUI
//
//struct InvoicesViewsx: View {
//    @StateObject var vm: InvoiceVM = InvoiceVM()
//    @State private var report = "Day"
//    var type = ["Day", "Week", "Month", "Year"]
//    var columns: [GridItem] =
//    Array(repeating: .init(.flexible()), count: 4)
//    var body: some View {
//        let amount = vm.res?.data.amounts
//        ZStack {
//        ScrollView {
//            
//            InvoicesCardView(total: amount?.total ?? "", paid: amount?.paid ?? "", unpaid: amount?.unpaid ?? "")
//                .padding(.bottom, 10)
//            
//            Picker("Generate Report", selection: $report) {
//                ForEach(type, id: \.self) {
//                    Text($0)
//                }
//            }
//            .pickerStyle(.segmented)
//            .padding(.horizontal)
//            .onChange(of: report, perform: { val in
//                print(val)
//                vm.OnChangePicker(val);
//                
//            })
//            
//            VStack {
//                LazyVGrid(columns: columns, alignment: .leading) {
//                    Text("Date")
//                    Text("Invoice Id")
//                    Text("Amount")
//                    Text("Status")
//                }
//                .font(.subheadline)
//                .padding(.bottom, 8)
//                .foregroundColor(Color.accentColor)
//                if((vm.res?.data.amounts.invoices) != nil) {
//                    ForEach(Array((vm.res?.data.amounts.invoices.enumerated())!), id: \.offset) { i, el in
//                        Divider()
//                        LazyVGrid(columns: columns, alignment: .leading) {
//                            Text(el.createAt ?? "")
//                            Text(el.invoiceID ?? "")
//                            Text(el.amount ?? "")
//                            HStack{
//                                Text(el.status ?? "")
//                                Spacer()
//                                Button(action: {
//                                    vm.invoiceListDownload(invoiceId: el.invoiceID ?? "")
//                                }){
//                                    Image(systemName: "square.and.arrow.down")
//                                        .font(.title2)
//                                        .foregroundColor(Color("AccentColor"))
//                                }
//                                
//                            }
//                           
//                        }
//                        .font(.footnote)
//                    }
//                }
//            }
//            .padding()
//            .navigationDestination(isPresented: $vm.openPDF) {
//                PDFKitView(url: URL(fileURLWithPath: vm.path))
//            }
//        }
//        .onAppear(){
//            let dateFormatter = DateFormatter()
//            dateFormatter.dateFormat = "dd/MM/YYYY"
//            let startDate = dateFormatter.string(from: Date())
//            let endDate = dateFormatter.string(from: Date())
//            vm.getInvoiceData(start: startDate, end: endDate)
//        }
//        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//        .navigationBarTitle("Invoices", displayMode: .inline)
//        .toolbar {
//            ToolbarItem(placement: .navigationBarTrailing) {
//                Button (action : {
//                    vm.invoiceDownload(start: vm.startDate, end: vm.endDate)
//                }) {
//                    HStack {
//                        Image(systemName: "square.and.arrow.down")
//                        Text("PDF").bold()
//                    }
//                }
//            }
//        }
//            if vm.loading {
//                ActivityIndicator()
//            }
//        }
//        .disabled(vm.loading)
//    }
//}
//
//struct InvoicesCardView: View {
//    var total: String
//    var paid: String
//    var unpaid: String
//    var body: some View {
//        VStack(alignment: .leading) {
//            VStack {
//                Text("\(total )")
//                    .fontWeight(.bold)
//                    .font(.title2)
//                    .foregroundColor(Color.accentColor)
//                Text("Total Invoice".uppercased())
//                    .font(.caption)
//                    .opacity(0.7)
//            }
//            .padding(.all, 12.0)
//            Divider()
//            HStack() {
//                VStack(alignment: .leading) {
//                    Text("Total Paid".uppercased())
//                        .font(.caption)
//                        .opacity(0.7)
//                    Text("\(paid )")
//                        .fontWeight(.bold)
//                        .font(.subheadline)
//                }
//                Spacer()
//                VStack(alignment: .leading) {
//                    Text("Unpaid".uppercased())
//                        .font(.caption)
//                        .opacity(0.7)
//                    Text("\(unpaid )")
//                        .fontWeight(.bold)
//                        .font(.subheadline)
//                }
//            }
//            .padding(.all, 12.0)
//            .cornerRadius(6.0)
//            //            .background(Color("Card"))
//        }
//        .shadow(radius: 15)
//        .frame(maxWidth: .infinity)
//        .background(Color("Card"))
//        .cornerRadius(6.0)
//        .padding()
//    }
//}
//
//struct InvoicesView_Previews: PreviewProvider {
//    static var previews: some View {
//        NavigationView {
//            InvoicesView()
//        }
//    }
//}
