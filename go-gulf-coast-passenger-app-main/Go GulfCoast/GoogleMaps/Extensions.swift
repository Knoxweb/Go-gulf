
import GoogleMaps
import SwiftUI

extension GMSMapView {
    
    static let kPadding: CGFloat = 115
    /*
     Static path String for demoing purpose you will get actual path by direction  api for google for more https://console.cloud.google.com/apis/library/directions-backend.googleapis.com?filter=category:maps&id=c6b51d83-d721-458f-a259-fae6b0af35c5&project=ios-task
     */
    static let pathString: String = "_gfzDaiksMnGeF\\WaCmDyAyBRQJPxHlLdD~EjRrYzJvOzBlDd@K|F}DLGTAX?tHkFJIJX~HdRbKvVBHzBqAnAw@|GcEpDaClApCrBoAhHqEtAw@fC`Gx@`B|@xB^v@B@FAjA}@tNfMdGnFVPNBRG~AwAd@MfK}AJCH^RnAHZN?|Ag@"
    
    func updateMap(toLocation location: CLLocation, zoomLevel: Float? = nil) {
        if let zoomLevel = zoomLevel {
            let cameraUpdate = GMSCameraUpdate.setTarget(location.coordinate, zoom: zoomLevel)
            animate(with: cameraUpdate)
        } else {
            animate(toLocation: location.coordinate)
        }
    }
    func drawPath(_ encodedPathString: String) {
        print("opopopopopoopo")
        CATransaction.begin()
        CATransaction.setAnimationDuration(0.0)
        let path = GMSPath(fromEncodedPath: encodedPathString)
        let line = GMSPolyline(path: path)
        line.strokeWidth = 4.0
        line.strokeColor = UIColor.routeColor
        line.isTappable = true
        line.map = self
        CATransaction.commit()
    }
}



extension Color {
    static let systemBackground = Color("FormField")
}

struct ClearListBackgroundModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.scrollContentBackground(.hidden)
        } else {
            content
        }
    }
}

extension View {
    func clearListBackground() -> some View {
        modifier(ClearListBackgroundModifier())
    }
}


struct TextFieldStyle: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding()
            .frame(height: 50)
            .background(Color("FormField"))
            .clipShape(RoundedRectangle(cornerRadius: 6))
            .foregroundColor(Color.black)
            .textFieldStyle(.plain)
            .overlay(RoundedRectangle(cornerRadius: 6).stroke(Color.gray.opacity(0.4), lineWidth: 1))
    }
}
extension View {
    func textFieldStyle() -> some View {
        self.modifier(TextFieldStyle())
    }
}




extension View {
    func focusNextField<F: RawRepresentable>(_ field: FocusState<F?>.Binding) where F.RawValue == Int {
        guard let currentValue = field.wrappedValue else { return }
        let nextValue = currentValue.rawValue + 1
        if let newValue = F.init(rawValue: nextValue) {
            field.wrappedValue = newValue
        }
    }

    func focusPreviousField<F: RawRepresentable>(_ field: FocusState<F?>.Binding) where F.RawValue == Int {
        guard let currentValue = field.wrappedValue else { return }
        let nextValue = currentValue.rawValue - 1
        if let newValue = F.init(rawValue: nextValue) {
            field.wrappedValue = newValue
        }
    }
}



extension View {
    func animatePlaceholder(isLoading: Binding<Bool>) -> some View {
        self.modifier(AnimatePlaceholderModifier(isLoading: isLoading))
    }
}


struct AnimatePlaceholderModifier: AnimatableModifier {
    @Binding var isLoading: Bool

    @State private var isAnim: Bool = false
    private var center = (UIScreen.main.bounds.width / 2) + 110
    private let animation: Animation = .linear(duration: 1.5)

    init(isLoading: Binding<Bool>) {
        self._isLoading = isLoading
    }

    func body(content: Content) -> some View {
        content.overlay(animView.mask(content))
    }

    var animView: some View {
        ZStack {
            Color.black.opacity(isLoading ? 0.09 : 0.0)
            Color.black.mask(
                Rectangle()
                    .fill(
                        LinearGradient(gradient: .init(colors: [.clear, .white.opacity(0.48), .clear]), startPoint: .top , endPoint: .bottom)
                    )
                    .scaleEffect(1.5)
                    .rotationEffect(.init(degrees: 70.0))
                    .offset(x: isAnim ? center : -center)
            )
        }
        .animation(isLoading ? animation.repeatForever(autoreverses: false) : nil, value: isAnim)
        .onAppear {
            guard isLoading else { return }
            isAnim.toggle()
        }
        .onChange(of: isLoading) { _ in
            isAnim.toggle()
        }
    }
}


extension CLLocationCoordinate2D {
    
    func bearing(to point: CLLocationCoordinate2D) -> Double {
        func degreesToRadians(_ degrees: Double) -> Double { return degrees * Double.pi / 180.0 }
        func radiansToDegrees(_ radians: Double) -> Double { return radians * 180.0 / Double.pi }
        
        let fromLatitude = degreesToRadians(latitude)
        let fromLongitude = degreesToRadians(longitude)
        
        let toLatitude = degreesToRadians(point.latitude)
        let toLongitude = degreesToRadians(point.longitude)
        
        let differenceLongitude = toLongitude - fromLongitude
        
        let y = sin(differenceLongitude) * cos(toLatitude)
        let x = cos(fromLatitude) * sin(toLatitude) - sin(fromLatitude) * cos(toLatitude) * cos(differenceLongitude)
        let radiansBearing = atan2(y, x);
        let degree = radiansToDegrees(radiansBearing)
        return (degree >= 0) ? degree : (360 + degree)
    }
}

extension CLLocationCoordinate2D : Equatable{
    public static func == (lhs: CLLocationCoordinate2D, rhs: CLLocationCoordinate2D) -> Bool {
        return lhs.latitude == rhs.latitude && lhs.longitude == rhs.longitude
    }
    //distance in meters, as explained in CLLoactionDistance definition
    func distance(from: CLLocationCoordinate2D) -> CLLocationDistance {
        let destination=CLLocation(latitude:from.latitude,longitude:from.longitude)
        return CLLocation(latitude: latitude, longitude: longitude).distance(from: destination)
    }
}


struct AnimationData {
    var delay: TimeInterval
}


private struct DotView: View {
    
    @Binding var scale: CGFloat

 
    var body: some View {
        Circle()
            .scale(scale)
            .fill(Color.accentColor.opacity(scale >= 0.7 ? scale : scale - 0.1))
            .frame(width: 30, height: 30, alignment: .center)
    }
}

struct ThreeBounceAnimation: View {

    @State var scales: [CGFloat] = DATA.map { _ in return 0 }

    var animation = Animation.easeInOut.speed(0.8)
    static let DATA = [
        AnimationData(delay: 0.0),
        AnimationData(delay: 0.2),
        AnimationData(delay: 0.4),
    ]
    
    var body: some View {
        HStack {
            DotView(scale: .constant(scales[0]))
            DotView(scale: .constant(scales[1]))
            DotView(scale: .constant(scales[2]))
        }
        .onAppear {
            animateDots() // Not defined yet
        }
    }
    
    
    func animateDots() {
        for (index, data) in Self.DATA.enumerated() {
            DispatchQueue.main.asyncAfter(deadline: .now() + data.delay) {
                animateDot(binding: $scales[index], animationData: data)
            }
        }

        //Repeat
        DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
            animateDots()
        }
    }

    func animateDot(binding: Binding<CGFloat>, animationData: AnimationData) {
        withAnimation(animation) {
            binding.wrappedValue = 1
        }

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.4) {
            withAnimation(animation) {
                binding.wrappedValue = 0.2
            }
        }
    }
}




