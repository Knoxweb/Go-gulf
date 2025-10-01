//
//  CardListVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 13/07/2022.
//

import Foundation
import UIKit
import SwiftUI

struct RemoveCardIDModel: Codable {
    let id: String
}

class CardListVM: BaseObservableObject {
    @Published var response: GetCardResponse?
    @Published var defaultRes: DefaultResponse?
    @Published var showCardSheet = false
    @Published var disableClose = false
    
    
    func getCards(){
        self.loading = true
        NetworkManager.shared.GET(from: "passenger/card")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.response, "response")
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    let alertController = GlobalAlertController(title: String(err.title ?? ""), message: String(err.message ?? ""), preferredStyle: .alert)
                    alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: {_ in
                        print("foo")
                    }))
                    alertController.presentGlobally(animated: true, completion: nil)
                }
            } receiveValue: { (response: GetCardResponse) in
                self.response = response;
                
            }
            .store(in: &cancellables)
    }
    
    func deleteCard(by id: Int){
        self.loading = true
        let json = RemoveCardIDModel(id: "\(id)")
        NetworkManager.shared.GET(from: "passenger/delete-card")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    self.getCards()
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    let alertController = GlobalAlertController(title: String(err.title ?? ""), message: String(err.message ?? ""), preferredStyle: .alert)
                    alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: {_ in
                        print("foo")
                    }))
                    alertController.presentGlobally(animated: true, completion: nil)
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response;
                
            }
            .store(in: &cancellables)
        
    }
    
}
