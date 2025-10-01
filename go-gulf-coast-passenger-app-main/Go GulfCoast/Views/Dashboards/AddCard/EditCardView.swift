//
//  EditCard.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 28/04/2024.
//

import SwiftUI
import Combine

struct CardUpdateModel: Codable {
    let id: Int?
    let card_holder_name: String?
    let card_expiry: String?
    let is_active: Int?
}

struct EditCardView: View {
    @StateObject var VM: EditCardVM = EditCardVM()
    @State var cardData: CardModel?
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
                            .disabled(true)
                            .onChange(of: VM.cardNumber) { newValue in
                                   if newValue.count > 19 {
                                       VM.cardNumber = String(newValue.prefix(19))
                                   }
                            }
                            .disabled(true)
                        HStack{
                            TextFieldView(field: $inputText, text: "Expiry Date", placeholder: "MM/AA", keyboardType: .numberPad)
                                .disabled(true)
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
                            TextFieldView(field: $VM.CVC, text: "CVC", placeholder: "***", keyboardType: .numberPad)
                                .disabled(true)
                        }
                        
                        TextFieldView(field: $VM.cardHolder, text: "Holder's Name")
                            .disabled(true)
                        
                        Toggle("Set this card to active", isOn: $VM.active)
                            .cardStyleModifier()
                            .disabled(cardData?.is_active ?? true)
                    }
                    Spacer()
                }
                .padding()
            }
            Button(action: {
                VM.updateCard(cardData?.id)
            }) {
                Text("Update Card")
                    .fullWithButton()
            }
            .padding()
            .disabled(!VM.active)
        }
        
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient, ignoresSafeAreaEdges: .all)
        .presentationContentInteraction(.resizes)
        .presentationDragIndicator(.visible)
        .presentationCompactAdaptation(.sheet)
        .navigationTitle("Edit Card")
        .onReceive(VM.viewDismissalModePublisher) { shouldDismiss in
            if shouldDismiss {
                self.presentationMode.wrappedValue.dismiss()
                
            }
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
        .onDisappear() {
            VM.showAlert = false
            presentationMode.wrappedValue.dismiss()
        }
        .onAppear() {
            VM.cardNumber = cardData?.card_masked ?? ""
            VM.expiryMonth = cardData?.exp_month ?? 0
            VM.expiryYear = cardData?.exp_year ?? 0
            VM.cardHolder = cardData?.name ?? ""
            let formattedMonth = String(format: "%02d", VM.expiryMonth)
            let formattedYear = String(format: "%02d", VM.expiryYear % 100)
            inputText = "\(formattedMonth)/\(formattedYear)"
            VM.active = cardData?.is_active ?? true
        }
        .modifier(LoadingView(isPresented: $VM.loading))
        
        .presentationDetents([.height(450), .large])
        
    }
    
}
