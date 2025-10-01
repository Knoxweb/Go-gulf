//
//  TermsConditionView.swift
// SlyykDriver
//
//  Created by Office on 10/10/2022.
//

import SwiftUI

struct LegalView: View {
    @StateObject var VM: HelpVM = HelpVM()
    @State var endpoint: String
    @State var hasToolbar: Bool?
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    var body: some View {
        VStack {
            ScrollView{
                VStack (alignment: .leading, spacing: 30){
                    if VM.legalData?.data?.count ?? 0 > 0 {
                        ForEach(Array((VM.legalData?.data?.enumerated())!), id: \.offset) { index, element in
                            VStack (alignment: .leading, spacing: 10){
                                Text(element.heading ?? "")
                                    .font(.title3)
                                    .multilineTextAlignment(.leading)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                
                                if element.list?.count ?? 0 > 0 {
                                    ForEach(Array((element.list?.enumerated())!), id: \.offset) { i, el in
                                        Text(el.content ?? "")
                                            .font(.system(size: 16))
                                    }
                                }
                            }
                        }
                    }
                }
                .padding()
            }
        }
        .frame(maxWidth: .infinity)
        .toolbarRole(.editor)
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient.ignoresSafeArea())
        .navigationTitle("\(VM.legalData?.title ?? "")")
        .navigationBarTitleDisplayMode(.large)
        .modifier(LoadingView(isPresented: $VM.loading))
        .modifier(
            AlertView(isPresented: $VM.showAlert,  primaryButtonText: .constant("ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert.toggle()
            })
        )
        .toolbar {
            if hasToolbar != nil {
                if hasToolbar ?? false {
                    ToolbarItem(placement: .confirmationAction) {
                        Button(action: {
                            self.appRoot.currentRoot = .tabs
                            self.router.popToRoot()
                        }) {
                            Text("Done")
                        }
                    }
                }
            }
        }
        .onAppear() {
            VM.getLegal(endpoint)
            UINavigationBar.customizeBackButton()
        }
    }
}

#Preview {
    LegalView(endpoint: "")
}
