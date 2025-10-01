//
//  PDFDownload.swift
// SlyykDriverDriver
//
//  Created by Office on 24/11/2022.
//
import SwiftUI
import PDFKit

struct PDFKitView: View {
    var url: URL
    @Binding var sharePdf: Bool
    var body: some View {
        VStack {
            PDFKitRepresentedView(url)
                .background(Color.linearGradient.ignoresSafeArea())
        }
        .navigationBarTitle("Statement Preview")
        .toolbarRole(.editor)
        .background(Color.linearGradient.ignoresSafeArea())
        .toolbar {
            ToolbarItem(placement: .confirmationAction) {
                Button(action: {
                    sharePdf = true
                }) {
                    Image(.downloadIcon)
                        .renderingMode(.template)
                        .resizable()
                        .foregroundStyle(Color.accentColor)
                        .scaledToFit()
                        .frame(width: 24)
                }
            }
        }
    }
}

struct PDFKitRepresentedView: UIViewRepresentable {
    let url: URL
    init(_ url: URL) {
        self.url = url
    }

    func makeUIView(context: UIViewRepresentableContext<PDFKitRepresentedView>) -> PDFKitRepresentedView.UIViewType {
        let pdfView = PDFView()
        pdfView.document = PDFDocument(url: self.url)
        pdfView.autoScales = true
        return pdfView
    }

    func updateUIView(_ uiView: UIView, context: UIViewRepresentableContext<PDFKitRepresentedView>) {
        // Update the view.
    }
}



