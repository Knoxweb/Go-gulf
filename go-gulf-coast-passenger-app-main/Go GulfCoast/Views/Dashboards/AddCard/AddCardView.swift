//
//  AddCardView.swift
//  GoGulf
//
//  Created by Mac on 12/29/21.
//

import SwiftUI

struct AddCardView: View {
    @StateObject var VM: PaymentVM = PaymentVM()
    @State var cardNumber = ""
    @State var presentSheet = false
    @State var mobPhoneNumber = ""
    @State private var searchCountry: String = ""
    @Environment(\.presentationMode) var presentationMode
    @FocusState public var keyIsFocused: Bool
    @EnvironmentObject var router: Router
    @State var userCurrentCountry: CPData? = nil
    @State private var inputText = ""
    let maxDigits = 5
    let countries: [CPData] = Bundle.main.decode("CountryNumbers.json")
    @State private var previousValue: String = ""

    
    
    var body: some View {
        VStack {
            ScrollView {
                VStack {
                    VStack (spacing: 15){
                        TextFieldView(field: $VM.cardNumber, text: "Card Number", keyboardType: .numberPad)
                            .onChange(of: VM.cardNumber) { newValue in
                                   if newValue.count > 19 {
                                       VM.cardNumber = String(newValue.prefix(19))
                                   }
                            }
                        HStack{
                            TextFieldView(field: $inputText, text: "Expiry Date", placeholder: "MM/YY", keyboardType: .numberPad)
                                .onChange(of: inputText) { newValue in
                                    if newValue.count > maxDigits {
                                        inputText = String(newValue.prefix(maxDigits))
                                    }
                                    else if newValue.count == 2 && !newValue.contains("/") {
                                        inputText = "\(newValue)/"
                                    }
                                    else if newValue.count < previousValue.count && newValue.last == "/" {
                                        inputText.removeLast(2)
                                    }
                                    previousValue = inputText
                                }
                            TextFieldView(field: $VM.CVC, text: "CVC", placeholder: "123", keyboardType: .numberPad)
                        }
                        
                        TextFieldView(field: $VM.cardHolder, text: "Holder's Name")
                        
                       
                    }
                    Spacer()
                }
                .padding()
            }
            .toolbarRole(.editor)
            Button(action: {
                VM.cardExpiry = inputText
                VM.handlePayment()
            }) {
                Text("Add Card")
                    .fullWithButton()
            }
            .padding()
        }
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert.toggle()
            })
        )
        
        .modifier(
            AlertView(isPresented: $VM.showSuccess, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, primaryAction: {
                self.presentationMode.wrappedValue.dismiss()
            })
        )
        
        .navigationTitle("Add Card")
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient, ignoresSafeAreaEdges: .all)
        .presentationContentInteraction(.resizes)
        .presentationDragIndicator(.visible)
        .presentationCompactAdaptation(.sheet)
        .onReceive(VM.viewDismissalModePublisher) { shouldDismiss in
            if shouldDismiss {
                self.presentationMode.wrappedValue.dismiss()
            }
        }
        .onDisappear() {
            VM.showAlert = false
            presentationMode.wrappedValue.dismiss()
        }
        .modifier(LoadingView(isPresented: $VM.loading))
        
        .presentationDetents([.height(450), .large])
        
    }
}

