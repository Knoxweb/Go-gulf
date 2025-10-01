//
//  AddShortcutsView.swift
//   GoGulf
//
//  Created by Mac on 6/9/22.
//

import SwiftUI
import Firebase

struct ShortCut: Identifiable, Hashable {
    let icon: String
    let title: String
    let id: Int
}
private var ShortCutList = [
    ShortCut(icon: "home", title: "Home", id: 1),
    ShortCut(icon: "office", title: "Office", id: 2),
    ShortCut(icon: "airport", title: "Airport", id: 3),
    ShortCut(icon: "bank", title: "Bank", id: 4),
    ShortCut(icon: "school", title: "School", id: 5),
    ShortCut(icon: "restaurant", title: "Restaurant", id: 6),
    ShortCut(icon: "hospital", title: "Hospital", id: 7),
    ShortCut(icon: "doctor", title: "Doctor", id: 8),
    ShortCut(icon: "store", title: "Store", id: 9),
    ShortCut(icon: "college", title: "College", id: 10),
    ShortCut(icon: "cinema", title: "Cinema", id: 11),
    ShortCut(icon: "meeting", title: "Meeting", id: 12),
    ShortCut(icon: "theatre", title: "Theatre", id: 13),
    ShortCut(icon: "wedding", title: "Wedding", id: 14),
    ShortCut(icon: "fitness", title: "Fitness", id: 15),
    ShortCut(icon: "gas st", title: "Gas St", id: 16),
    ShortCut(icon: "charge st", title: "Charge St", id: 17),
    ShortCut(icon: "mosque", title: "Mosque", id: 18),
    ShortCut(icon: "family", title: "Family", id: 19),
    ShortCut(icon: "pool", title: "Pool", id: 20),
]

struct AddShortcutsView: View {
    @StateObject var viewModel = ShortcutVM()
    @State var isActive = false
//    @State var id: Int?
    @State var addr: AddressModel?
    
    
    
    var columns: [GridItem] =
    Array(repeating: .init(.flexible()),
     count: UIDevice.modelName == "iPhone12,1" || UIDevice.modelName == "iPhone12,2" ||
     UIDevice.modelName == "iPhone12,4" || UIDevice.modelName == "iPhone12,3" || UIDevice.modelName == "iPhone11,2" || UIDevice.modelName == "iPhone11,4" || UIDevice.modelName == "iPhone11,8" || UIDevice.modelName == "Simulator iPhone12,1" || UIDevice.modelName == "iPhone X" || UIDevice.modelName == "Simulator iPhone X" ?  4 : 5)
    
    @Environment(\.presentationMode) var presentationMode: Binding<PresentationMode>
    let identity = UserDefaults.standard.string(forKey: "identity")
    
    var body: some View {
            ScrollView{
                VStack(alignment: .leading, spacing: 20){
                    Text("Address")
                        .fontWeight(.bold)
                    
                    VStack (alignment: .leading){
                        Button(action: {
                            viewModel.openPicker = true
                        }){
                            HStack{
                                Text("\(viewModel.address)")
                                    .lineLimit(1)
                                    .foregroundColor(.gray)
                                Spacer()
                            }
                            .padding(.leading)
                            .frame(maxWidth: .infinity)
                        }
                        .frame(height: 45)
                        .background(RoundedRectangle(cornerRadius: 10).fill(Color("FormField")))
                    }
                    
                    VStack (alignment: .leading){
                        Text("Title")
                            .fontWeight(.bold)
                        TextField("", text: $viewModel.title)
                            .frame(height: 45)
                            .padding(.horizontal)
                            .disabled(true)
                            .background(RoundedRectangle(cornerRadius: 10).fill(Color("FormField")))
                            .foregroundColor(.black)
                    }
                }
                VStack {
                    LazyVGrid(columns: columns) {
                        ForEach(ShortCutList, id: \.self) { item in
                            Button(action: {
                                viewModel.id = item.id
                                viewModel.title = item.title
                                viewModel.icon = item.icon
                            }){
                                ShortcutItem(icon: "\(item.icon)", title: "\(item.title)", isActive: viewModel.title.capitalized == item.title ? true : false)
                                    .cornerRadius(10)
                                    .padding(.vertical, 5)
                                
                            }
                            .cornerRadius(8)
                        }
                        
                    }
                }
                .padding(.vertical)
                Spacer()
            }
//            .sheet(isPresented: $viewModel.openPicker) {
//                AddressPicker(address: $viewModel.address, lat: $viewModel.lat, lng: $viewModel.lng)
//            }
            .fullScreenCover(isPresented: $viewModel.openPicker) {
                NavigationView {
                    SearchView(type: .constant("Address"), lat: $viewModel.lat, lng: $viewModel.lng, address: $viewModel.address)
                }
            }
            .onAppear(){
                if addr != nil {
                    viewModel.id = addr?.id ?? 0
                    viewModel.address = addr?.address ?? ""
                    viewModel.lat = addr?.lat ?? 0
                    viewModel.lng = addr?.lng ?? 0
                    viewModel.title = addr?.name ?? ""
                }
            }
            .padding()
            .navigationBarBackButtonHidden(true)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .navigationBarTitle("ShortCuts", displayMode: .inline)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .modifier(LoadingView(isPresented: $viewModel.loading))
        
            .modifier(
                AlertView(isPresented: $viewModel.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, secondaryAction: {
                    viewModel.showAlert.toggle()
                })
            )
            
            .modifier(
                AlertView(isPresented: $viewModel.showSuccess, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $viewModel.alertTitle, desc: $viewModel.alertMessage, primaryAction: {
                    self.presentationMode.wrappedValue.dismiss()
                })
            )
        
            .onReceive(viewModel.viewDismissalModePublisher) { shouldDismiss in
                if shouldDismiss {
                    self.presentationMode.wrappedValue.dismiss()
                }
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(action: {
                        self.presentationMode.wrappedValue.dismiss()
                    }) {
                        Text("Cancel")
                            .foregroundColor(.black)
                    }
                }
                ToolbarItem(placement: .confirmationAction) {
                    if addr != nil {
                        Button(action: {
                            viewModel.updateShortcut(id: addr?.id)
                        }) {
                            Text("Update")
                                .foregroundColor(Color.accentColor)
                        }
                    }
                    else {
                        Button(action: {
                            viewModel.addShortcut()
                        }) {
                            Text("Add")
                                .foregroundColor(Color.accentColor)
                        }
                    }
                }
            }
    }
}

struct AddShortcutsView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            AddShortcutsView()
        }
    }
}
