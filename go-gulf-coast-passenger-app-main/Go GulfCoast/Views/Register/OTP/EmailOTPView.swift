//
//  EmailOTPView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 05/09/2024.
//

import SwiftUI
import Combine

struct EmailOTPView: View {
    @StateObject var VM: OTPVM = OTPVM()
    let textBoxWidth = UIScreen.main.bounds.width / 8
    let textBoxHeight = UIScreen.main.bounds.width / 8
    let spaceBetweenLines: CGFloat = 12
    let paddingOfBox: CGFloat = 1
    var textFieldOriginalWidth: CGFloat {
          (textBoxWidth*6)+(spaceBetweenLines*3)+((paddingOfBox*2)*3)
      }
    let email: String?
    @EnvironmentObject private var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    @State private var keyboardHeight: CGFloat = 0
    @Environment(\.presentationMode) var presentationMode
    init(email: String? = nil) {
        self.email = email
        UINavigationBar.customizeBackButton()
    }
    
    var body: some View {
        VStack {
            Spacer()
            VStack (alignment: .leading, spacing: 8){
                Text("Enter OTP Code")
                    .font(.system(size: 18))
                    .foregroundColor(Color.black)
                VStack (alignment: .leading, spacing: 5){
                    ZStack {
                        HStack (spacing: spaceBetweenLines){
                            otpText(text: VM.otp1, isNextTyped: $VM.isNextTypedArr[0])
                            otpText(text: VM.otp2, isNextTyped: $VM.isNextTypedArr[1])
                            otpText(text: VM.otp3, isNextTyped: $VM.isNextTypedArr[2])
                            otpText(text: VM.otp4, isNextTyped: $VM.isNextTypedArr[3])
                            otpText(text: VM.otp5, isNextTyped: $VM.isNextTypedArr[4])
                            otpText(text: VM.otp6, isNextTyped: $VM.isNextTypedArr[5])
                        }
                        TextField("", text: $VM.otpField) { isEditing in
                            VM.isEditing = isEditing
                        }
                        .frame(width: VM.isEditing ? 0 : textFieldOriginalWidth, height: textBoxHeight)
                        .textContentType(.oneTimeCode)
                        .foregroundColor(.clear)
                        .accentColor(.clear)
                        .background(Color.clear)
                        .keyboardType(.numberPad)
                        
                    }
                }
            }
            .foregroundColor(Color.gray)
            Button(action: {
                VM.forgotOTPCheck(email: email)
            }) {
                Text("Continue")
                    .outlinedButton()
            }
            .padding(.top, 40)
            Spacer()
        }
        .padding()
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .modifier(LoadingView(isPresented: $VM.loading))
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert.toggle()
            })
        )
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .toolbarRole(.editor)
    }
    private func otpText(text: String, isNextTyped: Binding<Bool>) -> some View {
        return Text(text)
            .padding(.top, 8)
            .font(.system(size: 14))
            .foregroundColor(Color.gray)
            .frame(width: textBoxWidth, height: textBoxHeight)
            .background(VStack{
                Spacer()
                RoundedRectangle(cornerRadius: 1)
                    .frame(width: 55, height: 55)
                    .background(Color.clear)
                    .foregroundColor(Color.clear)
                    .clipShape(RoundedRectangle(cornerRadius: 10))
                    .overlay(RoundedRectangle(cornerRadius: 6).stroke(Color.gray.opacity(0.2), lineWidth: 1))
            })
            .padding(paddingOfBox)
    }
//    func hideKeyboard() {
//        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
//    }

}

#Preview {
    NavigationView {
        EmailOTPView()
    }
}
