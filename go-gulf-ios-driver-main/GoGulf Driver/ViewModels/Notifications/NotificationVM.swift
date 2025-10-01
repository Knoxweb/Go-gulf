//
//  NotificationVM.swift
//  SlyykDriver
//
//  Created by Prabin Phasikawo on 25/08/2023.
//

import Foundation
import SwiftUI
import Firebase

class NotificationVM: BaseObservableObject {
    @Published var notificationList: [NotificationListModel] = []
    @Published var todayList = [NotificationListModel]()
    @Published var otherList = [NotificationListModel]()
    
    private var notiListener: ListenerRegistration?
    @Published var bookings: [CurrentBookingModel]?
    @Published var booking: CurrentBookingModel?
    @Published var offer: CurrentBookingModel?
    @Published var showSheet = false
    @Published var shoAlert = false
    @Published var showOfferAlert = false
    @Published var defaultRes: DefaultResponse?
    @Published var seenList = [NotificationListModel]()
    @Published var unseenList = [NotificationListModel]()
    
    @State var companyTitle = ""
    @State var companyMessage = ""
    
    internal func ObserveNotification() {
        Task(priority: .medium) {
            await getNotifications()
        }
    }
    
    public func stopListener() {
        self.notiListener?.remove()
        self.notiListener = nil
    }
    
    
    public func markAsRead(for notificationId: String?) {
        self.loading = true
        print(notificationId as Any, "ididdididididid")
        NetworkManager.shared.POST(to: APIEndpoints.markAsSeen(notificationId ?? ""), body: "")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    print(self.defaultRes?.message ?? "")
                    UIApplication.shared.applicationIconBadgeNumber = (self.unseenList.count) - 1
                case .failure(let error):
                    print(error.localizedDescription, "errrorrrrrr")
                }
            } receiveValue: { (response: DefaultResponse) in
                print(response, "responseresponseresponseresponseresponseresponse")
            }
            .store(in: &cancellables)
    }
    
    
    @MainActor
    private func getNotifications() {
        self.loading = true
        if let uid = UserDefaults.standard.string(forKey: "UID") {
            let documentReference = db.collection("drivers").document(uid).collection("notifications")
            notiListener = documentReference.addSnapshotListener { [weak self] querySnapshot, error in
                guard let self = self else { return }
                self.notificationList = []
                if let error = error {
                    print("Error fetching documents: \(error)")
                } else {
                    do {
                        self.loading = false
                        var notis = [NotificationListModel]()
                        for document in querySnapshot!.documents {
                            let documentData = document.data()
                            let jsonData = try JSONSerialization.data(withJSONObject: documentData)
                            let noti = try JSONDecoder().decode(NotificationListModel.self, from: jsonData)
                            notis.append(noti)
                        }
                        DispatchQueue.main.async {
                            self.notificationList = notis.sorted(by: { $0.timestamp ?? 0 > $1.timestamp ?? 0 })
                        }
                    } catch {
                        print("Error decoding documents: \(error)")
                    }
                }
            }
        }
    }
}


struct AcceptRejectRequest: Codable {
    let status: String?
}
