//
//  BookingDetailVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 14/07/2022.
//

import Foundation
import UIKit
import Firebase

class BookingsDetailVM: BaseObservableObject {
    @Published var bookingData:  CurrentBookingModel?
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    private var listener: ListenerRegistration?
    
    
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    @MainActor
    public func getBookingDetail(bookingId: String?, type: String?) {
        guard let bookingId = bookingId, !bookingId.isEmpty else {
            print("Invalid bookingId")
            return
        }

        self.loading = true
        let profileDocument = parentCollection.collection(type == "history" ? "bookings" : "schedules").whereField("id", isEqualTo: Int(bookingId) ?? 0)
        
        listener = profileDocument.addSnapshotListener { [weak self] (querySnapshot, error) in
            self?.loading = false
            guard let self = self else { return }
            self.bookingData = nil
            
            if let error = error {
                print("Error getting documents: \(error)")
                return
            }
            
            guard let querySnapshot = querySnapshot else {
                print("QuerySnapshot is nil.")
                return
            }
            
            print("Number of documents found:", querySnapshot.documents.count)
            if !querySnapshot.isEmpty {
                var notices: [CurrentBookingModel] = []
                for document in querySnapshot.documents {
                    print("Document Data:", document.data())
                    do {
                        let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                        let decoder = JSONDecoder()
                        let notice = try decoder.decode(CurrentBookingModel.self, from: jsonData)
                        notices.append(notice)
                    } catch {
                        print("Error decoding document: \(error)")
                    }
                }
                self.bookingData = notices.first
            } else {
                print("No completed or cancelled documents found.")
            }
        }
    }


}
