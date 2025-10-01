//
//  ButtonsUI.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 15/08/2024.
//

import Foundation
import SwiftUI

struct FullWidthButton: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .lineLimit(4)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity)
            .frame(height: 50)
            .foregroundColor(Color.white)
            .background(Color.accent)
            .fontWeight(.bold)
            .shadow(
                color: Color.accentColor.opacity(0.3),
                radius: 8,
                x: 0,
                y: 2
            )
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .kerning(0.25)
            .font(.system(size: 18))
    }
}


struct CustomButton: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .lineLimit(4)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity)
            .frame(height: 50)
            .foregroundColor(Color("ThemeColor"))
            .background(Color.accentColor)
            .shadow(
                color: Color.accentColor.opacity(0.3),
                radius: 8,
                x: 0,
                y: 2
            )
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .kerning(0.25)
            .font(.system(size: 18))
    }
}


struct PlainButton: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .lineLimit(4)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity)
            .frame(height: 50)
            .background(
                Color.accentColor
            )
            
            .background()
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .kerning(0.25)
            .font(.system(size: 18))
    }
}


struct OutlinedButton: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .lineLimit(4)
            .fixedSize(horizontal: false, vertical: true)
            .frame(maxWidth: .infinity)
            .frame(height: 50)
            .foregroundColor(Color.black)
            .background(Color.formField)
            .kerning(0.25)
            .font(.system(size: 18))
            .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.gray.opacity(0.4), lineWidth: 1))
    }
}



extension View {
    func fullWithButton() -> some View {
        self.modifier(FullWidthButton())
    }
    func customButton() -> some View {
        self.modifier(CustomButton())
    }
    
    func outlinedButton() -> some View {
        self.modifier(OutlinedButton())
    }
    
    func plainButton() -> some View {
        self.modifier(PlainButton())
    }
}


