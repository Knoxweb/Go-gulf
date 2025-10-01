//
//  Constant.swift
//  Multibrain
//
//  Created by Prabin Phasikawo on 4/27/22.
//

import SwiftUI
 
extension Color {
    static let linearGradient =  LinearGradient(gradient: Gradient(colors: [Color("Linear1"), Color("Linear2")]), startPoint: .top, endPoint: .bottom)
   static let radialGradient = RadialGradient(gradient: Gradient(colors: [Color("Gradient2"), Color("Gradient1")]), center: .center, startRadius: 10, endRadius: 350)
    
    static let linearGradient1 =  LinearGradient(gradient: Gradient(colors: [Color("Gradient3"), Color("Gradient4")]), startPoint: .top, endPoint: .bottom)
    static let linearGradient2 =  LinearGradient(gradient: Gradient(colors: [Color("Gradient5"), Color("Gradient6")]), startPoint: .top, endPoint: .bottom)
}

extension UIColor {
    static var routeColor: UIColor {
        guard let color = UIColor(named: "AccentColor") else { return .black }
        return color
    }
}

func dateToString(_ date: Date) -> String {
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy-MM-dd"
    return formatter.string(from: date)
}

func stringToDate(_ dateString: String) -> Date? {
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy-MM-dd"
    return formatter.date(from: dateString)
}

class HapticManager {
    static func generateFeedback(for type: UIImpactFeedbackGenerator.FeedbackStyle) {
        let generator = UIImpactFeedbackGenerator(style: type)
        generator.prepare()
        generator.impactOccurred()
    }
}



func convertTimestampToDate(_ timestamp: TimeInterval, timeZone: TimeZone = .current) -> String {
  let date = Date(timeIntervalSince1970: timestamp)
  
  let dateFormatter = DateFormatter()
  dateFormatter.dateFormat = "dd MMM HH:mm"
  dateFormatter.timeZone = timeZone
  
  return dateFormatter.string(from: date)
}


func convertTimestampToDate(_ timestamp: Double) -> Date {
    return Date(timeIntervalSince1970: timestamp)
}



extension Color {
    init(hex: Int, opacity: Double = 1.0) {
        let red = Double((hex & 0xff0000) >> 16) / 255.0
        let green = Double((hex & 0xff00) >> 8) / 255.0
        let blue = Double((hex & 0xff) >> 0) / 255.0
        self.init(.sRGB, red: red, green: green, blue: blue, opacity: opacity)
    }
}



//Check for valid URL
extension String {
    var isValidURL: Bool {
        let detector = try! NSDataDetector(types: NSTextCheckingResult.CheckingType.link.rawValue)
        if let match = detector.firstMatch(in: self, options: [], range: NSRange(location: 0, length: self.utf16.count)) {
            // it is a link, if the match covers the whole string
            return match.range.length == self.utf16.count
        } else {
            return false
        }
    }
}


func defaultError(title: String?, msg: String?) {
    let alertController = GlobalAlertController(title: title ?? "Error", message: msg ?? "Something went wrong.", preferredStyle: .alert)
    alertController.addAction(UIAlertAction(title: "Ok", style: .destructive, handler: nil))
    alertController.presentGlobally(animated: true, completion: nil)
}

func defaultSuccess(title: String?, msg: String?) {
    let alertController = GlobalAlertController(title: title ?? "Success", message: msg ?? "Successfully Submitted", preferredStyle: .alert)
    alertController.addAction(UIAlertAction(title: "Ok", style: .default, handler: nil))
    alertController.presentGlobally(animated: true, completion: nil)
}


//Substract Between the Date from the Specific Date
func addOrSubtractDay(day:Int)->Date{
  return Calendar.current.date(byAdding: .day, value: day, to: Date())!
}

func addOrSubtractMonth(month:Int)->Date{
  return Calendar.current.date(byAdding: .month, value: month, to: Date())!
}

func addOrSubtractYear(year:Int)->Date{
  return Calendar.current.date(byAdding: .year, value: year, to: Date())!
}


//HexString Code Color in View
func hexStringToUIColor (hex:String) -> UIColor {
    var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()

    if (cString.hasPrefix("#")) {
        cString.remove(at: cString.startIndex)
    }

    if ((cString.count) != 6) {
        return UIColor.gray
    }

    var rgbValue:UInt64 = 0
    Scanner(string: cString).scanHexInt64(&rgbValue)

    return UIColor(
        red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
        green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
        blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
        alpha: CGFloat(1.0)
    )
}

//Rounded Corner View
struct RoundedCorners: View {
//    var color: Color = .blue
    var tl: CGFloat = 0.0
    var tr: CGFloat = 0.0
    var bl: CGFloat = 0.0
    var br: CGFloat = 0.0

    var body: some View {
        GeometryReader { geometry in
            Path { path in

                let w = geometry.size.width
                let h = geometry.size.height

                // Make sure we do not exceed the size of the rectangle
                let tr = min(min(self.tr, h/2), w/2)
                let tl = min(min(self.tl, h/2), w/2)
                let bl = min(min(self.bl, h/2), w/2)
                let br = min(min(self.br, h/2), w/2)

                path.move(to: CGPoint(x: w / 2.0, y: 0))
                path.addLine(to: CGPoint(x: w - tr, y: 0))
                path.addArc(center: CGPoint(x: w - tr, y: tr), radius: tr, startAngle: Angle(degrees: -90), endAngle: Angle(degrees: 0), clockwise: false)
                path.addLine(to: CGPoint(x: w, y: h - br))
                path.addArc(center: CGPoint(x: w - br, y: h - br), radius: br, startAngle: Angle(degrees: 0), endAngle: Angle(degrees: 90), clockwise: false)
                path.addLine(to: CGPoint(x: bl, y: h))
                path.addArc(center: CGPoint(x: bl, y: h - bl), radius: bl, startAngle: Angle(degrees: 90), endAngle: Angle(degrees: 180), clockwise: false)
                path.addLine(to: CGPoint(x: 0, y: tl))
                path.addArc(center: CGPoint(x: tl, y: tl), radius: tl, startAngle: Angle(degrees: 180), endAngle: Angle(degrees: 270), clockwise: false)
            }
            .fill(LinearGradient(gradient: Gradient(colors: [Color(hexStringToUIColor(hex: "#7f8c8d")), Color(hexStringToUIColor(hex: "#B4D2C8"))]), startPoint: .top, endPoint: .bottom))
        }
    }
}



// Global function to get the device model
func getDeviceModel() -> String {
    var systemInfo = utsname()
    uname(&systemInfo)
    let modelIdentifier = String(bytes: Data(bytes: &systemInfo.machine, count: Int(_SYS_NAMELEN)), encoding: .ascii)?.trimmingCharacters(in: .controlCharacters) ?? "Unknown"
    return mapToDevice(modelIdentifier: modelIdentifier)
}

// Global function to map the model identifier to the human-readable device name
func mapToDevice(modelIdentifier: String) -> String {
    // iPhone Models (including up to iPhone 16)
    let deviceMap: [String: String] = [
        // iPhone 16 Series (Assuming Identifiers)
        "iPhone16,1": "iPhone 16",
        "iPhone16,2": "iPhone 16 Plus",
        "iPhone16,3": "iPhone 16 Pro",
        "iPhone16,4": "iPhone 16 Pro Max",
        
        // iPhone 15 Series
        "iPhone15,1": "iPhone 15",
        "iPhone15,2": "iPhone 15 Plus",
        "iPhone15,3": "iPhone 15 Pro",
        "iPhone15,4": "iPhone 15 Pro Max",
        
        // iPhone 14 Series
        "iPhone14,7": "iPhone 14",
        "iPhone14,8": "iPhone 14 Plus",
        "iPhone14,2": "iPhone 13 Pro",
        "iPhone14,3": "iPhone 13 Pro Max",
        
        // iPhone 13 Series
        "iPhone13,1": "iPhone 12 mini",
        "iPhone13,2": "iPhone 12",
        "iPhone13,3": "iPhone 12 Pro",
        "iPhone13,4": "iPhone 12 Pro Max",
        
        // iPhone 12 Series
        "iPhone12,1": "iPhone 11",
        "iPhone12,3": "iPhone 11 Pro",
        "iPhone12,5": "iPhone 11 Pro Max",
        
        // iPhone X Series
        "iPhone11,2": "iPhone XS",
        "iPhone11,4": "iPhone XS Max",
        "iPhone11,6": "iPhone XR",
        
        // iPhone 8 and earlier
        "iPhone10,3": "iPhone X",
        "iPhone10,6": "iPhone X (GSM)",
        "iPhone10,1": "iPhone 8",
        "iPhone10,2": "iPhone 8 Plus",
        "iPhone10,4": "iPhone 8",
        "iPhone10,5": "iPhone 8 Plus",
        
        // iPhone SE Models
        "iPhone12,8": "iPhone SE (2nd generation)",
        "iPhone14,6": "iPhone SE (3rd generation)",

        // Add other models as needed
    ]
    
    // Return the mapped device name, or fallback to the identifier
    return deviceMap[modelIdentifier] ?? modelIdentifier
}
