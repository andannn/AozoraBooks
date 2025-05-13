
import ComposeApp

class IOSAnalytics : PlatformAnalytics {
    func logEvent(event: String, params: [String : String]) {
        print("invoke log event in swift")
    }
}
