//
//  AddCardView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/29/21.
//

import SwiftUI
import Combine

struct AddEditBankAccount: View {
    @StateObject var viewModel = BankInfoVM()
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    @FocusState private var focusedField: Field?
    @Binding var showBankAccountSheet: Bool
    let bsbLimit = 6
    let accountLimit = 16
    @State var bankName: String?
    @State var holderName: String?
    @State var accountHolder: String?
    @State var bsbNumber: String?
    
    enum Field: Int, Hashable {
        case bankName
        case holderName
        case accountNumber
        case bsbNumber
    }
    
    
    var body: some View {
        VStack {
            ScrollView {
                VStack (spacing: 20){
                    TextFieldView(field: $viewModel.bankName, text: "Bank Name", placeholder: "")
                        .submitLabel(.next)
                        .focused($focusedField, equals: .bankName)
                        .onSubmit { self.focusNextField($focusedField) }
                    
                    TextFieldView(field: $viewModel.holdersName, text: "Holder's Name", placeholder: "")
                        .submitLabel(.next)
                        .focused($focusedField, equals: .holderName)
                        .onSubmit { self.focusNextField($focusedField) }
                    
                    TextFieldView(field: $viewModel.accountNumber, text: "Account Number", placeholder: "", keyboardType: .numbersAndPunctuation)
                        .submitLabel(.next)
                        .focused($focusedField, equals: .accountNumber)
                        .onSubmit { self.focusNextField($focusedField) }
                    
                    TextFieldView(field: $viewModel.bsbNumber, text: "Routing Number", placeholder: "", keyboardType: .numbersAndPunctuation)
                        .submitLabel(.done)
                        .focused($focusedField, equals: .bsbNumber)
                        .onSubmit {  viewModel.submitBankInfo() }
                }
                .padding()
            }
            .navigationBarTitle(Text("Add Bank Account"), displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationBarItems(trailing: Button(action: {
                viewModel.submitBankInfo()
            }) {
                Text("Done").bold()
            })
            .onAppear() {
                viewModel.bankName = bankName ?? ""
                viewModel.holdersName = holderName ?? ""
                viewModel.accountNumber = accountHolder ?? ""
                viewModel.bsbNumber = bsbNumber ?? ""
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
                    self.focusedField = .bankName
                }
            }
            .navigationBarItems(leading: Button(action: {
                self.showBankAccountSheet = false
            }) {
                Text("Cancel").bold().foregroundColor(.gray)
            })
            .modifier(
                AlertView(isPresented: $viewModel.showSuccess, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, primaryAction: {
                    presentationMode.wrappedValue.dismiss()
                })
            )
            
            .modifier(
                AlertView(isPresented: $viewModel.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, primaryAction: {
                    viewModel.showAlert = false
                })
            )
            
            .environment(\.sizeCategory, .medium)
            .modifier(LoadingView(isPresented: $viewModel.loading))
            .onReceive(viewModel.viewDismissalModePublisher) { shouldDismiss in
                if shouldDismiss {
                    self.presentationMode.wrappedValue.dismiss()
                }
            }
            .environment(\.sizeCategory, .medium)
        }
    }
    
    func accountNumberLimit(_ upper: Int) {
        if viewModel.accountNumber.count > upper {
            viewModel.accountNumber = String(viewModel.accountNumber.prefix(upper))
        }
    }
    
    func bsbNumberLimit(_ upper: Int) {
        if viewModel.bsbNumber.count > upper {
            viewModel.bsbNumber = String(viewModel.bsbNumber.prefix(upper))
        }
    }
    
    
}

//struct AddEditBankAccount_Previews: PreviewProvider {
//    static var previews: some View {
//        AddEditBankAccount(showBankAccountSheet: .constant(true), VM: AccountVM())
//    }
//}
