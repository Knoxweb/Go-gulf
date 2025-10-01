//
//  CountryPickerExt.swift
//  Aelfo
//
//  Created by Prabin Phasikawo on 06/11/2023.
//

import SwiftUI
import Combine

extension PhoneVerifyUIView {
    var CountryTextView: some View {
        HStack {
            Button(action: {
                presentSheet = true
                keyIsFocused = false
            }) {
                HStack {
                    Text("\(countryFlag)")
                        .font(.system(size: 30))
                    Image(systemName: "chevron.down")
                        .font(.system(size: 12))
                    Text("\(countryCode)")
                        .padding(.horizontal, 10)
                        .font(.system(size: 17))
                }
                .fontWeight(.light)
                .foregroundColor(foregroundColor)
            }
            
            TextField("123 456 7890", text: $mobPhoneNumber)
                .focused($focusedField, equals: .phone)
                .keyboardType(.numberPad)
                .font(.system(size: 17))
                .onReceive(Just(mobPhoneNumber)) { _ in
                    applyPatternOnNumbers(&mobPhoneNumber, pattern: countryPattern, replacementCharacter: "#")
                }
                .onChange(of: mobPhoneNumber) { item in
                    phoneVerifyVM.phoneNumber = mobPhoneNumber
                }
                .clearListBackground()
        }
        .foregroundColor(.black)
        .padding(.vertical, 10)
        .padding(.horizontal)
        .foregroundColor(.black)
        .frame(maxWidth: .infinity)
        .background(Color("FormField"))
        .clipShape(RoundedRectangle(cornerRadius: 12))
//        .shadow(radius: 5)
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.gray.opacity(0.4), lineWidth: 1))
        .toolbarRole(.editor)
        
        
    }
    func placeholder<Content: View>(
        when shouldShow: Bool,
        alignment: Alignment = .leading,
        @ViewBuilder placeholder: () -> Content) -> some View {
            
            ZStack(alignment: alignment) {
                placeholder().opacity(shouldShow ? 1 : 0)
                self
            }
        }
    func hideKeyboard() {
        let resign = #selector(UIResponder.resignFirstResponder)
        UIApplication.shared.sendAction(resign, to: nil, from: nil, for: nil)
    }
    var foregroundColor: Color {
        if colorScheme == .dark {
            return Color(.white)
        } else {
            return Color(.black)
        }
    }
    
    var backgroundColor: Color {
        if colorScheme == .dark {
            return Color(.systemGray5)
        } else {
            return Color(.systemGray6)
        }
    }
    
    func applyPatternOnNumbers(_ stringvar: inout String, pattern: String, replacementCharacter: Character) {
        var pureNumber = stringvar.replacingOccurrences( of: "[^0-9]", with: "", options: .regularExpression)
        for index in 0 ..< pattern.count {
            guard index < pureNumber.count else {
                stringvar = pureNumber
                return
            }
            let stringIndex = String.Index(utf16Offset: index, in: pattern)
            let patternCharacter = pattern[stringIndex]
            guard patternCharacter != replacementCharacter else { continue }
            pureNumber.insert(patternCharacter, at: stringIndex)
        }
        stringvar = pureNumber
    }
}
