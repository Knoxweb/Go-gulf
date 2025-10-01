//
//  OTPUIView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 10/3/21.
//

import SwiftUI

struct OTPUIView: View {
    @StateObject var viewModel = OTPVM()
    var show : Bool
    var ID : String
    var phoneCC : String
    var phoneNumber : String
    let textBoxWidth = UIScreen.main.bounds.width / 8
    let textBoxHeight = UIScreen.main.bounds.width / 8
    let spaceBetweenLines: CGFloat = 10
    let paddingOfBox: CGFloat = 1
    var textFieldOriginalWidth: CGFloat {
        (textBoxWidth*6)+(spaceBetweenLines*3)+((paddingOfBox*2)*3)
    }
    @FocusState private var focusedField: Field?
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    enum Field: Int, Hashable {
       case otp1
    }
    
    var body: some View {
        ZStack {
            VStack {
                HStack{
                    Text("Enter 6 digit OTP code sent at \(phoneCC) \(phoneNumber)")
                        .foregroundStyle(Color.gray)
                    Spacer()
                }
                .padding(.horizontal)
                
                ZStack {
                    
                    HStack (spacing: spaceBetweenLines){
                        otpText(text: viewModel.otp1, isNextTyped: $viewModel.isNextTypedArr[0])
                        otpText(text: viewModel.otp2, isNextTyped: $viewModel.isNextTypedArr[1])
                        otpText(text: viewModel.otp3, isNextTyped: $viewModel.isNextTypedArr[2])
                        otpText(text: viewModel.otp4, isNextTyped: $viewModel.isNextTypedArr[3])
                        otpText(text: viewModel.otp5, isNextTyped: $viewModel.isNextTypedArr[4])
                        otpText(text: viewModel.otp6, isNextTyped: $viewModel.isNextTypedArr[5])
                    }
                    
                    TextField("", text: $viewModel.otpField) { isEditing in
                        viewModel.isEditing = isEditing
                    }
                    .focused($focusedField, equals: .otp1)
                    .frame(width: viewModel.isEditing ? 0 : textFieldOriginalWidth, height: textBoxHeight)
                    .textContentType(.oneTimeCode)
                    .foregroundColor(.clear)
                    .accentColor(.clear)
                    .background(Color.clear)
                    .keyboardType(.numberPad)
                }
                HStack{
                    Spacer()
                    Text("Didn't get a Code?")
                        .foregroundStyle(Color.gray)
                    if viewModel.showResendLabel {
                        Text("Resend Now")
                            .foregroundStyle(Color.accentColor)
                            .onTapGesture(perform: {
                                viewModel.startTimer()
                                viewModel.resendCode(phoneCC: phoneCC, phoneNumber: phoneNumber)
                            })
                    }
                    else {
                        Text("\(viewModel.formattedTime())")
                            .foregroundStyle(Color.gray)
                    }
                    Spacer()
                }
                .font(.system(size: 13))
                .padding(.top, 20)
                .padding(.bottom, 12)
                .fontWeight(.medium)
                Spacer()
            }
            .toolbarRole(.editor)
            .navigationBarTitle("Enter OTP", displayMode: .large)
            .navigationBarBackButtonHidden(viewModel.loading)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                        Button(action: {
                            viewModel.submitOTPCode(router: router, appRoot: appRootManager, ID: self.ID, phoneCC: "\(self.phoneCC)", phoneNumber: self.phoneNumber)
                        }) {
                            Text("Done")
                        }
                }
            }
            .navigationDestination(isPresented: $viewModel.navigateToDashboard) {
                if UserDefaults.standard.string(forKey: "accessToken") != "" {
                    BookNowUIView()
                }
            }
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .onAppear() {
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.15) {
                    focusedField = Field.otp1
                }
                print(self.ID, "on iotptppppppppp", self.phoneCC, self.phoneNumber)
                viewModel.firebaseGeneratedId = ID
            }
            
            if viewModel.loading {
                ActivityIndicator()
            }
        }
        .disabled(viewModel.loading)
    }
    
    
    private func otpText(text: String, isNextTyped: Binding<Bool>) -> some View {
        return Text(text)
            .padding(.top, 6)
            .font(.system(size: 20))
            .frame(width: textBoxWidth, height: textBoxHeight)
            .background(VStack{
                Spacer()
                ZStack {
                    RoundedRectangle(cornerRadius: 1)
                        .frame(width: 55, height: 55)
                        .foregroundColor(Color.clear)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .background(Color("FormField"))
                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.gray.opacity(0.1), lineWidth: 1))
                        .cornerRadius(12)
                }
            })
            .padding(paddingOfBox)
    }
}

struct OTPUIView_Previews: PreviewProvider {
    static var previews: some View {
        OTPUIView(show: true, ID: "ID", phoneCC: "+00", phoneNumber: "9884646466")
    }
}
