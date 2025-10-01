//
//  Components.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 02/10/2024.
//

import SwiftUI
import Foundation

struct ListWithIcon: View {
    @State var icon: String?
    @State var title: String?
    @Binding var caption: String?
    @State var systemName: String?
    var body: some View {
        HStack (alignment: .center, spacing: 20){
            Image("\(icon ?? "")")
                .renderingMode(.template)
                .resizable()
                .foregroundStyle(Color.black)
                .scaledToFit()
                .frame(width: 22, height: 18)
                .frame(maxWidth: 22)
            VStack {
                HStack {
                    VStack (alignment: .leading, spacing: 3){
                        if let text = title {
                            Text(text)
                                .font(.system(size: 16))
                                .foregroundStyle(Color.black)
                        }
                        if caption != nil && caption != "" {
                            Text(caption ?? "")
                                .font(.system(size: 13))
                        }
                    }
                    .frame(minHeight: 40)
                    .padding(.vertical, 14)
                    Spacer()
                    Group {
                        if let icon = systemName {
                            Image(systemName: icon)
                        }
                        else {
                            Image(systemName: "chevron.right")
                        }
                    }
                    .foregroundColor(Color.gray)
                    .fontWeight(.bold)
                    .font(.system(size: 14))
                    .padding(.trailing)
                  
                      
                }
                Divider()
                    .frame(height: 1)
                    .overlay(.gray.opacity(0.5))
                    .padding(.trailing, -30)
            }
        }
    }
}




