//
//  TransactionsView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/28/21.
//

import SwiftUI

struct StatementsView: View {
    @State private var fromDate = Date()
    @State private var toDate = Date()
    @State private var report = "Week"
    var type = ["Week", "Month", "Year", "Custom"]
    @StateObject var VM: StatementVM = StatementVM()
    
    var columns: [GridItem] =
    Array(repeating: .init(.flexible()), count: 4)
    var body: some View {
        ScrollView {
            //            StatementsCardView()
            //                .padding(.bottom, 10)
            
            //            Picker("Generate Report", selection: $report) {
            //                ForEach(type, id: \.self) {
            //                    Text($0)
            //                }
            //            }
            //            .pickerStyle(.segmented)
            
            LazyVStack (spacing: 15) {
                if !VM.redacting {
                    if VM.statements?.count ?? 0 > 0 {
                        ForEach(Array((VM.statements?.enumerated())!), id: \.offset) { index, element in
                            Button(action: {
                                VM.downloadStatement(element.id, reference: element.reference)
                            }) {
                                VStack {
                                    HStack {
                                        VStack (alignment: .leading){
                                            HStack(spacing: 10){
                                                Text("#\(element.reference ?? "")")
                                                    .foregroundStyle(Color.gray)
                                                BadgeView(text: .constant(element.status_title ?? ""), bgColor: .constant(element.status == "paid" ? Color.accentColor : Color.red), foregroundColor: .constant(Color("ThemeColor")))
                                                Spacer()
                                            }
                                            Text("\(element.start_date ?? "") - \(element.end_date ?? "")")
                                                .font(.system(size: 16))
                                                .fontWeight(.semibold)
                                        }
                                        Spacer()
                                        HStack {
                                            Text("$\(element.amount ?? "")")
                                        }
                                        .fontWeight(.semibold)
                                        .foregroundStyle(Color.accentColor)
                                    }
                                }
                                .cardStyleModifier()
                            }
                        }
                    }
                    else {
                        Text("No any invocies")
                            .cardStyleModifier()
                    }
                }
                else {
                    ForEach(0..<2) { _ in
                        VStack {
                            HStack {
                                VStack (alignment: .leading){
                                    HStack(spacing: 10){
                                        Text("Remark")
                                        BadgeView(text: .constant("Title"), bgColor: .constant(Color.accentColor), foregroundColor: .constant(Color("ThemeColor")))
                                        Spacer()
                                    }
                                    Text("Start Date - End Date")
                                        .foregroundStyle(Color.gray)
                                        .font(.system(size: 14))
                                }
                                Spacer()
                                HStack {
                                    Text("$ Amount")
                                }
                                .fontWeight(.semibold)
                                .foregroundStyle(Color.accentColor)
                            }
                        }
                        .cardStyleModifier()
                        .redacted(reason: .placeholder)
                        .animatePlaceholder(isLoading: $VM.redacting)
                    }
                }
            }
            .padding()
            .onAppear() {
                VM.getStatements()
            }
        }
//        .alert(VM.alertTitle ?? "", isPresented: $VM.showAlert) {
//            Button("OK", role: .cancel) { }
//        } message: {
//            Text(VM.alertMessage ?? "")
//        }
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert = false
            })
        )
        .modifier(LoadingView(isPresented: $VM.loading))
        .toolbarRole(.editor)
        .navigationBarTitle("Statements", displayMode: .inline)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationDestination(isPresented: $VM.openPDF) {
            PDFKitView(url: URL(fileURLWithPath: VM.path),  sharePdf: $VM.showShareSheet)
        }
        .sheet(isPresented: $VM.showShareSheet) {
            if let fileURL = VM.fileURL {
                    ShareSheet(activityItems: [fileURL])
            }
        }
    }
}

struct StatementsView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            StatementsView()
        }
    }
}




struct ShareSheet: UIViewControllerRepresentable {
    let activityItems: [Any]

    func makeUIViewController(context: Context) -> UIActivityViewController {
        let activityViewController = UIActivityViewController(activityItems: activityItems, applicationActivities: nil)
        activityViewController.excludedActivityTypes = [.airDrop, .addToReadingList] // Customize as needed
        return activityViewController
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: Context) {
        // No need to update the view controller
    }
}
