//
//  AddNoteToDriver.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/29/21.
//

import SwiftUI

struct AddNoteToDriver: View {
    @State var note = ""
    @Binding var addNoteSheet: Bool
    var body: some View {
        NavigationView {
            Form{
                Section() {
                    TextField("I will be waiting at...,", text: $note)
                }
                .listRowBackground(Color("Card"))
            }
            .navigationBarTitle("Add Notes To Driver", displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationBarItems(trailing: Button(action: {
                self.addNoteSheet = false
            }) {
                Text("Done").bold()
            })
            
            .navigationBarItems(leading: Button(action: {
                self.addNoteSheet = false
            }) {
                Text("Cancel").bold().foregroundColor(.gray)
            })
        }
        
    }
}

struct AddNoteToDriver_Previews: PreviewProvider {
    static var previews: some View {
        AddNoteToDriver(addNoteSheet: .constant(true))
    }
}
