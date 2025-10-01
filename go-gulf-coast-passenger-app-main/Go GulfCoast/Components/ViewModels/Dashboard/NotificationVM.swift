//
//  NotificationVM.swift
//  CTP Limo
//
//  Created by Office on 23/05/2023.
//

import Foundation
import FirebaseFirestore

class NotificationVM: BaseObservableObject {
    @Published var notificationList: [NotificationListModel] = []
    @Published var todayList = [NotificationListModel]()
    @Published var otherList = [NotificationListModel]()
    private var notiListener: ListenerRegistration?
    @Published var booking: CurrentBookingModel?
    @Published var showRatingSheet = false
    @Published var review = ""

    @Published var seenList = [NotificationListModel]()
    @Published var unseenList = [NotificationListModel]()
    @Published var defaultRes: DefaultResponse?
    @Published var dressCode = 0
    @Published var kindness = 0
    @Published var vehicle = 0
    @Published var driving = 0
    @Published var global = 0
    
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
            }
            .store(in: &cancellables)
    }
    
    
    @MainActor
    private func getNotifications() {
        self.loading = true
        if let uid = UserDefaults.standard.string(forKey: "UID") {
            let documentReference = db.collection("passengers").document(uid).collection("notifications")
            notiListener = documentReference.addSnapshotListener { [weak self] querySnapshot, error in
                guard let self = self else { return }
                self.notificationList = []
                self.todayList = []
                self.otherList = []
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
                            self.seenList = self.notificationList.filter{ $0.is_seen == true}
                            self.unseenList = self.notificationList.filter{ $0.is_seen == false }
                            for ride in self.notificationList {
                                    if let pickupTimestamp = ride.timestamp {
                                        let pickupDate = Date(timeIntervalSince1970: TimeInterval(pickupTimestamp))
                                        let calendar = Calendar.current
                                        if calendar.isDateInToday(pickupDate) {
                                            self.todayList.append(ride)
                                        } else {
                                            self.otherList.append(ride)
                                        }
                                    }
                                }
                        }
                    } catch {
                        print("Error decoding documents: \(error)")
                    }
                }
            }
        }
    }
}
