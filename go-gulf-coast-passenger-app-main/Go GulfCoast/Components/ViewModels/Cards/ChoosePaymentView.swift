//
//  ChoosePaymentView.swift
//  GoGulf
//
//  Created by Prabin Phasikawo on 07/09/2024.
//
import SwiftUI

struct ChoosePaymentView: View {
    @Environment(\.presentationMode) var presentationMode
    @StateObject var VM: PaymentVM = PaymentVM()
    @Binding var card: [PaymentModel]?
    @Binding var selection: PaymentModel?
    @State var dismiss: Bool?
    @State var showCard = false
    
    var body: some View {
        
        VStack {
            VStack {
                ScrollView {
                    VStack {
                        VStack (spacing: 15){
                            if VM.cards?.count ?? 0 > 0 {
                                ForEach(Array((VM.cards?.enumerated())!), id: \.offset) { index, element in
                                    Button(action: {
                                        withAnimation {
                                            selection = element
                                            self.presentationMode.wrappedValue.dismiss()
                                        }
                                    }) {
                                        HStack (alignment: .center, spacing: 20){
                                            Image(.card)
                                                .renderingMode(.template)
                                                .resizable()
                                                .foregroundStyle(Color.black)
                                                .scaledToFit()
                                                .frame(width: 16, height: 16)
                                            
                                            VStack (alignment: .leading, spacing: 5){
                                                Group {
                                                    Text(element.name )
                                                }
                                                .font(.system(size: 18))
                                                .foregroundStyle(Color.black)
                                                .frame(maxWidth: .infinity, alignment: .leading)
                                                .multilineTextAlignment(.leading)
                                                
                                                Text("\(element.card_masked ?? "")")
                                                    .foregroundStyle(.gray)
                                                    .font(.system(size: 14))
                                                    .frame(maxWidth: .infinity, alignment: .leading)
                                                    .multilineTextAlignment(.leading)
                                            }
                                            Spacer()
                                            Image(systemName: selection?.id == element.id ? "circle.fill" : "circle")
                                                .renderingMode(.template)
                                                .resizable()
                                                .scaledToFit()
                                                .foregroundStyle(selection?.id == element.id ? Color.accentColor : Color.gray )
                                                .frame(width: 24, height: 24)
                                        }
                                    }
                                    Divider()
                                        .frame(height: 1.08)
                                        .overlay(.gray.opacity(0.1))
                                }
                            }
                            VStack {
                                Button(action: {
                                    showCard = true
                                }) {
                                    HStack (alignment: .center, spacing: 20){
                                        Image(systemName: "plus.circle")
                                            .renderingMode(.template)
                                            .resizable()
                                            .foregroundStyle(Color.black)
                                            .scaledToFit()
                                            .frame(width: 16, height: 16)
                                        
                                        VStack (alignment: .leading, spacing: 5){
                                            Group {
                                                Text("Add Card")
                                            }
                                            .font(.system(size: 18))
                                            .foregroundStyle(Color.black)
                                            .frame(maxWidth: .infinity, alignment: .leading)
                                            .multilineTextAlignment(.leading)
                                        }
                                        Spacer()
                                        Image(systemName: "circle")
                                            .renderingMode(.template)
                                            .resizable()
                                            .scaledToFit()
                                            .foregroundStyle(Color.gray )
                                            .frame(width: 24, height: 24)
                                    }
                                }
                            }
                        }
                    }
                    .padding()
                }
            }
            .padding(.top)
        }
        .onAppear() {
            VM.getCard()
        }
        .frame(maxWidth: .infinity)
        .frame(maxHeight: .infinity)
        .background(Color.linearGradient, ignoresSafeAreaEdges: .all)
        .presentationContentInteraction(.resizes)
        .presentationDragIndicator(.visible)
        .presentationCompactAdaptation(.sheet)
        .presentationDetents([.height(250), .large])
        .sheet(isPresented: $showCard) {
            AddCardView()
        }
        
    }
}
