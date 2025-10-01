//
//  EarningsVM.swift
// SlyykDriverDriver
//
//  Created by Office on 30/11/2022.
//


import Foundation
import UIKit

class EarningsVM: BaseObservableObject {
    @Published var fromDate = Date()
    @Published var toDate = Date()
    @Published var startDate = ""
    @Published var endDate = ""
    @Published var openPDF = false
    
    @Published var path = ""
    @Published var res: EarningResponse?
    @Published var responseData: DashboardResponse?
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
        self.getEarningsData(start: self.startDate, end: self.endDate)
    }
    
    func getEarningsData(start: String, end: String){
        print(start, end, "---->>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        self.loading = true
//        NetworkManager.shared.get(urlString: "driver/earning?start_date=\(startDate)&end_date=\(endDate)", header: nil) { (RESPONSE_DATA: EarningResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async { [self] in
//                self.loading = false
//                if(URL_RESPONSE?.statusCode == 200){
//                    DispatchQueue.main.async {
//                        self.res = RESPONSE_DATA!
//                    }
//                }
//                else{
//                    self.showAlert(title: String(self.res?.title ?? ""), msg: String(self.res?.message ?? ""))
//                }
//            }
//        }
    }
    
    
    func earningDownload(start: String, end: String){
        self.loading = true
//        NetworkManager.shared.get(urlString: "driver/invoice/download?start_date=\(startDate)&end_date=\(endDate)", header: nil) { (RESPONSE_DATA: invoiceDownloadResponse?, URL_RESPONSE, ERROR) in
//            DispatchQueue.main.async { [self] in
//                self.loading = false
//                if(URL_RESPONSE?.statusCode == 200){
//                    DispatchQueue.main.async {
//                        self.invoiceRes = RESPONSE_DATA!
//                        self.downloadfile(url: self.invoiceRes?.data.link ?? "")
//                    }
//                }
//                else{
//                    self.showAlert(title: String(self.res?.title ?? ""), msg: String(self.res?.message ?? ""))
//                }
//            }
//        }
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
