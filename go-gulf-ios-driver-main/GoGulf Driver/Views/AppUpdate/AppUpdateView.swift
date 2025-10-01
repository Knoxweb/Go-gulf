//
//  AppUpdateView.swift
//  DoubleLeap
//
//  Created by Office on 12/02/2023.
//

import SwiftUI

struct AppUpdateView: View {
    @State var navigate = false
    let token = (UserDefaults.standard.string(forKey: "accessToken") ?? "");
    var body: some View {
        VStack{
            Spacer()
            VStack (spacing: 20){
                Image(systemName: "arrow.up.doc.on.clipboard")
                    .font(.system(size: 75))
                    .foregroundColor(Color.accentColor)
                Text("Time To Update!")
                    .fontWeight(.bold)
                
            } 
            VStack{
                Text("We added lots of new features and fix some bugs to make your experience as smooth as possible")
                    .multilineTextAlignment(.center)
                    .lineLimit(5)
                    .opacity(0.5)
            }
            .padding(.top, 5)
            Spacer()
            Button(action: {updateApp()}) {
                    Text("Update")
                    .padding()
                    .frame(width: 180)
                    .background(Color("AccentColor"))
                    .foregroundColor(.black)
                    .cornerRadius(8)
                    .padding()
            }
            Button(action: {
                self.navigate = true
            }){
                Text("May be later")
                    .foregroundColor(.gray)
                    
            }
        }
        .padding(.horizontal, 30)
        .navigationBarHidden(true)
        .navigationDestination(isPresented: self.$navigate){
            if(token != "") {
                    DashboardUIView()
            }
            else{
                PhoneVerifyUIView()
            }
        }
    }
    func updateApp(){
        if let url = URL(string: "itms-apps://itunes.apple.com/app/id1585805676") {
            UIApplication.shared.open(url)
        }
    }
}

struct AppUpdateView_Previews: PreviewProvider {
    static var previews: some View {
        AppUpdateView()
    }
}
