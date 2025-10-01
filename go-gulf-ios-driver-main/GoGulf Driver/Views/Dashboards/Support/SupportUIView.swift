//
//  SupportUIView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 12/14/21.
//

import SwiftUI

struct SupportList: Codable {
    let id: Int?
    let title: String?
    let icon: String?
}
struct SupportUIView: View {
    @StateObject var viewModel = SupportVM()
    @State var topic = ""
    @State var id = 0
    @State var fields: Array<Any> = []
    @State var navigate = false
    var body: some View {
            List(Array((viewModel.list.enumerated())), id: \.0) { index, element in
                Button(action: {
                    self.topic = element.title ?? ""
                    self.id = element.id ?? 0
                    self.fields = viewModel.fields ?? []
                    self.navigate = true
                }){
//                    NavigationLink(destination: SupportFormView(), isActive: self.$navigate) {
                        HStack {
                            Image(systemName: element.icon ?? "")
                                .frame(width: 40)
                                .imageScale(.large)
                                .foregroundColor(Color.accentColor)
                            Text(element.title ?? "")
                                .foregroundColor(.black)
                        }
//                    }
                }
            }
            .listRowBackground(Color("Card"))
            .onAppear(perform: {
                viewModel.getSupportList()
            })
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationBarTitle("Support", displayMode: .inline)
    }
}

struct SupportUIView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            SupportUIView()
        }
    }
}
