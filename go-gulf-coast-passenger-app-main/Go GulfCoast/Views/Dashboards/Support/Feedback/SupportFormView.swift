//
//  SupportFormView.swift
//  GoGulf
//
//  Created by Mac on 12/27/21.
//

import SwiftUI

struct SupportFormView: View {
    @StateObject var VM = SupportFormVM()
    @Environment(\.presentationMode) var presentationMode
    let supportType = ["Lost and Found", "Complaints", "Feedback", "Account", "Billing"]
    var body: some View {
        VStack{
            Form{
                Section{
                    Picker("Choose Topic", selection: $VM.selection) {
                        ForEach(supportType, id: \.self) {
                            Text($0)
                        }
                        .font(.system(size: 16))
                        .environment(\.sizeCategory, .medium)
                    }
                    
                }
                .listRowBackground(Color("Card"))
                
                Section(header: Text("Booking Id")){
                    TextField("", text: $VM.bookingId)
                }
                .listRowBackground(Color("Card"))
                
                Section(header: Text("Message")){
                        TextField("", text: $VM.message, axis: .vertical)
                }
                .listRowBackground(Color("Card"))
            }
            .clearListBackground()
        }
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert.toggle()
            })
        )
        
        .modifier(
            AlertView(isPresented: $VM.showSuccess, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, primaryAction: {
                VM.bookingId = ""
                VM.message = ""
                self.presentationMode.wrappedValue.dismiss()
            })
        )
        .toolbarRole(.editor)
        .environment(\.sizeCategory, .medium)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationBarTitle("Support", displayMode: .inline)
        .modifier(LoadingView(isPresented: $VM.loading))
        .toolbar {
            ToolbarItem(placement: .confirmationAction) {
                Button(action: {
                    VM.submitForm()
                }) {
                    Text("Done")
                }
            }
        }
    }
}

struct SupportFormView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            SupportFormView()
        }
    }
}
