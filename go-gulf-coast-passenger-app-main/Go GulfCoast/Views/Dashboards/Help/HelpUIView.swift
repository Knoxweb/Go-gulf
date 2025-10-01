//
//  HelpUIViews.swift
//  SwiftProject
//
//  Created by Mac on 12/14/21.
//

import SwiftUI


struct Help: Codable, Hashable {
    var id: Int?
    var title: String?
    var icon: String?
    var content: HelpContent?
}
struct HelpContent: Codable, Hashable {
    var desc: String
}
//
//struct HelpUIView: View {
//    @StateObject var viewModel = HelpVM()
//    @State var topic: String?
//    @State var id: Int?
//    
//    var body: some View {
//            List(Array((viewModel.helpList?.enumerated())!), id: \.offset) { index, element in
//                Button(action: {
//                    viewModel.getHelpDetail(categoryId: element.id ?? 0)
//                    self.topic = element.title ?? "Help"
//                    self.id = element.id ?? 0
//                }){
//                        HStack {
//                            Image(systemName: element.icon ?? "")
//                                .frame(width: 40)
//                                .imageScale(.large)
//                                .foregroundColor(Color.accentColor)
//                            Text(element.title ?? "")
//                                .foregroundColor(.white)
//                        }
//                }
//                .listRowBackground(Color("Card"))
//                .navigationDestination(isPresented: $viewModel.navigate) {
//                    HelpListView(id: self.id, topic: self.topic, vm: viewModel)
//                }
//            }
//              
//            .navigationBarTitle("Help", displayMode: .inline)
//            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//    }
//}
//
//struct HelpUIView_Previews: PreviewProvider {
//    static var previews: some View {
//        NavigationView {
//            HelpUIView()
//        }
//    }
//}
