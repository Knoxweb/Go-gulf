//
//  HelpListView.swift
// SlyykDriver
//
//  Created by Office on 13/07/2022.
//
//
//import SwiftUI
//
//struct HelpListView: View {
//    var id: Int?
//    var topic: String?
//    var vm: HelpVM?
//    var body: some View {
//            List(Array((vm?.helpListDetail?.enumerated())!), id: \.offset) { index, element in
//                NavigationLink(destination: HelpListViewDetail(topic: topic, title: element.title, content: vm?.content ?? [])){
//                    HStack {
//                        Text(element.title ?? "")
//                            .foregroundColor(.white)
//                    }
//                }
//                .listRowBackground(Color("Card"))
//            }
//            .navigationBarTitle(topic ?? "Help", displayMode: .inline)
//            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//    }
//}
//
//struct HelpListViewDetail: View {
//    var topic: String?
//    var title: String?
//    var content: Array<Any>
//    var body: some View {
//        if #available(iOS 16.0, *) {
//            ScrollView{
//                HStack {
//                    VStack (alignment: .leading, spacing: 8) {
//                        Text(title ?? "")
//                            .font(.title)
//                        ForEach(Array((content.enumerated())), id: \.offset) { index, element in
//                            Text(element as? String ?? "" )
//                        }
//                    }
//                    Spacer()
//                }
//            }
//            .padding()
//            .frame(maxWidth: .infinity, maxHeight: .infinity)
//            .navigationBarTitle(topic ?? "Help", displayMode: .inline)
//            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//            .scrollContentBackground(.hidden)
//        } else {
//            ScrollView{
//                HStack {
//                    VStack (alignment: .leading, spacing: 8) {
//                        Text(title ?? "")
//                            .font(.title)
//                        ForEach(Array((content.enumerated())), id: \.offset) { index, element in
//                            Text(element as? String ?? "")
//                        }
//                    }
//                    Spacer()
//                }
//            }
//            .padding()
//            .frame(maxWidth: .infinity, maxHeight: .infinity)
//            .navigationBarTitle(topic ?? "Help", displayMode: .inline)
//            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//        }
//    }
//}
//
//
//struct HelpListView_Previews: PreviewProvider {
//    static var previews: some View {
//        HelpListView()
//    }
//}
