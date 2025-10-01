//
//  PreferDriver.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/30/21.
//

import SwiftUI

struct PreferDriver: View {
    var body: some View {
            List{
                HStack(alignment: .center){
                    Image("logo1")
                        .resizable()
                        .frame(width: 50, height: 50)
                        .clipShape(Circle())
                        .overlay(Circle().stroke(Color.black))
                        .shadow(radius: 4)
                    VStack(alignment: .leading){
                        Text("John Doe")
                            .font(.subheadline)
                            .fontWeight(.medium)
                        HStack{
                            ForEach((1...4), id: \.self) { star in
                                Image(systemName: "star.fill")
                                    .font(.caption)
                                    .foregroundColor(Color.accentColor)
                            }
                        }
                    }
                    Spacer()
                    HStack {
                        Button(action: {}) {
                            Image(systemName: "phone.fill")
                                .font(.title3)
                                .foregroundColor(Color.accentColor)
                                .padding()
                        }
                    }
                }
                .listRowBackground(Color("Card"))
                
            }
            .navigationBarTitle("Prefer Driver", displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
    }
}

struct PreferDriver_Previews: PreviewProvider {
    static var previews: some View {
        PreferDriver()
    }
}
