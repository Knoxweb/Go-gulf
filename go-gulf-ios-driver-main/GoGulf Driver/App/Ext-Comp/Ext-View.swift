//
//  Extensions.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 15/08/2024.
//

import SwiftUI


struct CardStyleModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .foregroundColor(.black)
            .frame(maxWidth: .infinity)
            .background(Color("FormField"))
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.gray.opacity(0.2), lineWidth: 1))
    }
}


extension UINavigationBar {
    static func circularBakButton() {
        var backButtonBackgroundImage = UIImage(resource: .circleArrowLeft)
        backButtonBackgroundImage = backButtonBackgroundImage.applyingSymbolConfiguration(.init(paletteColors: [.white, UIColor(Color.white)]))!
        self.appearance().backIndicatorImage = backButtonBackgroundImage
        self.appearance().backIndicatorTransitionMaskImage = backButtonBackgroundImage
    }
}



extension UINavigationBar {
    static func customizeBackButton() {
        var backButtonBackgroundImage = UIImage(resource: .arrowLeft)
        backButtonBackgroundImage = backButtonBackgroundImage.applyingSymbolConfiguration(.init(paletteColors: [UIColor(Color.black), UIColor(Color.white)]))!
        self.appearance().backIndicatorImage = backButtonBackgroundImage
        self.appearance().backIndicatorTransitionMaskImage = backButtonBackgroundImage
    }
}

extension View {
    func cardStyleModifier() -> some View {
        self.modifier(CardStyleModifier())
    }
}



struct TextfieldModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .frame(height: 50)
            .foregroundColor(.black)
            .background(Color("FormField"))
            .clipShape(RoundedRectangle(cornerRadius: 6))
            .overlay(RoundedRectangle(cornerRadius: 6).stroke(Color.gray.opacity(0.4), lineWidth: 1))
    }
}


extension View {
    func textfieldModifier() -> some View {
        self.modifier(TextfieldModifier())
    }
}




struct AlertView: ViewModifier {
    @Binding var isPresented: Bool
    @State var icon: ImageResource?
    @Binding var primaryButtonText: String?
    @State var secondaryButtonText: String?
    @Binding var title: String?
    @Binding var desc: String?
    @State var backdropDismiss: Bool?
    @State var outlineButton: Bool?
    @State var hasDismissButton: Bool?
    var primaryAction: (() -> Void)?
    var secondaryAction: (() -> Void)?
    
    func body(content: Content) -> some View {
        content
            .overlay(
                ZStack {
                    if isPresented {
                        Color(.white)
                            .ignoresSafeArea()
                            .opacity(0.1)
                            .onTapGesture {
                                if backdropDismiss ?? false {
                                    isPresented = false
                                }
                            }
                        VStack (alignment: .center, spacing: 10){
                            if let ico = icon {
                                Image(ico)
                                    .renderingMode(.template)
                                    .resizable()
                                    .scaledToFit()
                                    .frame(width: 80)
                                    .padding(.bottom, 8)
                                    .foregroundStyle(Color.accentColor)
                            }
                            
                            if let heading = title {
                                Text(heading)
                                    .font(.system(size: 24))
                                    .fontWeight(.bold)
                                    .multilineTextAlignment(.center)
                                    .environment(\._lineHeightMultiple, 0.8)
                                    .lineLimit(3)
                                    .fixedSize(horizontal: false, vertical: true)
                            }
                            if let message = desc {
                                Text(message)
                                    .font(.system(size: 14))
                                    .foregroundStyle(Color.gray)
                                    .frame(maxWidth: .infinity, alignment: .center)
                                    .multilineTextAlignment(.center)
                                    .lineLimit(8)
                                    .fixedSize(horizontal: false, vertical: true)
                                    .padding(.bottom)
                            }
                            
                            if let primaryButton = primaryAction {
                                Button(action: primaryButton) {
                                    if outlineButton ?? false {
                                        Text(primaryButtonText ?? "")
                                                .multilineTextAlignment(.center)
                                                .lineLimit(8)
                                                .fixedSize(horizontal: false, vertical: true)
                                                .outlinedButton()
                                        
                                    }
                                    else {
                                            Text(primaryButtonText ?? "")
                                                .multilineTextAlignment(.center)
                                                .lineLimit(8)
                                                .fixedSize(horizontal: false, vertical: true)
                                                .fullWithButton()
                                        
                                    }
                                }
                            }
                            
                            if let secondaryButton = secondaryAction {
                                Button(action: secondaryButton) {
                                    Text(secondaryButtonText ?? "Ok")
                                        .outlinedButton()
                                        .opacity(outlineButton ?? false ? 0.7 : 1)
                                }
                            }
                        }
                        .padding(30)
                        .frame(maxWidth: .infinity)
                        .background(Color.white)
                        .clipShape(RoundedRectangle(cornerRadius: 24))
                        .overlay(RoundedRectangle(cornerRadius: 24).stroke(Color.gray.opacity(0.4), lineWidth: 1))
                        .padding()
                        .shadow(color: Color.gray.opacity(0.5), radius: 20)
                        
                    }
                }
            )
           
    }
}


struct BadgeView: View {
    @Binding var text: String?
    @Binding var bgColor: Color?
    @Binding var foregroundColor: Color?
    var body: some View {
        Text("\(text ?? "")")
            .padding(.horizontal, 8)
            .padding(.vertical, 2)
            .background(
                bgColor ?? Color.accentColor
            )
            .font(.system(size: 13))
            .foregroundColor(foregroundColor ?? Color("ThemeColor"))
            .clipShape(RoundedRectangle(cornerRadius: 6))
    }
}




struct LoadingView: ViewModifier {
    @Binding var isPresented: Bool
    @State var backdropDismiss: Bool?
    @State private var progress = 0.6
    @State private var degree = 270
    var count: Int = 11
    
    func body(content: Content) -> some View {
        content
            .disabled(isPresented)
            .overlay(
                ZStack {
                    if isPresented {
                        Color(UIColor(Color.white))
                            .ignoresSafeArea()
                            .opacity(0.1)
                        VStack {
                            ProgressView()
                                .padding()
                                .frame(width: 80, height: 80)
                                .scaleEffect(1.74)
                                .background(.ultraThinMaterial)
                                .overlay(RoundedRectangle(cornerRadius: 6).stroke(Color("Color"), lineWidth: 1))
                                .clipShape(RoundedRectangle(cornerRadius: 15))
                        }
                    }
                }
            )
    }
    private func getDotSize(_ index: Int) -> CGFloat {
        CGFloat(index)
    }
}
