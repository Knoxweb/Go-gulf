//
//  CountryRowView.swift
//  Aelfo
//
//  Created by Prabin Phasikawo on 06/11/2023.
//

import SwiftUI

struct CountryRowView: View {
    let country: CPData
    let isSelected: Bool = false
    
    var body: some View {
        HStack {
            Text(country.flag)
            Text(country.name)
                .font(.headline)
            Spacer()
            Text(country.dial_code)
                .foregroundColor(.secondary)
        }
    }
}
#Preview {
    CountryRowView(country: CPData(id: "1", name: "USA", flag: "us", code: "11", dial_code: "+01", pattern: "112", limit: 9))
}
