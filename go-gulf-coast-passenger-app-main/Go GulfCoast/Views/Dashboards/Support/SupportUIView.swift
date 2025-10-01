//
//  SupportUIView.swift
//  SwiftProject
//
//  Created by Mac on 12/14/21.
//

import SwiftUI

struct SupportList: Codable, Hashable {
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
                        HStack {
                            Image(systemName: element.icon ?? "")
                                .frame(width: 40)
                                .imageScale(.large)
                                .foregroundColor(Color.accentColor)
                            Text(element.title ?? "")
                                .foregroundColor(.black)
                        }
                }
                .listRowBackground(Color("Card"))
                .navigationDestination(isPresented: self.$navigate) {
                    SupportFormView()
                }
            }
            .clearListBackground()
            .onAppear(perform: {
                viewModel.getSupportList()
            })
            .toolbarRole(.editor)
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
