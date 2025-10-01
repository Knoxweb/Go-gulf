//
//  RatingUIView.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 27/09/2022.
//

import SwiftUI
import FirebaseDatabase
import UIKit

struct RatingUIView: View {
    @StateObject var viewModel: RatingVM = RatingVM()
    let ref = Database.database().reference()
    let bookingId = (UserDefaults.standard.string(forKey: "bookingId") ?? "");
    //    var bookingId: String
    @State var title = ""
    @State var message = ""
    @State var driverLat: Double = 0
    @State var driverLng: Double = 0
    @State var buttonLabel = ""
    @State var mode = ""
    @State var navigateToRating = false
    @State var ratings = 0
    @State var comment = ""
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRootManager: AppRootManager
    
    var body: some View {
        let currentData = viewModel.bookingResponse?.data
        ZStack {
            VStack (spacing: 0){
                VStack{
                    Spacer()
                    VStack{
                        AsyncImage(url: URL(string: currentData?.driverUser.imageLink ?? "")) { image in
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                        } placeholder: {
                            Image(systemName: "photo")
                                .imageScale(.large)
                                .foregroundColor(.gray)
                        }
                        .frame(width: 100, height: 100)
                        .clipShape(Circle())
                        .shadow(radius: 4)
                        .padding(.top, 25)
                        
                        Text(currentData?.message2 ?? "")
                        
                        HStack(spacing: 15){
                            Spacer()
                            ForEach(1...5,id: \.self){ i in
                                Image(systemName: viewModel.ratings == 0 ? "star" : "star.fill")
                                    .resizable()
                                    .frame(width: 30, height: 30)
                                    .foregroundColor(i <= viewModel.ratings ? Color.accentColor : Color.accentColor.opacity(0.2))
                                    .onTapGesture {
                                        viewModel.ratings = i
                                    }
                            }
                            Spacer()
                            
                        }.padding()
                        
                        VStack{
                            if #available(iOS 16.0, *) {
                                TextField("", text: $viewModel.message, axis: .vertical)
                                    .padding()
                                    .foregroundColor(.black)
                                    .background(Color("Card"))
                                    .cornerRadius(20)
                            } else {
                                TextField("", text: $viewModel.message)
                                    .padding()
                                    .foregroundColor(.black)
                                    .background(Color("Card"))
                                    .cornerRadius(20)
                            }
                        }
                        .padding(.horizontal, 20)
                        Button(action: {
//                            viewModel.submitRating()
                        }){
                            UIButton(label: "Leave a Comment", width: 200)
                        }
                        .padding(.vertical)
                    }
                    .padding(.all, 30)
                    .offset(x: 0, y: -90)
                    Spacer()
                }
                .navigationDestination(isPresented: $viewModel.navigateToDashboard) {
                    BookNowUIView()
                }
            }
            .onAppear(){
                print(self.bookingId, "sdfdsfdsfdsfdsfsdfsdf sdfsdfsd sdfdsfds")
            }
            
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button (action : {
                        router.navigateTo(.menusScreen)
                    }) {
                            Image("MenuWhite")
                                .resizable()
                                .frame(width: 28, height: 20)
                        
                    }
                }
            }
            .padding(.bottom, 0)
            if viewModel.loading {
                ActivityIndicator()
            }
        }
        .disabled(viewModel.loading)
    }
}

struct RatingUIView_Previews: PreviewProvider {
    static var previews: some View {
        RatingUIView()
    }
}
