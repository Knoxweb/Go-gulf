//
//  InvoiceVM.swift
//   GoGulf
//
//  Created by Prabin Phasikawo on 01/12/2022.
//

import Foundation
import UIKit

class InvoiceVMs: BaseObservableObject {
    @Published var fromDate = Date()
    @Published var toDate = Date()
    @Published var startDate = ""
    @Published var endDate = ""
    @Published var openPDF = false
    @Published var path = ""
    @Published var res: InvoiceResponse?
    @Published var invoiceRes: invoiceDownloadResponse?
    
    
    
    func OnChangePicker(_ index: String){
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd/MM/YYYY"
        
        let calendarDate = Calendar.current.dateComponents([.day, .weekday, .year, .month], from: Date())
        let substractedWeek = addOrSubtractDay(day: -(calendarDate.weekday ?? 0))
        //        let substractedYear = substractedWeek - substractedMonth
        //        let yearSubtractedDate = addOrSubtractYear(year: 0)
        //        let thisYear = DateComponents(calendar: .current,year: calendarDate.year, month: 1, day: 1).date
        self.endDate = dateFormatter.string(from: self.toDate)
        
        switch index {
        case "Day" :
            self.startDate = dateFormatter.string(from: Date())
            break;
        case "Week" :
            self.startDate = dateFormatter.string(from: substractedWeek)
            break;
        case "Month" :
            self.startDate = "01/\(calendarDate.month ?? 0)/\(calendarDate.year ?? 0)"
            break;
        case "Year" :
            self.startDate = "01/01/\(calendarDate.year ?? 0)"
            break;
        default :
            break;
        }
        self.getInvoiceData(start: self.startDate, end: self.endDate)
    }
    
    func getInvoiceData(start: String, end: String){
        print(start, end, "---->>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
//        self.loading = true
        NetworkManager.shared.GET(from: "passenger/invoice?start_date=\(start)&end_date=\(end)")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                   print("")
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.showAlert(title: String(err.title ?? ""), msg: String(err.message ?? ""))
                }
            } receiveValue: { (response: InvoiceResponse) in
                self.res = response;
                
            }
            .store(in: &cancellables)
    }
 
    
    //    Download Invoices From the List
    func invoiceListDownload(invoiceId: String){
        NetworkManager.shared.GET(from: "passenger/invoice/download-list?invoice_id=\(invoiceId)")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    DispatchQueue.main.async {
                        self.downloadfile(url: self.invoiceRes?.data.link ?? "")
                    }
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.showAlert(title: String(err.title ?? ""), msg: String(err.message ?? ""))
                }
            } receiveValue: { (response: invoiceDownloadResponse) in
                self.invoiceRes = response;
                
            }
            .store(in: &cancellables)
    }
    
    //    Bulk Invoice Download
    func invoiceDownload(start: String, end: String){
        NetworkManager.shared.GET(from: "passenger/invoice/download?start_date=\(startDate)&end_date=\(endDate)")
            .sink { completion in
                self.loading = false
                switch completion {
                case .finished:
                    DispatchQueue.main.async {
                        self.downloadfile(url: self.invoiceRes?.data.link ?? "")
                    }
                case .failure(let error):
                    let err = NetworkConnection().handleNetworkError(error)
                    self.showAlert(title: String(err.title ?? ""), msg: String(err.message ?? ""))
                }
            } receiveValue: { (response: invoiceDownloadResponse) in
                self.invoiceRes = response;
                
            }
            .store(in: &cancellables)
    }
    
    func downloadfile(url: String){
        let link = URL(string: url)
        FileDownloader.loadFileAsync(url: link!) { (path, error) in
            print("PDF File downloaded to : \(path!)")
            self.path = path ?? ""
            self.openPDF.toggle()
        }
    }
    func showAlert(title: String, msg: String) {
        let alertController = GlobalAlertController(title: title, message: title, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "ok", style: .destructive, handler: nil))
        alertController.presentGlobally(animated: true, completion: nil)
    }
    
    
}
