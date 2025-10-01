//
//  CountryPickerUI.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 05/09/2024.
//

import SwiftUI
import CoreLocation
import Combine

struct CountryPickerView: View {
    @State var presentSheet = false
    @State var countryCode: String = "+33"
    @State var countryFlag: String = "🇫🇷"
    @State var countryPattern: String = "# ## ## ## ##"
    @State var countryLimit: Int = 17
    @State var mobPhoneNumber = ""
    @State private var searchCountry: String = ""
    @State var title: LocalizedStringKey?
    @FocusState public var keyIsFocused: Bool
    @Binding var currentCountry: CPData?
    @State var setFocus: Bool?
    @Binding var phoneNumber: String
    @FocusState private var isFocus: Bool
    var locationManager = LocationTracker.shared.locationManager
    @EnvironmentObject var globalState: GlobalState
    
    var userLatitude: Double {
        return (locationManager.location?.coordinate.latitude) ?? 0
    }
    
    var userLongitude: Double {
        return (locationManager.location?.coordinate.longitude) ?? 0
    }
    
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
    var body: some View {
        VStack (alignment: .leading, spacing: 5){
            if let text = title {
                Text(text)
                    .font(.system(size: 13))
                    .foregroundStyle(Color.gray)
            }
            else {
                Text("Enter Number")
                    .font(.system(size: 13))
                    .foregroundStyle(Color.gray)
            }
            VStack (alignment: .leading, spacing: 5){
                HStack {
                    Button(action: {
                        presentSheet = true
                        keyIsFocused = false
                    }) {
                        HStack {
                            Text("\(countryFlag)")
                                .font(.system(size: 30))
                            Image(systemName: "chevron.down")
                                .font(.system(size: 10))
                            Text("\(countryCode)")
                        }
                        .padding(10)
                        .frame(minWidth: 100)
                        .frame(height: 50)
                        .foregroundColor(Color.black)
                        .font(.system(size: 16))
                    }
                    TextField("06 01 23 45 67", text: $mobPhoneNumber)
                        .focused($keyIsFocused)
                        .frame(height: 50)
                        .frame(maxWidth: .infinity)
                        .keyboardType(.numberPad)
                        .onChange(of: currentCountry?.code) { item in
                            if let newValue = item {
                                if currentCountry?.code == "FR"{
                                    mobPhoneNumber = PhoneNumberFormatter.format(mobPhoneNumber)
                                }
                                else {
                                    let filtered = mobPhoneNumber.filter { $0.isNumber }
                                    if filtered.count > 12 {
                                        mobPhoneNumber = String(filtered.prefix(12))
                                    } else {
                                        mobPhoneNumber = filtered
                                    }
                                    applyPatternOnNumbers(&mobPhoneNumber, pattern: countryPattern, replacementCharacter: "#")
                                }
                            }
                        }
                        .onReceive(Just(mobPhoneNumber)) { newValue in
                            if currentCountry?.code == "FR"{
                                mobPhoneNumber = PhoneNumberFormatter.format(newValue)
                            }
                            else {
                                let filtered = newValue.filter { $0.isNumber }
                                if filtered.count > 12 {
                                    mobPhoneNumber = String(filtered.prefix(12))
                                } else {
                                    mobPhoneNumber = filtered
                                }
                                applyPatternOnNumbers(&mobPhoneNumber, pattern: countryPattern, replacementCharacter: "#")
                            }
                        }
                        .foregroundColor(Color.black)
                        .onChange(of: mobPhoneNumber) { item in
                            phoneNumber = mobPhoneNumber
                        }
                        .font(.system(size: 16))
                        .padding(.trailing)
                        .foregroundColor(Color.black)
                }
                .background(Color("FormField").cornerRadius(8))
                .overlay(RoundedRectangle(cornerRadius: 6).stroke(Color.gray.opacity(0.2), lineWidth: 1))
                .focused($isFocus)
            }
        }
        .onChange(of: globalState.currentNumber) { _ in
            DispatchQueue.main.async {
                mobPhoneNumber = globalState.currentNumber ?? phoneNumber
                print(globalState.currentNumber as Any, "globalState")
            }
        }
        .onChange(of: globalState.currentCountry) { _ in
            if let current = globalState.currentCountry {
                countryCode = current.dial_code
                countryFlag = current.flag
                countryPattern = current.pattern
                countryLimit = current.limit
            }
            currentCountry = globalState.currentCountry
        }
        
        .onAppear(){
            DispatchQueue.main.async {
                mobPhoneNumber = phoneNumber
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 0.25) {
                if setFocus ?? true {
                    self.isFocus = true
                }
            }
            if self.currentCountry != nil {
                if let current = self.currentCountry {
                    countryCode = current.dial_code
                    countryFlag = current.flag
                    countryPattern = current.pattern
                    countryLimit = current.limit
                }
            }
            else {
                let locale = Locale.current
                if let selectedCountryCode = locale.region?.identifier {
                    if let selectedCountry = countries.first(where: { $0.code == selectedCountryCode }) {
                        currentCountry = selectedCountry
                        globalState.currentCountry = selectedCountry
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
                    if let userCurrentCountry = currentCountry {
                        Section(header: Text("Your Current Country")) {
                            CountryRowView(country: userCurrentCountry)
                        }
                    }
                    
                    Section(header: Text("All Countries")) {
                        ForEach(filteredResorts) { country in
                            Button(action: {
                                currentCountry = country
                                globalState.currentCountry = country
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
                                        .foregroundColor(Color.gray)
                                }
                                .foregroundStyle(Color.black)
                            }
                        }
                    }
                }
                .listStyle(.grouped)
                .searchable(text: $searchCountry, prompt: "Search Country")
                .navigationBarTitle("Choose Country")
            }
            .presentationDetents([.large, .large])
        }
        .presentationDetents([.large, .large])
        
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
    
    
    func placeholder<Content: View>(
        when shouldShow: Bool,
        alignment: Alignment = .leading,
        @ViewBuilder placeholder: () -> Content) -> some View {
            
            ZStack(alignment: alignment) {
                placeholder().opacity(shouldShow ? 1 : 0)
                self
            }
        }
    
    func hideKeyboard() {
        let resign = #selector(UIResponder.resignFirstResponder)
        UIApplication.shared.sendAction(resign, to: nil, from: nil, for: nil)
    }
    
    
    func applyPatternOnNumbers(_ stringvar: inout String, pattern: String, replacementCharacter: Character) {
        var pureNumber = stringvar.replacingOccurrences( of: "[^0-9]", with: "", options: .regularExpression)
        for index in 0 ..< pattern.count {
            guard index < pureNumber.count else {
                stringvar = pureNumber
                return
            }
            let stringIndex = String.Index(utf16Offset: index, in: pattern)
            let patternCharacter = pattern[stringIndex]
            guard patternCharacter != replacementCharacter else { continue }
            pureNumber.insert(patternCharacter, at: stringIndex)
        }
        stringvar = pureNumber
    }
}

struct PhoneNumberFormatter {
    static func format(_ number: String) -> String {
        let cleanedNumber = number.filter { "0123456789".contains($0) }
        
        if cleanedNumber.hasPrefix("0") {
            return cleanedNumber.enumerated().map { $0 > 0 && $0 % 2 == 0 ? " \($1)" : "\($1)" }.joined()
        } else {
            let formattedNumber = cleanedNumber.enumerated().map { $0 > 0 && $0 % 2 == 1 ? " \($1)" : "\($1)" }.joined()
            return formattedNumber.hasPrefix(" ") ? String(formattedNumber.dropFirst()) : formattedNumber
        }
    }
}
