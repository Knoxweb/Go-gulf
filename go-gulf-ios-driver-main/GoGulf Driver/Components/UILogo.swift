//
//  Logo.swift
//  Multibrain
//
//  Created by Prabin Phasikawo on 6/8/22.
//

import SwiftUI

struct UILogo: View {
    var width: CGFloat?
    var height: CGFloat?
    var body: some View {
        HStack {
            Spacer()
            Image("Logo")
                .resizable()
                .aspectRatio(contentMode: .fill)
                .frame(width: width ?? 100, height: height ?? 50)
                .padding(.bottom, 0)
                .shadow(radius: 4)
            Spacer()
        }
    }
}

struct UILogo_Previews: PreviewProvider {
    static var previews: some View {
        UILogo(width: 80, height: 30)
    }
}
