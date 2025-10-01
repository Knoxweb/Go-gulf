//
//  InvoicesView.swift
//  Connect Smart Drive
//
//  Created by Prabin Phasikawo on 01/04/2024.
//

import SwiftUI

struct InvoicesView: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject var VM: InvoiceVM = InvoiceVM()
//    @EnvironmentObject var appTheme: AppThemeViewModel
    @State var dismiss = false
    @State var selection: PaymentModel?
    
    @State var invoiceId = 0
    init() {
        UINavigationBar.customizeBackButton()
    }
    @Environment(\.colorScheme) var colorScheme
    
    
    var body: some View {
        VStack (spacing: 0){
            ScrollView {
                VStack (spacing: 30){
                    if !VM.redacting {
                        if VM.invoices?.count ?? 0 > 0 {
                            ForEach(Array((VM.invoices?.enumerated())!), id: \.offset) { index, element in
                                Button(action: {
                                    switch  element.status{
                                    case "need_authorization":
//                                        VM.handlePayment(secrete: element.client_secret, invoiceId: element.id)
                                        break;
                                    case "failed":
                                        self.invoiceId = element.id ?? 0
                                        VM.showCardSheet = true
                                        break;
                                    default:
                                        break;
                                    }
                                }) {
                                    SingleInvoiceCell(VM: VM, element: .constant(element), show: $VM.show, index: index, isSelected: VM.selectedInvoices.contains(element)) {
                                        if VM.selectedInvoices.contains(element) {
                                            VM.selectedInvoices.remove(element)
                                        } else {
                                            VM.selectedInvoices.insert(element)
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            Text("No any invocies")
                                .cardStyleModifier()
                        }
                    }
                    else {
                        ForEach(0..<3) { _ in
                            SingleInvoiceCell(VM: VM, element: .constant(InvoiceModel(amount: "343", client_secret: "testte", generated_at: "24th Oct 2024", id: 0, reference: "BK233", refund_amount: "er", remark: "status", status: "paid", status_title: "Paid", timestamp: 2343243)), show: .constant(true), index: 2, isSelected: true, onTap: {
                            })
                            .redacted(reason: .placeholder)
                            .animatePlaceholder(isLoading: $VM.redacting)
                        }
                    }
                }
                .padding()
                .frame(maxWidth: .infinity)
                .frame(maxHeight: .infinity)
            }
            Spacer()
            VStack {
                HStack {
                    VStack (spacing: 2){
                        Text("From")
                            .foregroundStyle(Color.black)
                            .font(.system(size: 16))
                        DatePicker("", selection: $VM.fromDate, in: ...Date.now, displayedComponents: .date)
                            .labelsHidden()
                            .onChange(of: VM.fromDate) { _ in
                                VM.getInvoices(startDate: VM.fromDate, endDate: VM.toDate)
                            }
                    }
                            
                    Spacer()
                    VStack (spacing: 2){
                        Text("To")
                            .foregroundStyle(Color.black)
                            .font(.system(size: 16))
                        DatePicker("", selection: $VM.toDate, in: ...Date.now, displayedComponents: .date)
                            .labelsHidden()
                            .onChange(of: VM.toDate) { _ in
                                VM.getInvoices(startDate: VM.fromDate, endDate: VM.toDate)
                            }
                    }
                }
                .padding()
                .frame(maxWidth: .infinity)
                .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.gray.opacity(0.5), lineWidth: 1))
            }
            .padding(.horizontal)
            .padding(.bottom)
            if VM.show {
                Divider()
                    .frame(height: 1.08)
                    .overlay(.gray.opacity(0.5))
                Button(action: {
//                    VM.multiplePDFDownload()
                }) {
                    HStack {
                        HStack {
                            Spacer()
                            Text("\(VM.selectedInvoices.count) file selected")
                            Spacer()
                            Image(.downloadIcon)
                                .renderingMode(.template)
                                .foregroundStyle(Color.black)
                    }
                    
                }
                .padding()
                .frame(maxWidth: .infinity)
                .foregroundStyle(Color.black)
                .background(.ultraThinMaterial)
            }
            }
        }
        .sheet(isPresented: $VM.showShareSheet) {
            if let fileURL = VM.fileURL {
                    ShareSheet(activityItems: [fileURL])
            }
            }
        .navigationDestination (isPresented: $VM.openPDF){
            PDFKitView(url: URL(fileURLWithPath: VM.path))
        }
        .sheet(isPresented: $VM.showCardSheet, onDismiss: retryInvoice) {
            ChoosePaymentView(card: $VM.cards, selection: $VM.selection, dismiss: true)
        }
        .toolbarRole(.editor)
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient)
        .navigationTitle("Invoices")
        .navigationBarTitleDisplayMode(.large)
        .modifier(LoadingView(isPresented: $VM.loading))
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert = false
            })
        )
        .onAppear() {
            VM.showCardSheet = false
            VM.getInvoices(startDate: VM.fromDate, endDate: VM.toDate)
            VM.cardObserver()
        }
        .onDisappear() {
            VM.stopListener()
        }
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
//                            .foregroundStyle(Color.white)
//                    }
//                }
//                else {
//                    Button(action: {
//                        withAnimation {
//                            VM.show = true
////                            HapticManager.generateFeedback(for: .medium)
//                        }
//                    }) {
//                        Image(.downloadIcon)
//                            .renderingMode(.template)
//                            .foregroundStyle(Color.white)
//                    }
//                }
//            }
//        }
    }
    
    func retryInvoice() {
        if VM.selection != nil {
            VM.retryInvoice(invoiceId: self.invoiceId, cardId: VM.selection?.id)
        }
    }
}
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
//
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
//                        BadgeView(text: .constant("#\(element?.reference ?? "")"), bgColor: .constant(Color("FormField")), foregroundColor: .constant(Color.white))
//                        BadgeView(text: .constant(element?.status_title ?? ""), bgColor: .constant(element?.status == "paid" ? Color.accentColor : Color.red), foregroundColor: .constant(Color("ThemeColor")))
//                        
//                    }
//                    Text("\(element?.generated_at ?? "")")
//                        .font(.system(size: 14))
//                        .foregroundStyle(Color.white)
//                    Text("$\(element?.amount ?? "")")
//                        .font(.system(size: 14))
//                }
//                Spacer()
//                if show {
//                    Button(action: onTap) {
//                        if isSelected {
//                            Image(systemName: "checkmark.circle.fill")
//                                .foregroundStyle(Color.accentColor)
//                                .font(.system(size: 24))
//                        }
//                        else {
//                            Image(systemName: "circle")
//                                .font(.system(size: 24))
//                                .foregroundStyle(Color.white)
//                        }
//                    }
//                }
//                else {
//                    Button(action: {
//                        VM.downloadPDF(element?.id, reference: element?.reference)
//                    }) {
//                        Image(systemName: "square.and.arrow.up")
//                            .frame(width: 50, height: 50)
//                            .foregroundStyle(Color.white)
//                            .overlay(RoundedRectangle(cornerRadius: 50).stroke(Color("ThemeColor"), lineWidth: 1))
//                    }
//                }
//            }
//        }
//    }
//}


struct SingleInvoiceCell: View {
    @ObservedObject var VM: InvoiceVM
    @Binding var element: InvoiceModel?
    @Binding var show: Bool
    var index: Int
    var isSelected: Bool
    var onTap: () -> Void

    var body: some View {
        VStack {
//            HStack {
//                VStack (alignment: .leading){
//                    HStack {
//                        BadgeView(text: "#\(element?.reference ?? "")", bgColor: Color.BadgeBgColor)
////                        BindingBadgeView(text: .constant(element?.status), bgColor: .constant(element?.status == "paid" ? "green" : "red"), foregroundColor: .constant(Color.black))
//                    }
//                    Text("\(element?.generatedAt ?? "")")
////                        .font(Lato.regular.font(size: 14))
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
