//
//  BookingsListVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 13/07/2022.
//

import Foundation
import UIKit
import Firebase

class BookingsListVM: BaseObservableObject {
    @Published var completedData: [CurrentBookingModel]?
    @Published var cancelledData: [CurrentBookingModel]?
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    private var listener: ListenerRegistration?
    @Published var jobType = ["completed", "cancelled"]
    @Published var selection = "completed"
    
    public func initialize() {
        Task(priority: .medium) {
            await getBookingList()
        }
    }
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    @MainActor
    public func getBookingList() {
        let profileDocument = parentCollection.collection("bookings")
        self.loading = true
        listener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.loading = false
                guard let self = self else { return }
                self.completedData = []
                self.cancelledData = []
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(profileDocument.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var bookings: [CurrentBookingModel] = []
//                    var jobs: [CurrentBookingModel] = []
                    let dateFormatter = DateFormatter()
                    dateFormatter.dateFormat = "dd MMM yyyy, hh:mm a"
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let booking = try decoder.decode(CurrentBookingModel.self, from: jsonData)
                            bookings.append(booking)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                    print(bookings, "bookingnngngnn")
                    self.completedData = bookings
                        .filter { $0.status == "completed" }
                        .sorted {
                            guard let date1 = dateFormatter.date(from: $0.pickup_date_time ?? ""),
                                  let date2 = dateFormatter.date(from: $1.pickup_date_time ?? "") else { return false }
                            return date1 > date2
                        }

                    self.cancelledData = bookings
                        .filter { $0.status == "cancelled" }
                        .sorted {
                            guard let date1 = dateFormatter.date(from: $0.pickup_date_time ?? ""),
                                  let date2 = dateFormatter.date(from: $1.pickup_date_time ?? "") else { return false }
                            return date1 > date2
                        }
                } else {
                    print("No bookings found.")
                }
            }
        
    }

}

