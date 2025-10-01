//
//  AddCardView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/29/21.
//

import SwiftUI
import Combine

struct AddCardView: View {
    @Binding var showSheetView: Bool
    @State private var cardExpiry: String = ""
    @State private var cardNumber = ""
    @State private var CVC: String = ""
    @State private var showAlert = false
    let cvcLimit = 4
    let cardLimit = 16
    
    var body: some View {
        NavigationView {
            Form{
                Section{
                    TextField("Card Number", text: $cardNumber)
                        .keyboardType(.numberPad)
                        .onReceive(Just(cardNumber)) { _ in cardNumberLimit(cardLimit) }
                }
                .listRowBackground(Color("Card"))
                Section {
                    HStack {
                        TextField("Expiry Date", text: $cardExpiry)
                            .frame(width: 250)
                        SecureField("CVC", text: $CVC)
                            .keyboardType(.numberPad)
                            .onReceive(Just(CVC)) { _ in limitText(cvcLimit) }
                    }
                }
                .listRowBackground(Color("Card"))
            }
            .environment(\.sizeCategory, .medium)
            .navigationBarTitle(Text("Add Card"), displayMode: .inline)
            .navigationBarItems(trailing: Button(action: {
                self.submitCard()
            }) {
                Text("Done").bold()
            })
            
            .navigationBarItems(leading: Button(action: {
                self.showSheetView = false
            }) {
                Text("Cancel").bold().foregroundColor(.gray)
            })
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .alert(isPresented: $showAlert) {
                Alert(title: Text("Error"),
                      message: Text("Card Number must be 16 digit number")
                )
            }
            
          
        }
       
    }
    //Function to keep text length in limits
       func limitText(_ upper: Int) {
           if CVC.count > upper {
               CVC = String(CVC.prefix(upper))
           }
       }
    func cardNumberLimit(_ upper: Int) {
        if cardNumber.count > upper {
            cardNumber = String(cardNumber.prefix(upper))
        }
    }
    
    func submitCard(){
        if cardNumber.count < 16 {
            showAlert = true
        }
        else {
            showSheetView = false
        }
    }
  
}

struct AddCardView_Previews: PreviewProvider {
    static var previews: some View {
        AddCardView(showSheetView: .constant(true))
    }
}
