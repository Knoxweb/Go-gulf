//
//  Extra.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 12/22/21.
//

import SwiftUI
import Combine
import Kingfisher
//let coloredNavAppearance = UINavigationBarAppearance()
//
struct QuoteConfirmView: View {
    @StateObject var viewModel: QuoteConfirmVM = QuoteConfirmVM()
    var quoteData: QuoteResponse?
    var fleetData: FleetList?
    var passenger: Int?
    @EnvironmentObject var router: Router
    @EnvironmentObject var tabRouter: TabRouter
    @EnvironmentObject var appRootManager: AppRootManager
    @State var showCard = false
    @State var chooseCardSheet = false
    @State var showDismiss = false
    
    var body: some View {
            Form {
                Section {
                    VStack {
                        HStack{
                            Spacer()
                            VStack(alignment: .center, spacing: 0) {
                                KFImage(URL(string: fleetData?.imagePath ?? ""))
                                    .placeholder {
                                        Image("fleeta")
                                            .resizable()
                                            .scaledToFit()
                                            .padding(.horizontal, 40)
                                            .frame(maxWidth: .infinity)
                                            .zIndex(8)
                                            .background(
                                                RoundedRectangle(cornerRadius: 15, style: .continuous).fill(Color("Card"))
                                                    .frame(maxWidth: .infinity)
                                                    .padding(.bottom, -5)
                                                    .offset(x: 0, y: 80)
                                            )
                                            .opacity(0)
                                    }
                                    .resizable()
                                    .scaledToFit()
                                    .padding(.horizontal, 40)
                                    .frame(maxWidth: .infinity)
                                    .zIndex(8)
                                    .background(
                                        RoundedRectangle(cornerRadius: 15, style: .continuous).fill(Color("Card"))
                                            .frame(maxWidth: .infinity)
                                            .padding(.bottom, -5)
                                            .offset(x: 0, y: 80)
                                    )
                                
                            }
                            .padding(.horizontal, -10)
                            Spacer()
                        }
                        .listRowSeparator(.hidden)
                        
                    }
                    
                    Section {
                        HStack {
                            Spacer()
                            HStack {
                                Image("PassengersWhite")
                                    .renderingMode(.template)
                                    .resizable()
                                    .foregroundStyle(Color.black)
                                    .scaledToFit()
                                    .frame(width: 15, height: 15)
                                Text("\(fleetData?.passengers ?? 0)")
                                    .foregroundColor(.black)
                            }
                            .padding(.horizontal, 4)
                            Text("")
                            HStack {
                                Image("PetsWhite")
                                    .renderingMode(.template)
                                    .resizable()
                                    .foregroundStyle(Color.black)
                                    
                                    .scaledToFit()
                                    .frame(width: 15, height: 15)
                                Text("\(fleetData?.pet ?? 0)")
                                    .foregroundColor(.black)
                            }
                            .padding(.horizontal, 4)
                            Text("")
                            HStack {
                                Image("WheelchairWhite")
                                    .renderingMode(.template)
                                    .resizable()
                                    .foregroundStyle(Color.black)
                                    
                                    .scaledToFit()
                                    .frame(width: 15, height: 15)
                                Text("\(fleetData?.wheelchair ?? 0)")
                                    .foregroundColor(.black)
                            }
                            .padding(.horizontal, 4)
                            Spacer()
                        }
                        .font(.caption)
                    }
                    .listRowBackground(Color("Card"))
                    .listRowSeparator(.hidden)
                
                    BookingSummary(quoteData: quoteData, fleetData: fleetData, passenger: passenger)
                }
                .listRowInsets(EdgeInsets())
                .listRowBackground(Color.clear)
                
                
                .listRowBackground(Color("Card"))
                
                DisclosureGroup("Capacity", isExpanded: $viewModel.expanded) {
                    CapacityView(viewModel: viewModel, element: fleetData)
                }
                .listRowBackground(Color("Card"))
                
                //                if  quoteData?.isAirport == 1 {
//                FlightInfo(viewModel: viewModel)
                //                }
                
                QuoteExtraView
                
                //                PaymentCardUI
            }
            .clearListBackground()
            .toolbarRole(.editor)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .onAppear(){
                viewModel.initialize(tabRouter: tabRouter)
                UINavigationBar.customizeBackButton()
                UITableView.appearance().separatorColor = .clear
                viewModel.passenger = passenger ?? 1
            }
            .clearListBackground()
            .navigationBarTitle("Confirmation", displayMode: .inline)
            .navigationBarBackButtonHidden(viewModel.loading)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .modifier(LoadingView(isPresented: $viewModel.loading))
        
            .modifier(
                AlertView(isPresented: $viewModel.successAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, primaryAction: {
                    appRootManager.currentRoot = .tabs
                    router.popToRoot()
                })
            )
        
            .modifier(
                AlertView(isPresented: $viewModel.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, secondaryAction: {
                    viewModel.showAlert = false
//                    tabRouter.selectedTab = .ride
//                    router.popToRoot()
                })
            )
        
//            .alert(viewModel.alertTitle ?? "", isPresented: $viewModel.successAlert) {
//                Button("OK", role: .cancel) {
////                    self.appRootManager.currentRoot = .bookNowScreen
////                    self.router.popToRoot()
//                }
//            } message: {
//                Text(viewModel.alertMessage ?? "")
//            }
//        
        
            VStack {
                HStack {
                    Text("Total Fare")
                    Spacer()
                    VStack {
                        if viewModel.discountApplied {
                            Text("€\(fleetData?.fare ?? "")")
                                .font(.system(size: 13))
                                .strikethrough()
                            Text("€\(String(format:"%.2f", viewModel.discountedAmount))")
                        }
                        else {
                            Text("€\(fleetData?.fare ?? "")")
                        }
                    }
                    
                }
                .foregroundColor(.gray)
                .fontWeight(.semibold)
                
//                HStack {
//                    Text("Total Fare")
//                    Spacer()
//                    VStack (alignment: .trailing) {
//                        Text("$ \(fleetData?.fare ?? "")")
//                            .fontWeight(.bold)
//                            .font(.system(size: 22))
//                            .foregroundStyle(Color.accentColor)
//                        Text("Inclusive GST")
//                            .font(.caption2)
//                    }
//                }
                .foregroundColor(.gray)
                Button(action: {
                    viewModel.confirmQuoteSubmit(quoteFleetId: fleetData?.id ?? 0, tabRouter: tabRouter)
                }) {
                    Text("Confirm Booking")
                        .fullWithButton()
                }
                Text("*You will be fully charged, if driver couldn't find you during pickup.")
                    .multilineTextAlignment(.leading)
                    .fixedSize(horizontal: false, vertical: true)
                    .font(.caption)
                    .foregroundStyle(Color.gray)
            }
            .padding(.horizontal)
            .padding(.top)
            .background(Color("Linear2").edgesIgnoringSafeArea(.all))
            .padding(.top, -10)
            .sheet(isPresented: $chooseCardSheet) {
                ChoosePaymentView(card: $viewModel.cards, selection: $viewModel.activeCard, dismiss: showDismiss)
            }
    }
    
}

extension QuoteConfirmView {
    var QuoteExtraView: some View {
        
        Group {
            Section(header: Text("Add Note to Driver")) {
                HStack{
                    
                    Image(viewModel.addNote != "" ? "Message" : "add")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 20, height: 20)
                    TextField("I will be waiting at...,", text: $viewModel.addNote)
                }
            }
            .listRowBackground(Color("Card"))
            
            
            Section(header: Text("Payment")) {
                Button(action: {
                    self.chooseCardSheet = true
                }) {
                    
                    HStack (spacing: 20){
                        Image(.card)
                            .renderingMode(.template)
                            .resizable()
                            .scaledToFit()
                            .foregroundStyle(Color.accentColor)
                            .frame(width: 18)
                        
                            if viewModel.activeCard != nil {
                                Text(viewModel.activeCard?.card_masked ?? "")
                                    .foregroundStyle(Color.black)
                                    .font(.system(size: 18))
                            }
                            else {
                                Text("Add new card")
                                    .foregroundStyle(Color.black)
                                    .font(.system(size: 18))
                            }
                        
                        Spacer()
                        Image(systemName: "chevron.right")
                            .foregroundColor(Color.gray)
                            .fontWeight(.bold)
                            .font(.system(size: 14))
                    }
                }
            }
            .clearListBackground()
            .listRowBackground(Color("Card"))
//                PromoCodeView
            .clearListBackground()
            .listRowBackground(Color("Card"))
        }
        .clearListBackground()
        .listRowBackground(Color("Card"))
    }
}


//
//extension QuoteConfirmView  {
//    var PaymentCardUI: some View {
//        Section (header: Text("Payment")){
//
//            if quoteData != nil && quoteData?.cardNumber != "" {
//                Button(action: {
//                }) {
//                    HStack {
//                        Image("card")
//                            .resizable()
//                            .scaledToFit()
//                            .frame(width: 25, height: 25)
//                        Text("\(quoteData?.cardNumber ?? "")")
//                    }
//                }
//                .onAppear() {
//                    viewModel.cardNumber = quoteData?.cardNumber ?? ""
//                }
//            }
//            else {
//                Button(action: {
//                    viewModel.navigateToCard = true
//                }) {
//                    HStack {
//                        Image("card")
//                            .resizable()
//                            .scaledToFit()
//                            .frame(width: 25, height: 25)
//                        Text(viewModel.cardNumber != "" ? viewModel.cardNumber : "Add payment Card")
//                    }
//                }
//            }
//
//        }
//        .listRowBackground(Color("Card"))
//
//    }
//
//    func fetchCard() {
//        viewModel.cardNumber = viewModel.cardVal
//    }
//}


extension QuoteConfirmView {
    var PromoCodeView: some View {
        Group {
            if viewModel.discountApplied {
                HStack (spacing: 20){
                    HStack (spacing: 20) {
                        Text("Promo Code \(viewModel.promoCode.uppercased()) is applied.")
                            .font(.system(size: 17))
                            .foregroundStyle(Color.accentColor)
                    }
                    Spacer()
                    VStack {
                        Button(action: {
                            viewModel.discountApplied = false
                            viewModel.promoCode = ""
                        }) {Text("Remove")
                        }
                        .padding(.vertical, 2)
                        .padding(.horizontal, 14)
                        .foregroundColor(Color.white)
                        .background(Color.red)
                        .clipShape(RoundedRectangle(cornerRadius: 8))
                    }
                }
                
            } else {
                Section(header: Text("Promo Code")) {
                    HStack (spacing: 20){
                        HStack (spacing: 20) {
                            TextField("Enter promo code", text: $viewModel.promoCode, axis: .horizontal)
                                .font(.system(size: 17))
                                .keyboardType(.numbersAndPunctuation)
                        }
                        Spacer()
                        VStack {
                            Button(action: {
                                viewModel.checkDiscount(quoteId: "\(quoteData?.quote?.id ?? 0)", fleet: fleetData)
                            }) {Text("Apply")
                            }
                            .padding(.vertical, 2)
                            .padding(.horizontal, 14)
                            .foregroundColor(Color("ThemeColor"))
                            .background(Color.accentColor)
                            .clipShape(RoundedRectangle(cornerRadius: 8))
                        }
                    }
                }
                .padding(.vertical, 8)
                .padding(.horizontal)
                .listRowBackground(Color("Card"))
            }
        }
        .listRowBackground(Color.clear)
        .listStyle(PlainListStyle())
        .listRowInsets(EdgeInsets())
        .clearListBackground()
    }
}
