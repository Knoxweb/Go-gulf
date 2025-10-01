//
//  ImpInfoView.swift
// SlyykDriverDriver
//
//  Created by Office on 24/11/2022.
//

import SwiftUI

struct ImpInfoView: View {
    let token = (UserDefaults.standard.string(forKey: "accessToken") ?? "");
    @State public var elements = [
        DriverInspect(text: "Any problems encountered duting your race and must be reported to dispatch (delay of plane or train, flat tire, accident, delay of yourself also, etc...)", value: false),
        DriverInspect(text: "Do not take initiative withour informing the dispatch. These are our customers so we know their habits and their degree of requirements", value: false),
    ]
    @State var navigateToHome = false
    @State var navigateToProfile = false
    
    var body: some View {
        VStack(alignment: .center){
            SafetyInspectionBody(elements: $elements, image: "alert", heading: "Important Information", subHeading: "", typo: "")
            VStack(alignment: .center){
                Divider()
                Text("All these rules are there for the smooth running of operations. any negligence towards these rules will be sanctioned and may result in the driver’s final deactivation")
                    .font(.system(size: 19))
                    .italic()
                    .fontWeight(.light)
                    .foregroundColor(Color("AccentColor"))
                    .multilineTextAlignment(.center)
                    .padding(.bottom)
            }
            .padding(.horizontal)
            Spacer()
            Button(action: {
                withAnimation(.default){
                    var condition: Bool = true
                    for d in self.elements {
                        if !d.value {
                            condition = false
                            break;
                        }
                    }
                    if (condition) {
                        if(token != "") {
                            self.navigateToProfile.toggle()
                        }
                        else{
                            self.navigateToHome.toggle()
                        }
                    }
                }
            }, label: {
                Image(systemName: "chevron.right")
                    .font(.system(size: 20, weight: .semibold))
                    .foregroundColor(Color("ThemeColor"))
                    .frame(width: 60, height: 60)
                    .background(Color.accentColor)
                    .clipShape(Circle())
                    .overlay(
                        ZStack {
                            Circle()
                                .stroke(Color.accentColor.opacity(0.04), lineWidth: 4)
                            
                            Circle()
                                .trim(from: 0, to: 1)
                                .stroke(Color.accentColor, lineWidth: 4)
                                .rotationEffect(.init(degrees: -90))
                        }
                            .padding(-15)
                    )
            })
            .padding(.vertical)
            .padding(.bottom)
            .navigationDestination(isPresented: self.$navigateToProfile) {
                DashboardUIView()
            }
            .navigationDestination(isPresented: self.$navigateToHome) {
                homeUIView()
            }
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 30)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .navigationBarBackButtonHidden(true)
    }
}

struct ImpInfoView_Previews: PreviewProvider {
    static var previews: some View {
        ImpInfoView()
    }
}
