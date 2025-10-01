//
//  RequestingRideVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 29/06/2022.
//

import Foundation
import Firebase
import UIKit
import Combine
import FirebaseDatabase

class requestingRideVM: BaseObservableObject {
    @Published var navigate = false
    
    @Published var myLoader = false
    @Published var bookingCancelled = false
    @Published var showCanceAlert = false
    
    //    private let debouncer = Debouncer(delay: 0.5)
    @Published var bookingId = ""
    @Published var defaultResponse: DefaultResponse?
    let objectWillChange = PassthroughSubject<Void, Never>()
    //    var backgroundTask: BackgroundTask?
    //    @Published var counter = 0
    //    @Published private var timer: AnyCancellable?
    private var listener: ListenerRegistration?
    let parentCollection = Firestore.firestore().collection("passengers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    @Published var counter = 0
    @Published var quoteData: QuoteResponseData?
    @Published public var remainingTime: Int = 0
    @Published public var timer: Timer? = nil
    @Published var showRetryAlert = false
    @Published var showSuccess = false
    
    public func initialize(tabRouter: TabRouter) {
        checkBookingrequest(tabRouter: tabRouter)
        
    }
    
    
    public func stopListener() {
        self.listener?.remove()
        self.listener = nil
    }
    
    private func checkBookingrequest(tabRouter: TabRouter) {
        self.loading = true
        let documentReference = parentCollection.collection("data").document("request")
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
                self.quoteData = try JSONDecoder().decode(QuoteResponseData.self, from: data)
                self.updateRemainingTime()
                switch self.quoteData?.status {
                case "current":
                    print("currebnt")
                    self.appRoot?.currentRoot = .currentRideScreen
                    self.router?.popToRoot()
                    
                case "failed":
                    self.showRetryAlert = true
                case "cancelled":
                    self.appRoot?.currentRoot = .tabs
                    self.router?.popToRoot()
                default:
                    self.showRetryAlert = false
                }
            } catch {
                print("Error decoding profile document: \(error)")
            }
        }
        
        
    }
    
    
    func updateRemainingTime() {
        let currentTime = Int(Date().timeIntervalSince1970) // Get current Unix time
        print("Current Unix time: \(currentTime)")
        
        if let expireAt = quoteData?.expire_at {
            let timeDifference = expireAt - currentTime // Time difference in seconds
            self.remainingTime = max(timeDifference, 0) // Ensure time is not negative
            print("Remaining time: \(self.remainingTime) seconds")
            
            // Only start the timer if it's not already running
            if timer == nil {
                startTimer()
            }
        } else {
            print("expire_at is nil")
            self.remainingTime = 0
            stopTimer() // Stop the timer if expire_at is nil
        }
    }
    
    func startTimer() {
        // Only start the timer if it is not already running
        if timer == nil {
            timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
                if self.remainingTime > 0 {
                    self.remainingTime -= 1 // Decrease the countdown by 1 second
                } else {
                    self.stopTimer() // Stop timer when time runs out
                    print("Timer has stopped")
                }
            }
        }
    }
    
    func stopTimer() {
        timer?.invalidate()
        timer = nil
    }
    
    func timeString(from seconds: Int) -> String {
        let minutes = seconds / 60
        let seconds = seconds % 60
        return String(format: "%02d:%02d", minutes, seconds)
    }
    
    func retryBooking() {
        let json = RetryBookingModel(second: "120")
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.retryBooking("\(self.quoteData?.id ?? 0)"), body: json)
            .sink { completion in
                self.loading = false
                self.showCanceAlert = false
                switch completion {
                case .finished:
                    print("Success Retry")
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    DispatchQueue.main.async {
                        let alertController = GlobalAlertController(title: String(err.title ?? "Error"), message: String(err.message ?? "Something went wrong"), preferredStyle: .alert)
                        alertController.addAction(UIAlertAction(title: "ok", style: .default, handler: { _ in
                        }))
                        alertController.presentGlobally(animated: true, completion: nil)
                    }
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultResponse = response
            }
            .store(in: &cancellables)
    }
    
    func rejectBooking(){
        self.loading = true
        NetworkManager.shared.POST(to: APIEndpoints.cancelBookingRequest("\(self.quoteData?.id ?? 0)"), body: "")
            .sink { completion in
                self.loading = false
                self.showCanceAlert = false
                switch completion {
                case .finished:
                    self.alertTitle = "\(self.defaultResponse?.title ?? "")"
                    self.alertMessage =  "\(self.defaultResponse?.message ?? "")"
                    self.showSuccess = true
                    
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.alertTitle = "\(err.title ?? "")"
                    self.alertMessage =  "\(err.message ?? "")"
                    self.showAlert = true
                }
            } receiveValue: { (response: DefaultResponse) in
                self.defaultResponse = response;
            }
            .store(in: &cancellables)
        
    }
}

//class BackgroundTask {
//    private var backgroundTask: UIBackgroundTaskIdentifier = .invalid
//
//    func start() {
//        backgroundTask = UIApplication.shared.beginBackgroundTask(withName: "Background Task") {
//            self.stop()
//        }
//    }
//
//    func stop() {
//        UIApplication.shared.endBackgroundTask(backgroundTask)
//        backgroundTask = .invalid
//    }
//}

struct RetryBookingModel: Codable, Hashable {
    let second: String?
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
import Combine
import Foundation

class TimerManager: ObservableObject {
    var cancellables = Set<AnyCancellable>()
    
    let timer = Timer.publish(every: 1, tolerance: 0.5, on: .main, in: .common)
        .autoconnect()
        .share()
    
    let manualTrigger = PassthroughSubject<Void, Never>()
    
    private let backgroundQueue = DispatchQueue(label: "com.GoGulf.rider", qos: .background)
    
    func handleTimerTick() {
        // Your timer logic goes here
        // Combine the timer and manual trigger
        Publishers.Merge(timer.map { _ in }, manualTrigger)
            .subscribe(on: backgroundQueue) // Use this to perform the logic on a background queue
            .sink { [weak self] in
                self?.handleTimerTick()
            }
            .store(in: &cancellables)
    }
    
    func manuallyTriggerTimer() {
        // Manually trigger the timer
        manualTrigger.send()
    }
    
    func stopTimer() {
        // Cancel the timer
        cancellables.forEach { $0.cancel() }
        cancellables.removeAll()
    }
}


