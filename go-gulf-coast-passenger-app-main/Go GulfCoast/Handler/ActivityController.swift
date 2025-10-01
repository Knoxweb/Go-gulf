//
//  ActivityController.swift
//   GoGulf
//
//  Created by Mac on 6/24/22.
//
import UIKit
import SwiftUI

struct ActivityIndicator: View {
    
    var body: some View {
        ZStack{
            Color(.systemBackground)
                .ignoresSafeArea()
                .opacity(0.2)
            
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle(tint: Color("AccentColor")))
                .scaleEffect(2.0, anchor: .center)
                .padding(.all, 40)
                .background(Color("Card").edgesIgnoringSafeArea(.all))
                .cornerRadius(20)
                .shadow(radius: 20)
        }
        
    }
}
