//
//  Constant.swift
//  GoGulf
//
//  Created by Mac on 4/27/22.
//

import SwiftUI
import CoreLocation
import GooglePlaces
import Foundation
import GoogleMaps

extension Color {
    static let linearGradient = Color.white
    static let radialGradient = Color.white
    static let linearGradient1 = Color.white
    static let linearGradient2 = Color.white
}

extension UIColor {
    static var routeColor: UIColor {
        guard let color = UIColor(named: "AccentColor") else { return .black }
        return color
    }
}

extension Color {
    static let startColor = Color("AccentColor")
    static let endColor = Color("Gold")
    init(hex: Int, opacity: Double = 1.0) {
        let red = Double((hex & 0xff0000) >> 16) / 255.0
        let green = Double((hex & 0xff00) >> 8) / 255.0
        let blue = Double((hex & 0xff) >> 0) / 255.0
        self.init(.sRGB, red: red, green: green, blue: blue, opacity: opacity)
    }
}


extension Color {
    init(hex: UInt, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
}




func getCoordinatesFromPlaceID(placeID: String, completion: @escaping (GeoLocationData?) -> Void) {
    let placesClient = GMSPlacesClient.shared()
    
    placesClient.lookUpPlaceID(placeID) { (place, error) in
        if let error = error {
            print("Error fetching place details: \(error.localizedDescription)")
            completion(nil)
            return
        }
        
        if let place = place {
            let coordinate = place.coordinate
            let postCode = place.addressComponents?.first { $0.types.contains("postal_code") }?.name
            getLocationDataFromCoordinates(latitude:coordinate.latitude, longitude: coordinate.longitude) { coordinate in
                completion(coordinate)
            }
                
        } else {
            print("Place details not found")
            completion(nil)
        }
    }
}


func getLocationDataFromCoordinates(latitude: Double, longitude: Double, completion: @escaping (GeoLocationData?) -> Void) {
    let coordinate = CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    let geocoder = GMSGeocoder()
    
    geocoder.reverseGeocodeCoordinate(coordinate) { response, error in
        if let error = error {
            print("Error fetching place details: \(error.localizedDescription)")
            completion(nil)
            return
        }
        
        if let address = response?.firstResult() {
            let postCode = address.postalCode
//            GeoLocationData(coordinate: CoordinatesData(lat: coordinate.latitude, lng: coordinate.longitude), zipcode: postCode)
            let locationData = GeoLocationData(coordinate: CoordinatesData(lat: coordinate.latitude, lng: coordinate.longitude), zipcode: postCode)
            completion(locationData)
        } else {
            print("No address found for coordinates")
            completion(nil)
        }
    }
}


func getAddressFromCoordinates(location: CLLocationCoordinate2D, completion: @escaping (String?) -> Void) {
    let geocoder = CLGeocoder()
    
    let location = CLLocation(latitude: location.latitude, longitude: location.longitude)
    
    geocoder.reverseGeocodeLocation(location) { placemarks, error in
        if let error = error {
            print("Reverse geocoding failed with error: \(error.localizedDescription)")
            completion(nil)
            return
        }
        
        if let placemark = placemarks?.first {
            // Construct the address string from the placemark components
            var addressString = ""
            if let name = placemark.name {
                addressString += name + ", "
            }
            if let thoroughfare = placemark.thoroughfare {
                addressString += thoroughfare + ", "
            }
            if let locality = placemark.locality {
                addressString += locality + ", "
            }
            if let administrativeArea = placemark.administrativeArea {
                addressString += administrativeArea + ", "
            }
            if let postalCode = placemark.postalCode {
                addressString += postalCode + ", "
            }
            
            if let country = placemark.country {
                addressString += country
            }
            
            print(addressString, "teststtstststsstststts")
            
                let coordinate = CLLocationCoordinate2D(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude)
                let geocoder = GMSGeocoder()
                
                geocoder.reverseGeocodeCoordinate(coordinate) { response, error in
                    if let error = error {
                        print("Error fetching place details: \(error.localizedDescription)")
                        completion(nil)
                        return
                    }
                    
                    if let address = response?.firstResult() {
//                        let postCode = address.postalCode
//                        let fullAddress = "\(postCode ?? "") \(addressString)"
                        let fullAddress = "\(addressString)"
                        completion(fullAddress)
                    } else {
                        print("No address found for coordinates")
                        completion(nil)
                    }
                }
            
        } else {
            print("No placemarks found")
            completion(nil)
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

// Day Substraction



///step 1 -- Create a shape view which can give shape
struct CornerRadiusShape: Shape {
    var radius = CGFloat.infinity
    var corners = UIRectCorner.allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        return Path(path.cgPath)
    }
}

//step 2 - embed shape in viewModifier to help use with ease
struct CornerRadiusStyle: ViewModifier {
    var radius: CGFloat
    var corners: UIRectCorner

    func body(content: Content) -> some View {
        content
            .clipShape(CornerRadiusShape(radius: radius, corners: corners))
    }
}
//step 3 - crate a polymorphic view with same name as swiftUI's cornerRadius
extension View {
    func cornerRadius(radius: CGFloat, corners: UIRectCorner) -> some View {
        ModifiedContent(content: self, modifier: CornerRadiusStyle(radius: radius, corners: corners))
    }
}




func machineName() -> String {
  var systemInfo = utsname()
  uname(&systemInfo)
  let machineMirror = Mirror(reflecting: systemInfo.machine)
  return machineMirror.children.reduce("") { identifier, element in
    guard let value = element.value as? Int8, value != 0 else { return identifier }
    return identifier + String(UnicodeScalar(UInt8(value)))
  }
}

public extension UIDevice {
  static let modelName: String = {
    var systemInfo = utsname()
    uname(&systemInfo)
    let machineMirror = Mirror(reflecting: systemInfo.machine)
    let identifier = machineMirror.children.reduce("") { identifier, element in
      guard let value = element.value as? Int8, value != 0 else { return identifier }
      return identifier + String(UnicodeScalar(UInt8(value)))
    }
    
    func mapToDevice(identifier: String) -> String {
      #if os(iOS)
      switch identifier {
      case "iPod5,1":                                 return "iPod Touch 5"
      case "iPod7,1":                                 return "iPod Touch 6"
      case "iPhone3,1", "iPhone3,2", "iPhone3,3":     return "iPhone 4"
      case "iPhone4,1":                               return "iPhone 4s"
      case "iPhone5,1", "iPhone5,2":                  return "iPhone 5"
      case "iPhone5,3", "iPhone5,4":                  return "iPhone 5c"
      case "iPhone6,1", "iPhone6,2":                  return "iPhone 5s"
      case "iPhone7,2":                               return "iPhone 6"
      case "iPhone7,1":                               return "iPhone 6 Plus"
      case "iPhone8,1":                               return "iPhone 6s"
      case "iPhone8,2":                               return "iPhone 6s Plus"
      case "iPhone9,1", "iPhone9,3":                  return "iPhone 7"
      case "iPhone9,2", "iPhone9,4":                  return "iPhone 7 Plus"
      case "iPhone8,4":                               return "iPhone SE"
      case "iPhone10,1", "iPhone10,4":                return "iPhone 8"
      case "iPhone10,2", "iPhone10,5":                return "iPhone 8 Plus"
      case "iPhone10,3", "iPhone10,6":                return "iPhone X"
      case "iPhone11,2":                              return "iPhone XS"
      case "iPhone11,4", "iPhone11,6":                return "iPhone XS Max"
      case "iPhone11,8":                              return "iPhone XR"
      case "iPad2,1", "iPad2,2", "iPad2,3", "iPad2,4":return "iPad 2"
      case "iPad3,1", "iPad3,2", "iPad3,3":           return "iPad 3"
      case "iPad3,4", "iPad3,5", "iPad3,6":           return "iPad 4"
      case "iPad4,1", "iPad4,2", "iPad4,3":           return "iPad Air"
      case "iPad5,3", "iPad5,4":                      return "iPad Air 2"
      case "iPad6,11", "iPad6,12":                    return "iPad 5"
      case "iPad7,5", "iPad7,6":                      return "iPad 6"
      case "iPad2,5", "iPad2,6", "iPad2,7":           return "iPad Mini"
      case "iPad4,4", "iPad4,5", "iPad4,6":           return "iPad Mini 2"
      case "iPad4,7", "iPad4,8", "iPad4,9":           return "iPad Mini 3"
      case "iPad5,1", "iPad5,2":                      return "iPad Mini 4"
      case "iPad6,3", "iPad6,4":                      return "iPad Pro (9.7-inch)"
      case "iPad6,7", "iPad6,8":                      return "iPad Pro (12.9-inch)"
      case "iPad7,1", "iPad7,2":                      return "iPad Pro (12.9-inch) (2nd generation)"
      case "iPad7,3", "iPad7,4":                      return "iPad Pro (10.5-inch)"
      case "iPad8,1", "iPad8,2", "iPad8,3", "iPad8,4":return "iPad Pro (11-inch)"
      case "iPad8,5", "iPad8,6", "iPad8,7", "iPad8,8":return "iPad Pro (12.9-inch) (3rd generation)"
      case "AppleTV5,3":                              return "Apple TV"
      case "AppleTV6,2":                              return "Apple TV 4K"
      case "AudioAccessory1,1":                       return "HomePod"
      case "i386", "x86_64":                          return "Simulator \(mapToDevice(identifier: ProcessInfo().environment["SIMULATOR_MODEL_IDENTIFIER"] ?? "iOS"))"
      default:                                        return identifier
      }
      #elseif os(tvOS)
      switch identifier {
      case "AppleTV5,3": return "Apple TV 4"
      case "AppleTV6,2": return "Apple TV 4K"
      case "i386", "x86_64": return "Simulator \(mapToDevice(identifier: ProcessInfo().environment["SIMULATOR_MODEL_IDENTIFIER"] ?? "tvOS"))"
      default: return identifier
      }
      #endif
    }
    
    return mapToDevice(identifier: identifier)
  }()
}



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


func getFormattedDate(from date: Date) -> String {
    let dateFormatter = DateFormatter()
    dateFormatter.dateFormat = "yyyy-MM-dd"
    return dateFormatter.string(from: date)
}

//Format get 24Hour Time
func get24HourFormatTime(from date: Date) -> String {
    let dateFormatter = DateFormatter()
    dateFormatter.dateFormat = "HH:mm:ss"
    dateFormatter.locale = Locale(identifier: "en_US_POSIX")
    return dateFormatter.string(from: date)
}

func convertTimestampToDate(_ timestamp: Double) -> Date {
    return Date(timeIntervalSince1970: timestamp)
}



func convertTimestampToDate(_ timestamp: TimeInterval, timeZone: TimeZone = .current) -> String {
  let date = Date(timeIntervalSince1970: timestamp)
  
  let dateFormatter = DateFormatter()
  dateFormatter.dateFormat = "dd MMM HH:mm"
  dateFormatter.timeZone = timeZone
  
  return dateFormatter.string(from: date)
}


// Format Date to a readable string
func formatDateToString(_ date: Date) -> String {
    let dateFormatter = DateFormatter()
    dateFormatter.dateStyle = .medium
    dateFormatter.timeStyle = .medium
    dateFormatter.locale = Locale(identifier: "en_US_POSIX")
    return dateFormatter.string(from: date)
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
