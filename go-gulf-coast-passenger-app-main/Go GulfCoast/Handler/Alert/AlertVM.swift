//
//  AlertVM.swift
//   GoGulf
//
//  Created by Mac on 6/22/22.
//

import SwiftUI

class AlertVM: ObservableObject {
    @Published var showAlert = false
    @Published var alertDismiss = false
    @Published var statusCode: Int = 0
    @Published var title: String = ""
    @Published var message: String = ""
    
    func showAlertMessage(statusCode: Int, title: String, msg: String){
        if(statusCode != 0){
            self.showAlert = true
        }
    }
}
