//
//  VehicleListView.swift

//
//  Created by Prabin Phasikawo on 1/17/22.
//

import SwiftUI
import Kingfisher

struct VehicleListedView: View {
    @StateObject var VM = vehicleListedVM()
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    
    var body: some View {
            ScrollView {
                VStack {
                    VStack {
                        if let fleet = VM.fleetData {
                            VStack(spacing: 20){
                                ForEach(Array(fleet.enumerated()), id: \.offset) { index, element in
//                                    if(element.displayStatus == true) {
                                        fleetCardListView(index: index, element: .constant(element), VM: VM)
//                                    }
                                }
                            }
                            .clearListBackground()
                        }
                    }
                }
                .padding()
            }
            .navigationBarTitle("Vehicles", displayMode: .inline)
//            .navigationBarBackButtonHidden(true)
            .frame(maxWidth: .infinity)
            .frame(maxHeight: .infinity)
            .toolbarRole(.editor)
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
//            .alert(VM.alertTitle ?? "", isPresented: $VM.showAlert) {
//                Button("OK", role: .cancel) {
//                }
//            } message: {
//                Text(VM.alertMessage ?? "")
//            }
        
            .modifier(
                AlertView(isPresented: $VM.showAlert, primaryButtonText: .constant("Ok"), secondaryButtonText: "Ok", title: $VM.alertTitle, desc: $VM.alertMessage, secondaryAction: {
                    VM.showAlert = false
                })
            )
        
            .onAppear() {
                VM.initialize()
                UINavigationBar.customizeBackButton()
            }
            .onDisappear() {
                VM.stopListener()
            }
            .modifier(LoadingView(isPresented: $VM.loading))
    }
}


struct fleetCardListView: View{
    var index: Int
    @Binding var element: FBFleetList
    @ObservedObject var VM: vehicleListedVM
    @State var active = false
    @State var defaultStatus = true
    var body: some View{
        Section{
                ZStack (alignment: .trailing){
                    VStack{
                        HStack{
                            NavigationLink(destination: AddEditVehicle(fleetData: element)) {
                                VStack(alignment: .leading, spacing: 5){
                                    VStack (alignment: .leading){
//                                        BadgeView(text: .constant(element.class_name ?? ""))
                                        Text(element.class_name ?? "")
                                        Text(element.type_name ?? "")
                                    }
                                    .font(.system(size: 15))
                                    .foregroundColor(.gray)
                                    Text(element.vehicle_registration_number ?? "")
                                        .font(.system(size: 16))
                                        .fontWeight(.semibold)
                                        .opacity(0.7)
                                    
                                }
                                Spacer()   
                            }
                        }
                        Divider()
                        HStack{
                            if element.vehicle_registration_number != "" {
                                VStack {
//                                    if element.is_active ?? true{
//                                        HStack {
//                                            Toggle("", isOn: .constant(true))
//                                                .labelsHidden()
//                                                .tint(Color.accentColor)
//                                                .opacity(1)
//                                            Text("Active")
//                                                .foregroundColor(.accentColor)
//                                                .font(.system(size: 15))
//                                                .fontWeight(.semibold)
//                                        }
//                                    }
//                                    else {
                                        HStack {
                                            Toggle("", isOn: $active)
                                                .labelsHidden()
                                                .tint(Color.accentColor)
                                                .onChange(of: active) { item in
                                                    VM.updateFleetStatus(id: element.id ?? 0, status: active)
                                                }
                                            
                                            Text(active ? "Active" : "Inactive")
                                                .foregroundColor(.gray)
                                                .font(.system(size: 15))
                                                .fontWeight(.semibold)
                                                
                                        }
//                                    }
                                }
                            }
                            else {
                                NavigationLink(destination: AddEditVehicle(fleetData: element)) {
                                    HStack {
                                        Image(systemName: "plus.circle")
                                            .foregroundStyle(Color.accentColor)
                                            .font(.system(size: 24))
                                            
                                        Text("Add this vehicle")
                                    }
                                    .foregroundColor(.accentColor)
                                    .font(.system(size: 15))
                                }
                            }

                            Spacer()
                            NavigationLink(destination: AddEditVehicle(fleetData: element)) {
                                HStack {
                                    Text("See Details")
                                        .foregroundColor(.gray)
                                        .font(.system(size: 16))
                                    Image(systemName: "chevron.right")
                                        .foregroundColor(.gray)
                                        .font(.system(size: 18))
                                }
                            }
                        }
                    }
                    VStack {
                        if let fleetImage =  element.image_path  {
                            NavigationLink(destination: AddEditVehicle(fleetData: element)) {
                                KFImage(URL(string: fleetImage))
                                    .placeholder {
                                        Image("default")
                                            .resizable()
                                            .aspectRatio(contentMode: .fit)
                                            .frame(height: 70)
                                            .offset(x:50, y: -20)
                                            .opacity(0)
                                        
                                    }
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                                    .frame(height: 70)
                                    .offset(x: 50, y: -20)
                            }
                        }
                    }
                }
                .onAppear() {
                    active = element.is_active ?? false
                }
        }
        .cardStyleModifier()
    }
}

struct VehicleListedView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            VehicleListedView()
        }
    }
}
