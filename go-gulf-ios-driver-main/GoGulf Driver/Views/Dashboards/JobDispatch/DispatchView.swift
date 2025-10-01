//
//  DispatchView.swift
//  SlyykDriver
//
//  Created by Office on 17/01/2023.
//

import SwiftUI
import Firebase
import AudioToolbox
import AVFoundation

struct DispatchView: View {
    @State var showMenu = false
    @StateObject var VM: DispatchVM = DispatchVM()
    @State private var stacked = true
    @State  var loading = false
    @State var TimeModel: [TimerModel] = []
    @State var shouldAnimate = false
    let identity = UserDefaults.standard.string(forKey: "identity")
    let ref = Database.database().reference()
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    @State var accepted = false
    @State private var drawingWidth = false
    @State var scrollNav = false
    
    var body: some View {
        VStack (spacing: 0){
            VStack {
                Text("Job Board")
                    .fontWeight(.bold)
                Picker("Job Type", selection: $VM.selection) {
                    ForEach(VM.jobType, id: \.self) {
                        Text($0.capitalized)
                    }
                }
                .pickerStyle(SegmentedPickerStyle())
            }
            .padding(.bottom)
            .padding(.horizontal)
            .background(.ultraThinMaterial.opacity(scrollNav ? 1: 0))
            
            ScrollView {
                VStack {
                    if VM.selection == "standby" {
                        if VM.JobsModel.count > 0 {
                            VStack (spacing: 20){
                                ForEach(Array(VM.JobsModel.enumerated()), id: \.offset) { (index, item) in
                                    VStack {
                                        DispatchJobCard(VM: VM, index: index, item: item)
                                    }
                                }
                                Spacer()
                            }
                        }
                        else {
                            if VM.profileData != nil && !(VM.profileData?.is_approved ?? false) {
                                DocumentMissingView()
                                Spacer()
                            }
                            else {
                                if VM.activeStatus {
                                    VStack {
                                        
                                        Spacer()
                                        Image(.homeLogo)
                                            .resizable()
                                            .scaledToFit()
                                            .frame(width: 100)
                                        ThreeBounceAnimation()
                                            .padding(.vertical)
                                        Text("Waiting for jobs")
                                        Spacer()
                                        
                                        
                                    }
                                    .frame(maxHeight: .infinity)
                                    .padding(.top, 150)
                                }
                                else {
                                    Text("You are offline")
                                        .cardStyleModifier()
                                }
                            }
                            
                            
                        }
                    }
                    else {
                        VStack {
                            if VM.scheduledData.count > 0 {
                                ForEach(Array(VM.scheduledData.enumerated()), id: \.offset) { (index, item) in
                                    AvailableCardView(element: .constant(item), VM: VM)
                                }
                            }
                            else {
                                Text("No schedule jobs")
                                    .cardStyleModifier()
                            }
                        }
                    }
                }
                .padding()
            }
            .simultaneousGesture(
                DragGesture()
                    .onChanged { value in
                        if value.translation.height < 100 {
                            scrollNav = true
                        } else {
                            scrollNav = false
                        }
                    }
                    .onEnded { value in
                        if value.translation.height > 100 {
                            scrollNav = false
                        } else {
                            scrollNav = true
                        }
                    }
            )
            
            if VM.selection == "standby" {
                VStack {
                    AcceptButton(VM: VM)
                }
                .padding(.horizontal)
                .padding(.bottom, 30)
            }
        }
        .toolbar(.hidden)
        .modifier(LoadingView(isPresented: $VM.loading))
        .frame(maxWidth: .infinity)
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .modifier(
            AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                VM.showAlert = false
            })
        )
        .onAppear(){
            UINavigationBar.customizeBackButton()
            VM.initialize()
        }
        .onDisappear(){
            VM.stopListener()
        }
        
    }
}

struct SimpleLoadingBar: View {
    @State private var offset: CGFloat = -1.0
    @State private var isAnimating = false // Control animation state
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .leading) {
                // Background for the progress bar
                Rectangle()
                    .foregroundColor(.gray.opacity(0.2))
                    .frame(height: 6)
                    .cornerRadius(6)
                    .onAppear {
                        if !isAnimating {
                            self.offset = -1.0
                            withAnimation(
                                Animation.linear(duration: 1)
                                    .repeatForever(autoreverses: false)
                            ) {
                                self.offset = 1.5
                            }
                            isAnimating = true
                        }
                    }
                    .onDisappear() {
                        self.offset = -1.0
                        withAnimation(nil) {}
                    }
                
                Rectangle()
                    .foregroundColor(Color.accentColor)
                    .frame(width: geometry.size.width * 1, height: 6) // Fraction of total width
                    .offset(x: self.offset * geometry.size.width)
                    .cornerRadius(6)
            }
        }
        .frame(height: 6)
        .onAppear() {
            self.isAnimating = false
        }
    }
}

struct DocumentMissingView: View {
    @EnvironmentObject var tabRouter: TabRouter
    var body: some View {
        VStack (spacing: 20){
            HStack (spacing: 15){
                Image(.documentIcon)
                    .renderingMode(.template)
                    .resizable()
                    .scaledToFit()
                    .foregroundStyle(Color.black)
                    .frame(width: 35)
                
                Text("Please complete your driver and vehicle documents in our system to receive job offers and wait for admin approval.")
                    .font(.system(size: 16))
                    .foregroundStyle(Color.gray)
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)
                Spacer()
            }
            Button(action: {
                self.tabRouter.selectedTab = .profile
            }) {
                Text("Complete my documents")
                    .fullWithButton()
            }
        }
        .padding(.vertical, 30)
        .padding(.horizontal, 30)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(style: StrokeStyle(
                    lineWidth: 2,
                    dash:  [5, 4]
                ))
                .foregroundColor(Color.gray.opacity(0.5))
        )
    }
}
