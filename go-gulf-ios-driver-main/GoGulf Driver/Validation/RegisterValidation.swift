//
//  RegisterValidation.swift
//  Slyyk
//
//  Created by Prabin Phasikawo on 4/26/22.
//

import Foundation

struct validationResult {
    var success: Bool = false
    var errorMessage: String?
}

struct RegisterValidation {
    // validate the user inputs
    func validateUserInputs (title: String, body: String, userId: Int) -> validationResult {
        if(title.isEmpty || body.isEmpty){
            return validationResult (
                success: false,
                errorMessage: "User email and password cannot be empty")
        }
//        if(isValidEmail(value: email) == false){
//            return validationResult (success: false, errorMessage: "User email format is incorrect")
//        }
        return validationResult (success: true, errorMessage: nil)
    }
    
    private func isValidEmail(value: String) -> Bool {
    let emailFormat = "(?:[\\p{L}0-9!#$%\\&'*+/=?\\^_`{|}~-]+(?:\\.[\\p{L}0-9!#$%\\&'*+/=?\\^_`{|}" + "~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\" + "x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[\\p{L}0-9](?:[a-" + "z0-9-]*[\\p{L}0-9])?\\.)+[\\p{L}0-9](?:[\\p{L}0-9-]*[\\p{L}0-9])?|\\[(?:(?:25[0-5" + "]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-" + "9][0-9]?|[\\p{L}0-9-]*[\\p{L}0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21" + "-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"
    //let emailFormat = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
    let emailPredicate = NSPredicate(format:"SELF MATCHES %@", emailFormat)
    return emailPredicate.evaluate(with: value)
    }
}
