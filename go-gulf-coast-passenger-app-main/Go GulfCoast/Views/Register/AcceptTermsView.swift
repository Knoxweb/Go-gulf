//
//  AcceptTermsView.swift
//  GoGulf
//
//  Created by Office on 19/04/2023.
//

import SwiftUI
import WebKit


struct WebView: UIViewRepresentable {
    @Binding var text: String
    
    func makeUIView(context: Context) -> WKWebView {
        return WKWebView()
    }
    
    func updateUIView(_ uiView: WKWebView, context: Context) {
        uiView.loadHTMLString(text, baseURL: nil)
    }
}


struct AcceptTermsView: View {
    @State var text = ""
    @StateObject var VM: TermsVM = TermsVM()
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    
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
            .navigationBarTitle("Terms of Service", displayMode: .inline)
//            .navigationDestination(isPresented: $VM.navigate){
//                DashboardUIView()
//            }
            .toolbar{
                ToolbarItem(placement: .confirmationAction){
                    if show == "true" {
                        Button(action: {
                            VM.readTerms(router: router, appRoot: appRootManager)
                        }) {
                            Text("Submit")
                        }
                    }
                    else {
                        
                    }
                }
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

struct AcceptTermsView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            AcceptTermsView()
        }
    }
}
