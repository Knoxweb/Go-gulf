//
//  ServiceAreaView.swift
//  Chauffeurs Network
//
//  Created by Prabin Phasikawo on 1/18/22.
//

import SwiftUI

struct ServiceAreaView: View {
    @State private var selectedState = "Victoria"
    let states = ["Australian Capital Territory", "New South Wales", "Northern Teritory", "Queensland", "South Australia", "Tasmania", "Victoria", "Western Australia"]
    
    @State private var selectedZone = "Western Victoria Region"
    let zones = ["Eastern Metropolitan Region", "Eastern Victoria Region", "Northern Metropolitan Region", "South Eastern Metropolitan Region", "Western Victoria Regionn", "Tasmania", "Victoria", "Western Australia"]
    
    var body: some View {
        Form{
            Section(){
                Picker("State", selection: $selectedState) {
                    ForEach(states, id: \.self) {
                        Text($0)
                    }
                }
            }
            Section{
                Picker("Zones", selection: $selectedZone) {
                    ForEach(zones, id: \.self) {
                        Text($0)
                    }
                }
            }
        }
        .navigationBarTitle("Service Area", displayMode: .inline)
        .toolbar{
            ToolbarItem(placement: .navigationBarTrailing) {
                Button (action : {}) {
                    NavigationLink(destination: InvoicesView()) {
                        HStack {
                            Text("Save").bold()
                        }
                    }
                }
            }
        }
    }
}

struct ServiceAreaView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView{
            ServiceAreaView()
        }
    }
}
