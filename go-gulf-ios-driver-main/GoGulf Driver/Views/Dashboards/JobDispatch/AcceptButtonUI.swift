//
//  AcceptButtonUI.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 28/08/2024.
//

import SwiftUI

struct AcceptButton: View {
    @State private var isJobAccepted = false
    @State var dispatchOffset: CGFloat = 0
    @ObservedObject var VM: DispatchVM
    
    
    var body: some View {
        HStack {
            ZStack(alignment: .leading) {
                HStack {
                    Spacer()
                    Text("Slide to \(VM.activeStatus ? "offline" : "online")")
                        .font(.system(size: 18))
                    Spacer()
                }
                .onChange(of: self.isJobAccepted) { newValue in
                    if newValue {
                        VM.activeStatus.toggle()
                        VM.setOnlineStatus()
                    }
                }
             
                
                Image(systemName: "chevron.forward.2")
                    .resizable()
                    .scaledToFit()
                    .padding(15)
                    .frame(width: 50, height: 50)
                    .background(VM.activeStatus ? Color.red : Color.accentColor)
                    .clipShape(RoundedRectangle(cornerRadius: 50))
                    .foregroundStyle(Color("ThemeColor"))
                    .offset(x: self.dispatchOffset)
                    .gesture(
                        DragGesture()
                            .onChanged { gesture in
                                self.dispatchOffset = max(0, gesture.translation.width)
                                if self.dispatchOffset > UIScreen.main.bounds.width - 150 {
                                    self.dispatchOffset = UIScreen.main.bounds.width - 130
                                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                        self.dispatchOffset = 0
                                        self.isJobAccepted = true
                                    }
                                }
                            }
                            .onEnded { gesture in
                                withAnimation(.spring()) {
                                    if self.dispatchOffset > 50 {
                                        self.dispatchOffset = UIScreen.main.bounds.width - 130
                                        self.isJobAccepted = true
                                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                            self.dispatchOffset = 0
                                            self.isJobAccepted = false
                                        }
                                    } else {
                                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
                                            self.dispatchOffset = 0
                                            self.isJobAccepted = false
                                        }
                                    }
                                }
                            }
                    )
            }
        }
        .frame(height: 50)
        .padding(8)
        .foregroundColor(.black)
        .frame(maxWidth: .infinity)
        .background(Color("FormField"))
        .cornerRadius(40)
    }
}
