//
//  BookingUIView.swift
//  SwiftProject
//
//  Created by Prabin Phasikawo on 10/3/21.
//

import SwiftUI
import CoreLocation
import Firebase
import FirebaseFirestore
import FirebaseDatabase

struct BookNowUIView: View {
    @StateObject var viewModel:BookingVM = BookingVM()
    @State var pageUrl = ""
    @State var navigateTo = false
    @State var navigate = false
    @State var navigateToLandingPage = false
    @Environment(\.scenePhase) var scenePhase
    let ref = Database.database().reference()
    let identity = UserDefaults.standard.string(forKey: "identity")
    let token = (UserDefaults.standard.string(forKey: "accessToken") ?? "");
    @State private var isFirstVisit: Bool = UserDefaults.standard.bool(forKey: "isFirstVisit")
    var locationManager = LocationTracker.shared.locationManager
    @EnvironmentObject var router: Router
    @EnvironmentObject var appRoot: AppRootManager
    @State var address = ""
    var userLatitude: Double {
        return locationManager.location?.coordinate.latitude ?? -33.868820
    }
    
    var userLongitude: Double {
        return locationManager.location?.coordinate.longitude ?? 151.209290
    }
    
    @State var shortcuts: [shortcutModel] = []
    var body: some View {
        ZStack{
            VStack (spacing: 0){
                GoogleMapsView()
                    .edgesIgnoringSafeArea(.all)
                    .padding(.bottom, -15)
                //                NavigationLink()
                
                VStack {
                    if viewModel.discount != nil && viewModel.discount?.discount_status != "0" {
                        HStack (spacing: 20){
                            Image(systemName: "tag")
                                .font(.system(size: 22))
                            Text("\(viewModel.discount?.discount_message ?? "")")
                                .font(.system(size: 17))
                                .fontWeight(.semibold)
                            Spacer()
                        }
                        .padding()
                        .background(Color.accentColor)
                        .foregroundStyle(Color.white)
                    }
                    
                    VStack {
                        SearchHeader
                        
                        //                    VStack{
                        //                        FavouritesView(shortcuts: self.shortcuts, address: self.address, lat: self.userLatitude, lng: self.userLongitude)
                        //                            .padding(.top, 5)
                        //                        Spacer()
                        //                    }
                        //                    .edgesIgnoringSafeArea(.all)
                    }
                    .frame(maxWidth: .infinity)
                    .padding()
                    .padding(.bottom, 70)
                    .background(Color("Linear2"))
                    .cornerRadius(20)
                    .edgesIgnoringSafeArea(.all)
                }
            }
            .navigationDestination(isPresented: $viewModel.navigateToLandingPage) {
                homeUIView()
            }
            .modifier(LoadingView(isPresented: $viewModel.loading))
            .onAppear() {
                UINavigationBar.customizeBackButton()
                viewModel.initialize()
//                self.appRoot.currentRoot = .ratingScreen(bookingId: "3")
            }
            .onDisappear() {
                viewModel.stopListener()
            }
            .toolbar(.hidden)
            .edgesIgnoringSafeArea(.all)
//            .navigationBarBackButtonHidden(true)
//            .toolbar {
//                ToolbarItem(placement: .navigationBarLeading) {
//                    Button (action : {
//                        router.navigateTo(.menusScreen)
//                    }) {
//                        Image("MenuWhite")
//                            .resizable()
//                            .frame(width: 28, height: 20)
//                        
//                    }
//                }
//            }
            .padding(.bottom, 0)
        }
        
    }
    
}



struct BookNowUIView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            BookNowUIView()
        }
    }
}


extension BookNowUIView {
    var SearchHeader: some View {

        VStack{
            HStack {
                Text("Welcome to Go GulfCoast")
                    .font(.title3)
                    .environment(\.sizeCategory, .medium)
                    .padding(.top)
                Spacer()
            }
            Button(action: {
                viewModel.loading = true
                getAddressFromCoordinates(location: CLLocationCoordinate2D(latitude: userLatitude, longitude: userLongitude)) { address in
                    if let address = address {
                        self.address = address
                        viewModel.loading = false
                        if userLatitude != 0 {
                            router.navigateTo(.placePicker(pAddress: self.address, pLat: userLatitude, pLng: userLongitude))
                        }
                    } else {
                        print("Failed to fetch address")
                    }
                }
                
                
                
            }){
                HStack{
                    Image("MagnifyingGlass")
                        .renderingMode(.template)
                        .foregroundStyle(Color.accentColor)
                    Text("Where do you want to go?")
                        .font(.system(size: 17))
                        .foregroundColor(Color.gray)
                    Spacer()
                }
                .environment(\.sizeCategory, .medium)
            }
            .frame(maxWidth: .infinity)
            .padding(.horizontal, 20)
            .frame(height: 50)
            .background(RoundedRectangle(cornerRadius: 10).fill(Color("FormField")))
            .cornerRadius(8)
        }
        .padding(.bottom, 20)
    }
}
