//
//  InvoiceView.swift
//  CSD Chauffeur
//
//  Created by Prabin Phasikawo on 02/08/2024.
//
//
//import SwiftUI
//
//struct InvoiceView: View {
//    @Environment(\.presentationMode) var presentationMode
//    @StateObject var VM: InvoiceVM = InvoiceVM()
//    @EnvironmentObject var appTheme: AppThemeViewModel
//    
//    
//    
//    @State var invoiceId = 0
//    init() {
//        UINavigationBar.customizeBackButton()
//    }
//    @Environment(\.colorScheme) var colorScheme
//    
//    
//    var body: some View {
//        VStack (spacing: 0){
//            ScrollView {
//                VStack (spacing: 30){
//                    if !VM.redacting {
//                        if VM.invoices?.count ?? 0 > 0 {
//                            ForEach(Array((VM.invoices?.enumerated())!), id: \.offset) { index, element in
//                                Button(action: {
//                                }) {
//                                    SingleInvoiceCell(VM: VM, element: .constant(element), show: $VM.show, index: index, isSelected: VM.selectedInvoices.contains(element)) {
//                                        if VM.selectedInvoices.contains(element) {
//                                            VM.selectedInvoices.remove(element)
//                                        } else {
//                                            VM.selectedInvoices.insert(element)
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        else {
//                            NotFoundCard(text: Localized.no_invoices)
//                        }
//                    }
//                    else {
//                        ForEach(0..<3) { _ in
//                            SingleInvoiceCell(VM: VM, element: .constant(InvoiceModel(reference: "", amount: "", createdAtTimestamp: 2, id: "", generatedAt: "", status: "", statusTitle: "2")), show: .constant(true), index: 2, isSelected: true, onTap: {
//                            })
//                            .redacted(reason: .placeholder)
//                            .animatePlaceholder(isLoading: $VM.redacting)
//                        }
//                    }
//                }
//                .padding()
//                .frame(maxWidth: .infinity)
//                .frame(maxHeight: .infinity)
//            }
//            Spacer()
//            VStack {
//                HStack {
//                    VStack (spacing: 2){
//                        Text(Localized.from)
//                            .foregroundStyle(Color.Default)
//                            .font(Lato.regular.font(size: 16))
//                        DatePicker("", selection: $VM.fromDate, in: ...Date.now, displayedComponents: .date)
//                            .labelsHidden()
//                            .onChange(of: VM.fromDate) { _ in
//                                VM.getInvoices(startDate: VM.fromDate, endDate: VM.toDate)
//                            }
//                    }
//                            
//                    Spacer()
//                    VStack (spacing: 2){
//                        Text(Localized.to)
//                            .foregroundStyle(Color.Default)
//                            .font(Lato.regular.font(size: 16))
//                        DatePicker("", selection: $VM.toDate, in: ...Date.now, displayedComponents: .date)
//                            .labelsHidden()
//                            .onChange(of: VM.toDate) { _ in
//                                VM.getInvoices(startDate: VM.fromDate, endDate: VM.toDate)
//                            }
//                    }
//                }
//                .padding()
//                .frame(maxWidth: .infinity)
//                .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.InputBorderColor, lineWidth: 1))
//            }
//            .padding(.horizontal)
//            .padding(.bottom)
//            if VM.show {
//                Divider()
//                Button(action: {
//                    VM.multiplePDFDownload()
//                }) {
//                    HStack {
//                        HStack {
//                            Spacer()
//                            Text("\(VM.selectedInvoices.count) file selected")
//                            Spacer()
//                            Image(.uploadIcon)
//                                .renderingMode(.template)
//                                .foregroundStyle(Color.Default)
//                    }
//                    
//                }
//                .padding()
//                .frame(maxWidth: .infinity)
//                .foregroundStyle(Color.Default)
//                .background(.ultraThinMaterial)
//            }
//            }
//        }
//        .sheet(isPresented: $VM.showShareSheet) {
//            if let fileURL = VM.fileURL {
//                    ShareSheet(activityItems: [fileURL])
//            }
//            }
//        .navigationDestination (isPresented: $VM.openPDF){
//            PDFKitView(url: URL(fileURLWithPath: VM.path))
//        }
////        .sheet(isPresented: $VM.showCardSheet, onDismiss: retryInvoice) {
////            NavigationView {
////                ChoosePaymentView(selection: $VM.selection, dismiss: true)
////            }
////        }
//        .toolbarRole(.editor)
//        .navigationTitle(Localized.invoices)
//        .navigationBarTitleDisplayMode(.large)
//        .modifier(LoadingView(isPresented: $VM.loading))
//        .modifier(
//            AlertView(isPresented: $VM.showAlert,  primaryButtonText: .constant(Localized.ok), secondaryButtonText: Localized.ok, title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
//                VM.showAlert.toggle()
//            })
//        )
//        .onAppear() {
//            VM.showCardSheet = false
//            VM.getInvoices(startDate: VM.fromDate, endDate: VM.toDate)
//        }
//        .onDisappear() {
//            VM.stopListener()
//        }
//        .toolbar {
//            ToolbarItem(placement: .confirmationAction) {
//                if VM.show {
//                    Button(action: {
//                        withAnimation {
//                            VM.show.toggle()
//                        }
//                    }) {
//                        Image(systemName: "xmark")
//                            .renderingMode(.template)
//                            .font(.system(size: 24))
//                            .foregroundStyle(Color.Default)
//                    }
//                }
//                else {
//                    Button(action: {
//                        withAnimation {
//                            VM.show = true
//                            HapticManager.generateFeedback(for: .medium)
//                        }
//                    }) {
//                        Image(.downloadIcon)
//                            .renderingMode(.template)
//                            .foregroundStyle(Color.Default)
//                    }
//                }
//            }
//        }
//    }
//    
////    func retryInvoice() {
////        VM.retryInvoice(invoiceId: self.invoiceId, cardId: VM.selection?.id)
////    }
//}
//
//
//struct ShareSheet: UIViewControllerRepresentable {
//    let activityItems: [Any]
//
//    func makeUIViewController(context: Context) -> UIActivityViewController {
//        let activityViewController = UIActivityViewController(activityItems: activityItems, applicationActivities: nil)
//        activityViewController.excludedActivityTypes = [.airDrop, .addToReadingList] // Customize as needed
//        return activityViewController
//    }
//
//    func updateUIViewController(_ uiViewController: UIActivityViewController, context: Context) {
//        // No need to update the view controller
//    }
//}
//struct SingleInvoiceCell: View {
//    @ObservedObject var VM: InvoiceVM
//    @Binding var element: InvoiceModel?
//    @Binding var show: Bool
//    var index: Int
//    var isSelected: Bool
//    var onTap: () -> Void
//    
//    var body: some View {
//        VStack {
//            HStack {
//                VStack (alignment: .leading){
//                    HStack {
//                        BadgeView(text: "#\(element?.reference ?? "")", bgColor: Color.BadgeBgColor)
//                        BindingBadgeView(text: .constant(element?.status), bgColor: .constant(element?.status == "paid" ? "green" : "red"), foregroundColor: .constant(Color.black))
//                    }
//                    Text("\(element?.generatedAt ?? "")")
//                        .font(Lato.regular.font(size: 14))
//                        .foregroundStyle(Color.Default)
//                    Text("\(element?.amount ?? "")")
//                        .caption()
//                }
//                Spacer()
//                if show {
//                    Button(action: onTap) {
//                        if isSelected {
//                            Image(systemName: "checkmark.circle.fill")
//                                .foregroundStyle(Color.Main)
//                                .font(.system(size: 24))
//                        }
//                        else {
//                            Image(systemName: "circle")
//                                .font(.system(size: 24))
//                                .foregroundStyle(Color.Default)
//                        }
//                    }
//                }
//                else {
//                    Button(action: {
//                        VM.downloadPDF(Int(element?.id ?? ""))
//                    }) {
//                        Image(systemName: "square.and.arrow.up")
//                            .frame(width: 50, height: 50)
//                            .foregroundStyle(Color.Default)
//                            .overlay(RoundedRectangle(cornerRadius: 50).stroke(Color.InputBorderColor, lineWidth: 1))
//                    }
//                }
//            }
//        }
//    }
//}
//
//#Preview {
//    NavigationView {
//        InvoicesView()
//    }
//}
