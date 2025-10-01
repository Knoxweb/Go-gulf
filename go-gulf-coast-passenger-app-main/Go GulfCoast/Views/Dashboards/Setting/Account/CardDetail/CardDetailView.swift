//
//  CardDetailView.swift
//  SwiftProject
//
//  Created by Mac on 12/16/21.
//

import SwiftUI
import Kingfisher

struct CardDetailView: View {
    @StateObject var VM: AccountVM = AccountVM()
    @State var showcard = false
    @EnvironmentObject var router: Router
    
    var body: some View {
                ScrollView {
                    VStack {
                        if VM.cardData?.count ?? 0 > 0 {
                            ActiveCardSingleView(cardMasked: VM.activeCard?.card_masked ?? "", CVC: "***", cardExpiryMonth: "\(VM.activeCard?.exp_month ?? 0)", cardExpiryYear: "\(VM.activeCard?.exp_year ?? 0)")
                            HStack {
                                Text("Card")
                                    .font(.title3)
                                    .fontWeight(.medium)
                                Spacer()
                            }
                            
                            ForEach(Array((VM.cardData?.enumerated())!), id: \.offset) { index, element in
                                CardListItemView(element: element, VM: VM)
                            }
                        }
                        else {
                            Text("No card added")
                                .cardStyleModifier()
                        }
                    }
                    .padding()
                    .navigationBarTitle("My Cards", displayMode: .inline)
                    
                    .toolbar {
                        ToolbarItem(placement: .navigationBarTrailing) {
                            Button (action : {
                                router.navigateTo(.addCardScreen)
                            }) {
                                Text("Add Card")
                            }
                        }
                    }
                }
                .toolbarRole(.editor)
                .onAppear(){
                    VM.getCards()
                }
                .modifier(LoadingView(isPresented: $VM.loading))
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
    }
}


struct CardListItemView: View{
    var element: CardModel
    @ObservedObject var VM: AccountVM
    @State var showCardSheet = false
    @State var deleteCard = false
    @State var selectedCard: CardModel?
    @EnvironmentObject var router: Router
    @State var cardId = 0
    
    var body: some View{
        ZStack (alignment: .top){
            VStack {
                ZStack (alignment: .top){
                    HStack (alignment: .bottom){
                        VStack(alignment: .leading) {
                            Text(element.card_masked?.uppercased() ?? "")
                                .fontWeight(.bold)
                                .font(.system(size: 18))
                                .multilineTextAlignment(.leading)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            Text("\(element.exp_month ?? 4 <= 9 ? "0" : "")\(String(element.exp_month ?? 0)) / \(String(element.exp_year ?? 0))")
                                .font(.system(size: 16))
                                .opacity(0.7)
                            HStack {
                                BadgeView(text: .constant("\(element.is_active ?? false ? "Active" : "Inactive")"), bgColor: element.is_active ?? false ? .constant(Color.accentColor) : .constant(Color.red), foregroundColor: .constant(element.is_active ?? false ? Color(.theme) : Color.black))
                            }
                            .foregroundColor(Color("ThemeColor"))
                            .background(element.is_active ?? false ? Color.accentColor : Color.red)
                            .cornerRadius(6.0)
                            .padding(.top, 3)
                            
                        }
                        Spacer()
                        VStack(alignment: .trailing) {
                            
                            KFImage(URL(string: element.brand_image ?? ""))
                                .placeholder {
                                    Image(.default)
                                        .resizable()
                                        .scaledToFit()
                                        .frame(width: 50, height: 25)
                                        .background(.gray)
                                        .opacity(0)
                                }
                                .resizable()
                                .scaledToFit()
                                .frame(width: 50, height: 25)
                                .background(.gray)
                                .opacity(0)
                        }
                    }
                }
            }
            .padding()
            .frame(maxWidth: .infinity)
            .background(Color("Card"))
            .cornerRadius(6.0)
            
            .onTapGesture {
                self.selectedCard = element
                if self.selectedCard != nil {
                    router.navigateTo(.editCardScreen(cardData: self.selectedCard))
                }
            }
            
            if !(element.is_active ?? false){
                HStack {
                    Spacer()
                    Button(action: {
                        self.deleteCard = true
                        self.cardId = element.id ?? 0
                    }) {
                        HStack {
                            Image(systemName: "trash.circle.fill")
                                .foregroundStyle(Color.red)
                                .padding(8)
                                .font(.system(size: 38))
                                .offset(x: 20, y: -20)
                        }
                    }
                }
                .zIndex(99)
            }
        }
        
        .alert("Delete Card?", isPresented: $deleteCard) {
                   Button("Delete", role: .destructive) {
                       VM.deleteCard(cardId: self.cardId)
                   }
                   Button("Cancel", role: .cancel) {}
               } message: {
                   Text("Are you sure you want to delete this card?")
               }
    }
    
    func getCards() {
        VM.getCards()
    }
}


struct ActiveCardSingleView: View{
    var cardMasked: String
    var CVC: String
    var cardExpiryMonth: String
    var cardExpiryYear: String
    
    var body: some View{
        VStack {
            Text(cardMasked)
                .fontWeight(.bold)
                .font(.title2)
                .foregroundColor(Color.accentColor)
            
            HStack() {
                VStack(alignment: .leading) {
                    Text("CVC".uppercased())
                        .font(.caption)
                        .opacity(0.7)
                    Text(CVC)
                        .fontWeight(.bold)
                        .font(.subheadline)
                }
                Spacer()
                VStack(alignment: .trailing) {
                    Text("Card Expiry".uppercased())
                        .font(.caption)
                        .opacity(0.7)
                    Text("\(cardExpiryMonth) / \(cardExpiryYear)")
                        .fontWeight(.bold)
                        .font(.subheadline)
                }
                .padding(.all, 12.0)
                .cornerRadius(6.0)
            }
        }
        .padding()
        .frame(maxWidth: .infinity)
        .background(Color("Card"))
        .cornerRadius(6.0)
    }
}

struct CardDetailView_Previews: PreviewProvider {
    static var previews: some View {
        CardDetailView()
    }
}
