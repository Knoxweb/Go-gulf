//
//  SupportFormView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/27/21.
//

import SwiftUI

struct SupportFormView: View {
    @StateObject var viewModel = SupportFormVM()
    @Environment(\.presentationMode) var presentationMode
    let strengths = ["Lost and Found", "Complaints", "Feedback", "Account and Settings"]
    var body: some View {
            VStack{
                Form{
                    Section{
                        Picker("Choose Topic", selection: $viewModel.selection) {
                            ForEach(strengths, id: \.self) {
                                Text($0)
                            }
                        }
                    }
                    .listRowBackground(Color("Card"))
                    
                    Section(header: Text("Booking Id")){
                        TextField("", text: $viewModel.bookingId)
                    }
                    .listRowBackground(Color("Card"))
                    
                    
                    Section(header: Text("Message")){
                        TextField("", text: $viewModel.message)
                    }
                    .listRowBackground(Color("Card"))
                }
                .clearListBackground()
            }
            .modifier(
                AlertView(isPresented: $viewModel.showSuccess, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, primaryAction: {
                    viewModel.bookingId = ""
                    viewModel.message = ""
                    self.presentationMode.wrappedValue.dismiss()
                })
            )
            .toolbarRole(.editor)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationBarTitle("Support", displayMode: .inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button(action: {
                        viewModel.submitForm()
                    }) {
                        Text("Done")
                    }
                }
            }
            .modifier(LoadingView(isPresented: $viewModel.loading))
            .modifier(
                AlertView(isPresented: $viewModel.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, secondaryAction: {
                    viewModel.showAlert = false
                })
            )
    }
}

struct SupportFormView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            SupportFormView()
        }
    }
}
