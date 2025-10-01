//
//  DashboardVM.swift
// SlyykDriver
//
//  Created by Prabin Phasikawo on 6/19/22.
//

import Foundation
import UIKit
import FirebaseFirestore
import Firebase
import AudioToolbox
import AVFoundation

class DashboardVM: BaseObservableObject {
    @Published var showCardSheet = false
    @Published var isOnline = false
    @Published var navigate = false
    @Published var ShowBankAccountSheet = false
    private var listener: ListenerRegistration?
    private var noticesListener: ListenerRegistration?
    private var upcomingListener: ListenerRegistration?
    private var currentRideListener: ListenerRegistration?
    @Published var profileData: FBProfileData?
    @Published var profileImage: UIImage = UIImage()
    @Published var defaultRes: DefaultResponse?
    @Published var noticeData: [NoticesModel]?
    @Published var currentData: CurrentBookingModel?
    @Published var upcomingData: CurrentBookingModel?
    private var statementListener: ListenerRegistration?
    @Published var statements: [StatementModel]?
    @Published var weekStatement: StatementModel?
    
    let token = (UserDefaults.standard.string(forKey: "accessToken") ?? "");
    let identity = UserDefaults.standard.string(forKey: "identity")
    
    @Published var locationManager = LocationManager()
    var userLatitude: Double {
        return locationManager.lastLocation?.coordinate.latitude ?? 0
    }
    
    var userLongitude: Double {
        return locationManager.lastLocation?.coordinate.longitude ?? 0
    }
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    
    public func initialize() {
        Task(priority: .medium) {
            await getProfileData()
            await getNotices()
            await getCurrentBooking()
            await getStatements()
            await getUpcomingRide()
            print((UserDefaults.standard.string(forKey: "UID") ?? ""), "UIIDIDIDIDIDIDIDI")
        }
    }
    
    public func stopListner() {
        self.listener?.remove()
        self.listener = nil
        self.noticesListener?.remove()
        self.noticesListener = nil
        self.currentRideListener?.remove()
        self.currentRideListener = nil
        self.statementListener?.remove()
        self.statementListener = nil
        self.upcomingListener?.remove()
        self.upcomingListener = nil
    }
    
    internal func updatePosition() {
        let body = CoordinatesModel(lat: "\(userLatitude)", lng: "\(userLongitude)")
        NetworkManager.shared.POST(to: APIEndpoints.udpatePosition, body: body)
            .sink { completion in
                switch completion {
                case .finished:
                    print(self.defaultRes?.message ?? "")
                case .failure(let error):
                    print(error.localizedDescription)
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultRes = response
            }
            .store(in: &cancellables)
    }
    
    
    @MainActor
    private func getNotices() {
        let profileDocument = parentCollection.collection("notices")
        noticesListener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                guard let self = self else { return }
                self.noticeData = []
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("\(profileDocument.path), QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    var notices: [NoticesModel] = []
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let notice = try decoder.decode(NoticesModel.self, from: jsonData)
                            notices.append(notice)
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                    self.noticeData = notices
                } else {
                    print("No completed or cancelled documents found.")
                }
            }
    }
    
    
    @MainActor
    private func getUpcomingRide() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("upcoming_job")
        print(documentReference.path, "documentReference")
        
        upcomingListener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            self?.upcomingData = nil
            guard let self = self else { return }
            if let error = error {
                print("Error fetching profile document: \(error)")
                return
            }
            guard let document = documentSnapshot else {
                print("Profile document does not exist")
                return
            }
            do {
                guard document.exists else {
                    throw NSError(domain: "Firestore", code: -1, userInfo: [NSLocalizedDescriptionKey: "Document does not exist"])
                }
                let data = try JSONSerialization.data(withJSONObject: document.data() ?? [:], options: [])
                self.upcomingData = try JSONDecoder().decode(CurrentBookingModel.self, from: data)
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    
    
    @MainActor
    public func getStatements() {
        let documentReference = parentCollection.collection("statements")
        statementListener = documentReference.addSnapshotListener { [weak self] querySnapshot, error in
            guard let self = self else { return }
            self.statements = []
            if let error = error {
                print("Error fetching documents: \(error)")
            } else {
                do {
                    self.loading = false
                    var statements = [StatementModel]()
                    for document in querySnapshot!.documents {
                        let documentData = document.data()
                        let jsonData = try JSONSerialization.data(withJSONObject: documentData)
                        let statement = try JSONDecoder().decode(StatementModel.self, from: jsonData)
                        statements.append(statement)
                    }
                    DispatchQueue.main.async {
                        self.statements = statements.sorted(by: { $0.timestamp ?? 0 > $1.timestamp ?? 0 })
                        self.weekStatement = self.statements?.first
                    }
                } catch {
                    print("Error decoding documents: \(error)")
                }
            }
        }
    }
    
    @MainActor
    private func getCurrentBooking() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("current_job")
        print(documentReference.path, "documentReference")
        
        currentRideListener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            self?.currentData = nil
            guard let self = self else { return }
            if let error = error {
                print("Error fetching profile document: \(error)")
                return
            }
            guard let document = documentSnapshot else {
                print("Profile document does not exist")
                return
            }
            do {
                guard document.exists else {
                    throw NSError(domain: "Firestore", code: -1, userInfo: [NSLocalizedDescriptionKey: "Document does not exist"])
                }
                let data = try JSONSerialization.data(withJSONObject: document.data() ?? [:], options: [])
                self.currentData = try JSONDecoder().decode(CurrentBookingModel.self, from: data)
                
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    @MainActor
    public func getProfileData() {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("profile")
        print(documentReference.path, "documentReference")
        
        listener = documentReference.addSnapshotListener { [weak self] documentSnapshot, error in
            self?.loading = false
            guard let self = self else { return }
            if let error = error {
                print("Error fetching profile document: \(error)")
                return
            }
            guard let document = documentSnapshot else {
                print("Profile document does not exist")
                return
            }
            do {
                guard document.exists else {
                    throw NSError(domain: "Firestore", code: -1, userInfo: [NSLocalizedDescriptionKey: "Document does not exist"])
                }
                let data = try JSONSerialization.data(withJSONObject: document.data() ?? [:], options: [])
                self.profileData = try JSONDecoder().decode(FBProfileData.self, from: data)
                
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
    }
    
    
    public func updateProfilePic() {
        let body = (key: "profile_image", image: profileImage)
        self.loading = true
        let param = ["name": "\(self.profileData?.name ?? "")", "email": "\(self.profileData?.email ?? "")"]
        
        NetworkManager.shared.UPLOAD(
            to: APIEndpoints.update,
            images: nil,
            singleImageWithPrefix: body,
            params: param
        )
        .sink { completion in
            self.loading = false
            print(completion, "")
            switch completion {
            case .finished:
                self.alertTitle = "\(self.defaultRes?.title ?? "")"
                self.alertMessage =  "\(self.defaultRes?.message ?? "")"
                self.showAlert.toggle()
                
            case .failure(let error):
                let err = NetworkConnection().handleNetworkError(error)
                self.alertTitle = "\(err.title ?? "")"
                self.alertMessage =  "\(err.message ?? "")"
                self.showAlert.toggle()
            }
        } receiveValue: { (response: DefaultResponse) in
            self.defaultRes = response
            print(response, "resfsdfdsfdsfsdfsdfsdf")
        }
        .store(in: &cancellables)
    }
    
    
}

class Debouncer {
    private let delay: TimeInterval
    private var timer: Timer?
    
    init(delay: TimeInterval) {
        self.delay = delay
    }
    
    func run(action: @escaping () -> Void) {
        timer?.invalidate()
        timer = Timer.scheduledTimer(withTimeInterval: delay, repeats: false) { _ in
            action()
        }
    }
}
