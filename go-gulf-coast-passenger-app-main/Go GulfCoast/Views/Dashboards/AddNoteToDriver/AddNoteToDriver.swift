//
//  AddNoteToDriver.swift
//  GoGulf
//
//  Created by Mac on 12/29/21.
//

import SwiftUI

struct AddNoteToDriver: View {
    @Environment(\.presentationMode) var presentationMode
    @Binding var note: String
//    var addNoteSheet: Bool
    var body: some View {
        NavigationView {
            Form{
                Section() {
                    TextField("I will be waiting at...,", text: self.$note)
                }
                .listRowBackground(Color("Card"))
            }
            .clearListBackground()
            .navigationBarTitle("Add Notes", displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .interactiveDismissDisabled(true)
            .navigationBarItems(trailing: Button(action: {
                self.presentationMode.wrappedValue.dismiss()
            }) {
                Text("Done").bold()
            })
            .environment(\.sizeCategory, .medium)
            .navigationBarItems(leading: Button(action: {
                self.presentationMode.wrappedValue.dismiss()
            }) {
                Text("Cancel").bold().foregroundColor(.gray)
            })
        }
        
    }
}

struct AddNoteToDriver_Previews: PreviewProvider {
    static var previews: some View {
        AddNoteToDriver(note: .constant("note"))
    }
}
