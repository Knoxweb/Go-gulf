//
//  AlertController.swift
//   GoGulf
//
//  Created by Mac on 6/23/22.
//

import Foundation
import UIKit

class GlobalAlertController: UIAlertController {
    
    var globalPresentationWindow: UIWindow?
    
    func presentGlobally(animated: Bool, completion: (() -> Void)?) {
        globalPresentationWindow = UIWindow(frame: UIScreen.main.bounds)
        
        //This is needed when using scenes.
        if let currentWindowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene {
            globalPresentationWindow?.windowScene = currentWindowScene
        }

        globalPresentationWindow?.rootViewController = UIViewController()
        globalPresentationWindow?.windowLevel = UIWindow.Level.alert + 1
        globalPresentationWindow?.backgroundColor = .clear
        globalPresentationWindow?.subviews.first?.subviews.first?.subviews.first?.backgroundColor = UIColor.green
       
        
        let titleFont = UIFont.systemFont(ofSize: 18, weight: .bold)
        let titleAttributes = [NSAttributedString.Key.font: titleFont]
        let attributedTitle = NSAttributedString(string: self.title ?? "", attributes: titleAttributes)
        self.setValue(attributedTitle, forKey: "attributedTitle")

        // Set the font size for the attributed message
        let messageFont = UIFont.systemFont(ofSize: 15)
        let messageAttributes = [NSAttributedString.Key.font: messageFont]
        let attributedMessage = NSAttributedString(string: self.message ?? "", attributes: messageAttributes)
        self.setValue(attributedMessage, forKey: "attributedMessage")
        

//      globalPresentationWindow?.subviews.first?.backgroundColor = UIColor.red
        globalPresentationWindow?.makeKeyAndVisible()
        globalPresentationWindow?.rootViewController?.present(self, animated: animated, completion: completion)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        globalPresentationWindow?.isHidden = true
        globalPresentationWindow = nil
    }

}
