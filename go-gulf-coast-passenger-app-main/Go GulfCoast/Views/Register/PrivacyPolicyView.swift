//
//  PrivacyPolicyView.swift
//  GoGulfDriver
//
//  Created by Prabin Phasikawo on 25/08/2023.
//

import SwiftUI


import SwiftUI
import WebKit


struct PrivacyPolicyView: View {
    @State var text = ""
    @StateObject var VM: PolicyVM = PolicyVM()
    var show: String?
    var body: some View {
        ZStack {
            HStack{
                Text("\(VM.name)")
                    .foregroundColor(.black)
            }
            .padding(.all, 8)
            .frame(maxWidth: .infinity)
            .background(.black).opacity(0.9)
            
//            ScrollView {
//
//                VStack {
                    WebView(text: $VM.data)
                        .font(.title)
                        .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, idealHeight: UIScreen.main.bounds.height, maxHeight: .infinity, alignment: .center)
//                }
//                Spacer()
//            }
            .navigationBarTitle("Privacy Policy", displayMode: .inline)
            .navigationDestination(isPresented: $VM.navigate){
                DashboardUIView()
            }
            .task {
                VM.getTerm()
            }
            if VM.loading {
                ActivityIndicator()
            }
        }
        .disabled(VM.loading)
    }
}

struct PrivacyPolicyView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            PrivacyPolicyView()
        }
    }
}
