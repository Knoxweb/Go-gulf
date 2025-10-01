//
//  InvoiceVM.swift

//
//  Created by Prabin Phasikawo on 28/05/2024.
//

import Foundation
import FirebaseFirestore
import SwiftUI
import PDFKit
import Combine

import Firebase

class StatementVM: BaseObservableObject {
    let parentCollection = Firestore.firestore().collection("drivers").document("\(UserDefaults.standard.string(forKey: "UID") ?? "")")
    private var statementListener: ListenerRegistration?
    @Published var statements: [StatementModel]?
    @Published public var fromDate = Date.now
    @Published public var toDate = Date.now
    @Published var openPDF = false
    @Published var path = ""
    @Published var defaultRes: DefaultResponse?
    @Published var showCardSheet = false
    @Published var show = false
    @Published var showShareSheet = false
    @Published var fdfFile = Data()
    

    var fileURL: URL?

    public func stopListener() {
        self.statementListener?.remove()
        self.statementListener = nil
    }
    
    func downloadStatement(_ invId: Int?, reference: String?) {
            self.loading = true
            NetworkManager.shared.downloadPDF(from: APIEndpoints.statementPdf("\(invId ?? 0)"))
                .sink { completion in
                    self.loading = false
                    switch completion {
                    case .finished:
                        print("PDF download completed")
                    case .failure(let error):
                        print(error as Any, "server error")
                        self.alertTitle = "Not Found"
                        self.alertMessage = "No file found"
                        self.showAlert.toggle()
                    }
                } receiveValue: { pdfData in
                    if let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first {
                        let fileURL = documentsDirectory.appendingPathComponent("SLYYK\(reference ?? "").pdf")
                        
                        do {
                            try pdfData.write(to: fileURL)
                            self.path = fileURL.path
                            print(self.path, "pathththththth")
                            self.openPDF = true
                            self.fileURL = fileURL
                            self.fdfFile = pdfData
                            
                        } catch {
                            print("Error saving PDF file: \(error)")
                        }
                    }
                }
                .store(in: &cancellables)
        }

    
    
    public func getStatements() {
        let profileDocument = parentCollection.collection("statements")
        self.redacting = true
        statementListener = profileDocument
            .addSnapshotListener { [weak self] (querySnapshot, error) in
                self?.redacting = false
                self?.statements = []
                guard self != nil else { return }
                
                if let error = error {
                    print("Error getting documents: \(error)")
                    return
                }
                guard let querySnapshot = querySnapshot else {
                    print("QuerySnapshot is nil.")
                    return
                }
                if !querySnapshot.isEmpty {
                    for document in querySnapshot.documents {
                        do {
                            let jsonData = try JSONSerialization.data(withJSONObject: document.data(), options: [])
                            let decoder = JSONDecoder()
                            let item = try decoder.decode(StatementModel.self, from: jsonData)
                            self?.statements?.append(item)
                            print(self?.statements as Any, "statetetetetestejejeje")
                        } catch {
                            print("Error decoding document: \(error)")
                        }
                    }
                } else {
                    print("\(profileDocument.path) No documents found.")
                }
            }
    }
}


