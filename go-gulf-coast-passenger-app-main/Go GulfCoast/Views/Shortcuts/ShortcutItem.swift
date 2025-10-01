//
//  ShortcutItem.swift
//   GoGulf
//
//  Created by Mac on 6/9/22.
//

import SwiftUI

struct ShortcutItem: View {
    var icon: String
    var title: String
    var isActive: Bool
    var body: some View {
        VStack(alignment: .center) {
            Image("\(icon.lowercased())")
                .renderingMode(.template)
                .font(.title)
                .foregroundColor(Color("AccentColor"))
            Text("\(title)")
                .font(.caption2)
                .foregroundColor(.black)
        }
        
        .frame(width: 65, height: 55, alignment: .center)
        .padding(.vertical)
        .padding(.horizontal, 5)
        .background(isActive ? Color("Card") : Color.clear)
        .cornerRadius(10)
    }
}

struct ShortcutItem_Previews: PreviewProvider {
    static var previews: some View {
        ShortcutItem(icon: "office", title: "Office", isActive: true)
    }
}
