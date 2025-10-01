//
//  UIButton.swift
//  Multibrain
//
//  Created by Prabin Phasikawo on 6/8/22.
//

import SwiftUI

struct UIButton: View {
    var label: String?
    var width: CGFloat?
    var bgColor: Color?
    var color: Color?
    var height: CGFloat?
    var body: some View {
        Text("\(label ?? "")")
            .fontWeight(.bold)
            .frame(width: width ?? 150, height: height ?? 50)
            .background(bgColor ?? Color.accentColor)
            .cornerRadius(10.0)
            .font(.system(size: 18))
            .shadow(radius: 8)
            .foregroundColor(color ?? Color("ThemeColor"))
    }
}

struct UIButton_Previews: PreviewProvider {
    static var previews: some View {
        UIButton()
    }
}
