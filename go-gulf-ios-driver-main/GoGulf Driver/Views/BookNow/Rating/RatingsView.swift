//
//  RatingsView.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 12/27/21.
//

import SwiftUI

struct RatingsView: View {
    @StateObject var viewModel = RatingVM()
    @State var show = true
    @State private var preferDriver: Bool = false
    var bookingId: String
    @State var responseData: RatingResponse?
    @State var loading = false
    @State var navigateToDashboard = false
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    @EnvironmentObject var appRootManager: AppRootManager
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    
    
    var body: some View {
        VStack {
                VStack{
                    Form {
                        Section(header: Text("Choose ratings")){
                            FeedBack(ratings: $viewModel.ratings, show: self.$show).padding()
                                .background(Color("Card"))
                        }
                        .listRowBackground(Color("Card"))
                        Section(header: Text("Write a review")){
                            TextField("", text: $viewModel.message)
                                .lineLimit(3)
                        }
                        .listRowBackground(Color("Card"))
                        
                    }
                    .clearListBackground()
                    Button(action: {
                        viewModel.submitRating(id: bookingId, tabRouter: tabRouter)

                    }) {
                        Text("Submit")
                            .fullWithButton()
                    }
                    .padding()
                }
                
        }
        .onReceive(viewModel.viewDismissalModePublisher) { shouldDismiss in
            if shouldDismiss {
                self.presentationMode.wrappedValue.dismiss()
            }
        }
        .alert(viewModel.alertTitle ?? "", isPresented: $viewModel.showAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text(viewModel.alertMessage ?? "")
        }
        .onAppear() {
            UINavigationBar.customizeBackButton()
        }
        .toolbarRole(.editor)
        .navigationBarBackButtonHidden(true)
        .navigationBarTitle(Text("Ratings"), displayMode: .inline)
        .modifier(LoadingView(isPresented: $viewModel.loading))
        .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
        .toolbar {
            ToolbarItem(placement: .cancellationAction) {
                    Button (action : {
                        viewModel.skipRating(id: bookingId, tabRouter: tabRouter)
                    }) {
                        Text("Skip").bold()
                            .foregroundStyle(Color.red)
                    }
            }
        }
    }
}

struct FeedBack : View {
    @Binding var ratings : Int
    @Binding var show : Bool
    var body : some View{

        VStack(alignment: .center){
            HStack(spacing: 15){
                Spacer()
                ForEach(1...5,id: \.self){i in

                    Image(systemName: self.ratings == 0 ? "star" : "star.fill")
                        .resizable()
                        .frame(width: 30, height: 30)
                        .foregroundColor(i <= self.ratings ? Color.accentColor : Color.black.opacity(0.2))
                        .onTapGesture {

                            self.ratings = i
                        }
                }
                Spacer()
            }.padding()

            VStack{

                if self.ratings != 0{

                    if self.ratings == 5{

                        Text("Excellent").fontWeight(.bold).foregroundColor(Color.accentColor)
                    }
                    else if self.ratings == 4 {

                        Text("Good").fontWeight(.bold).foregroundColor(Color.accentColor)
                    }
                    else{

                        Text("Okay").fontWeight(.bold).foregroundColor(Color.accentColor)
                    }
                }

            }.padding(.top, 20)

        }
        .background(.clear)
        .cornerRadius(10)
    }
}
