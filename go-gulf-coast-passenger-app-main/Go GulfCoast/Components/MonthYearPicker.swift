//
//  MonthYearPicker.swift
//  GoGulfDriver
//
//  Created by Office on 18/12/2022.
//

import Foundation
import SwiftUI
import UIKit
import Combine

struct MonthYearPickerView: View{
    @Binding var month: Int
    @Binding var year: Int
    let currentMonth = Calendar.current.component(.month, from: Date())
    @State var monthIndex: Int = Calendar.current.component(.month, from: Date()) - 1
    @State var yearIndex: Int = 0
    let monthSymbols = Calendar.current.monthSymbols
    let years = Array(Date().year..<Date().year+10)
    
    var body: some View {
        GeometryReader{ geometry in
            HStack(spacing: 0) {
                Picker(selection: self.$monthIndex.onChange(self.monthChanged), label: Text("")) {
                    ForEach(0..<self.monthSymbols.count) { index in
                        Text(self.monthSymbols[index])
                    }
                }.frame(maxWidth: geometry.size.width / 2).clipped()
                    .pickerStyle(.wheel)
                
                Picker(selection: self.$yearIndex.onChange(self.yearChanged), label: Text("")) {
                    ForEach(0..<self.years.count) { index in
                        Text(String(self.years[index]))
                    }
                }.frame(maxWidth: geometry.size.width / 2).clipped()
                    .pickerStyle(.wheel)
            }
         
        }
    }
    
    func monthChanged(_ index: Int) {
        self.dateChanged(yearIndex, index)
    }
    func yearChanged(_ index: Int) {
        self.dateChanged(index, monthIndex)
    }
    
    func dateChanged(_ yearIndex: Int, _ monthIndex: Int) {
        self.monthIndex = currentMonth - 1
        if((years[yearIndex] <=  Date().year) && (monthIndex + 1 < currentMonth)){
            self.monthIndex = currentMonth - 1
            self.year = years[yearIndex]
            self.month = currentMonth
        }
        else{
            self.monthIndex = monthIndex
            self.yearIndex = yearIndex
            self.year = years[yearIndex]
            self.month = monthIndex + 1
        }
    }
}

extension Date {
    var year: Int { Calendar.current.component(.year, from: self) }
}

extension Binding {
    func onChange(_ completion: @escaping (Value) -> Void) -> Binding<Value> {
        .init(get:{ self.wrappedValue }, set:{ self.wrappedValue = $0; completion($0) })
    }
}
