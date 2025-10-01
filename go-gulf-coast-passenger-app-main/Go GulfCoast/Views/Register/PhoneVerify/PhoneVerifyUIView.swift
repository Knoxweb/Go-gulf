//
//  PhoneVerifyUIView.swift
//  SwiftProject
//
//  Created by Mac on 12/11/21.
//

import SwiftUI
//import iPhoneNumberField

struct PhoneVerifyUIView: View {
    @StateObject var phoneVerifyVM = PhoneVerifyVM()
    @EnvironmentObject var appRootManager: AppRootManager
    @EnvironmentObject var router: Router
    
    @State var isEditing: Bool = false
    
    @State var presentSheet = false
    @State var countryCode: String = "+1"
    @State var countryFlag: String = "🇺🇸"
    @State var countryPattern: String = "### ### ####"
    @State var countryLimit: Int = 17
    @State var mobPhoneNumber = ""
    @State var searchCountry: String = ""
    @Environment(\.colorScheme) var colorScheme
    @FocusState var keyIsFocused: Bool
    @State var userCurrentCountry: CPData? = nil
    @FocusState var focusedField: Field?
    let countries: [CPData] = Bundle.main.decode("CountryNumbers.json")
    @State var selectedCountry: CPData? = nil {
        didSet {
            if let selectedCountry = selectedCountry {
                countryCode = selectedCountry.dial_code
                countryFlag = selectedCountry.flag
                countryPattern = selectedCountry.pattern
                countryLimit = selectedCountry.limit
            }
        }
    }
    
    enum Field: Int, Hashable {
        case phone
    }
    
    
    
    var body: some View {
        ZStack {
            ScrollView {
                VStack (alignment: .leading, spacing: 20){
                    Section (header: 
                        Text("Enter number to receive OTP code")
                            .font(.system(size: 13))
                            .foregroundStyle(Color.gray)
                    ){
                        CountryTextView
                    }
                    Button(action: {
                        phoneVerifyVM.submitForm(country: self.userCurrentCountry, router: router)
                    }) {
                        Text("Login")
                            .fullWithButton()
                    }
                    
                    HStack {
                        VStack {
                            Divider()
                                .overlay(
                                    Color.gray.opacity(1)
                                )
                        }
                        Text("or continue with".uppercased())
                            .font(.system(size: 12))
                            .padding(.horizontal, 10)
                            .foregroundColor(Color.gray)
                            .frame(minWidth: 150)
                        VStack {
                            Divider()
                                .overlay(
                                    Color.gray.opacity(0.1)
                                )
                        }
                    }
                    .padding(.vertical, 30)
                    Button(action: {
                        router.navigateTo(.emailLoginScreen)
                    }) {
                        Text("Email")
                            .outlinedButton()
                    }
                }
                .padding()
            }
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            .navigationBarBackButtonHidden(phoneVerifyVM.loading)
            .navigationBarTitle(Text("Enter mobile number"), displayMode: .large)
            
            .onAppear{
                    DispatchQueue.main.asyncAfter(deadline: .now() + 0.15) {
                        self.focusedField = .phone
                    }
                UINavigationBar.customizeBackButton()
                if self.userCurrentCountry == nil {
                    let locale = Locale.current
                    if let selectedCountryCode = locale.region?.identifier {
                        if let selectedCountry = countries.first(where: { $0.code == selectedCountryCode }) {
                            self.userCurrentCountry = selectedCountry
                            self.countryFlag = selectedCountry.flag
                            self.countryCode = selectedCountry.dial_code
                            self.countryPattern = selectedCountry.pattern
                            self.countryLimit = selectedCountry.limit
                        }
                    }
                }
            }
            .sheet(isPresented: $presentSheet) {
                NavigationView {
                    List {
                        if let userCurrentCountry = userCurrentCountry {
                            Section(header: Text("Your Current Country")) {
                                if self.userCurrentCountry != nil {
                                    CountryRowView(country: userCurrentCountry)
                                }
                            }
                            .background(Color("FromField"))
                            .listRowBackground(Color("FromField"))
                        }
                        
                        Section(header: Text("All Countries")) {
                            ForEach(filteredResorts) { country in
                                Button(action: {
                                    self.userCurrentCountry = country
                                    self.countryFlag = country.flag
                                    self.countryCode = country.dial_code
                                    self.countryPattern = country.pattern
                                    self.countryLimit = country.limit
                                    presentSheet = false
                                    searchCountry = ""
                                }) {
                                    HStack {
                                        Text(country.flag)
                                        Text(country.name)
                                            .font(.headline)
                                        Spacer()
                                        Text(country.dial_code)
                                            .foregroundColor(.secondary)
                                    }
                                    .background(Color("FromField"))
                                    .foregroundStyle(.white.opacity(0.7))
                                }
                            }
                        }
                        .listRowBackground(Color("FromField"))
                    }
                    .listRowInsets(EdgeInsets())
                    .listRowBackground(Color.linearGradient)
                    .listStyle(.grouped)
                    .clearListBackground()
                    .searchable(text: $searchCountry, prompt: "Search Country")
                    .navigationBarTitle("Choose Country", displayMode: .automatic)
                    .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
                }
                .clearListBackground()
                .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
                .presentationDetents([.large, .large])
                .listRowBackground(Color.linearGradient)

            }
            .presentationDetents([.large, .large])
            .background(Color.linearGradient.edgesIgnoringSafeArea(.all))
            
            if phoneVerifyVM.loading {
                ActivityIndicator()
            }
        }
        .disabled(phoneVerifyVM.loading)
    }
    
    var filteredResorts: [CPData] {
        if searchCountry.isEmpty {
            return countries
        } else {
            return countries.filter { country in
                let searchText = searchCountry.lowercased()
                return country.name.lowercased().contains(searchText) ||
                country.code.lowercased().contains(searchText) ||
                country.dial_code.lowercased().contains(searchText)
            }
        }
    }
}

struct PhoneVerifyUIView_Previews: PreviewProvider {
    static var previews: some View {
        PhoneVerifyUIView()
    }
}
