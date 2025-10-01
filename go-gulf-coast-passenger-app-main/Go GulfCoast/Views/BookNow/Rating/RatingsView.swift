//
//  RatingsView.swift
//  GoGulf
//
//  Created by Mac on 12/27/21.
//
//
import SwiftUI
import Combine

struct RatingsView: View {
    @StateObject var viewModel = RatingVM()
    @State var responseData: RatingResponse?
    @State var show = true
    @State var loading = false
    @State var navigateToDashboard = false
    @State private var preferDriver: Bool = false
    @State var bookingId: String
    //    let bookingId = UserDefaults.standard.string(forKey: "bookingId")
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    @EnvironmentObject var appRootManager: AppRootManager
    @State var cancellables = Set<AnyCancellable>()
    @Environment(\.presentationMode) var presentationMode
    @State var selection = 10
    
    
    var body: some View {
        ZStack {
            VStack {
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
                    
                    Section(header: Text("Add a tip"),footer: Text("It is never too late to say thank you to your personal driver!")){
                        HStack{
                            Text("$")
                                .foregroundStyle(Color.black)
                                .font(.system(size: 20))
                            TextField("0", text: $viewModel.tip)
                                .font(.system(size: 20))
                                .lineLimit(3)
                                .fontWeight(.medium)
                                .keyboardType(.numberPad)
                        }
                    }
                    .listRowBackground(Color("Card"))
                    
                    
                }
                .clearListBackground()
                
                Button(action: {
                    viewModel.submitRating(id: bookingId, router: router, appRoot: appRootManager, tabRouter: tabRouter)
                }) {
                    Text("Submit")
                        .fullWithButton()
                }
                .padding()
            }
            .onReceive(viewModel.viewDismissalModePublisher) { shouldDismiss in
                if shouldDismiss {
                    self.presentationMode.wrappedValue.dismiss()
                }
            }
            .modifier(
                AlertView(isPresented: $viewModel.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, secondaryAction: {
                    viewModel.showAlert = false
                })
            )
            .navigationBarBackButtonHidden(true)
            .toolbarRole(.editor)
            .navigationBarTitle(Text("Ratings"), displayMode: .inline)
            .modifier(LoadingView(isPresented: $viewModel.loading))
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .onAppear() {
                UINavigationBar.customizeBackButton()
            }
            .onDisappear(){
                UserDefaults.standard.removeObject(forKey: "bookingId")
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button (action : {
                        viewModel.skipRating(id: bookingId, router: router, appRoot: appRootManager, tabRouter: tabRouter)
                    }) {
                        Text("Skip").bold()
                            .foregroundStyle(Color.red)
                    }
                }
            }
            
            
            if self.loading {
                ActivityIndicator()
            }
        }
        .disabled(self.loading)
    }
}

struct RatingsView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            RatingsView(bookingId: "")
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


struct StarRatingView: View {
    @Binding var rating: Double
    let maxRating: Double
    let starSize: CGFloat
    let starSpacing: CGFloat
    
    var body: some View {
        HStack(spacing: starSpacing) {
            ForEach(1...Int(maxRating), id: \.self) { index in
                Image(systemName: index <= Int(rating) ? "star.fill" : "star")
                    .resizable()
                    .frame(width: starSize, height: starSize)
                    .foregroundColor(Color.yellow)
                    .onTapGesture {
                        rating = Double(index)
                    }
                    .gesture(
                        DragGesture(minimumDistance: 0)
                            .onChanged { value in
                                let starWidth = starSize + starSpacing
                                let newRating = Double(value.location.x / starWidth)
                                rating = min(maxRating, max(0, newRating + 1))
                            }
                    )
            }
        }
    }
}
