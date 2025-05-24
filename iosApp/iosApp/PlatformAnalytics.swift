
import ComposeApp
import FirebaseAnalytics
import FirebaseCrashlytics

class IOSAnalytics : PlatformAnalytics {
    func recordException(throwable: KotlinThrowable) {
        Crashlytics.crashlytics().record(error: toNSError(throwable: throwable))
    }

    func logEvent(event: String, params: [String : String]) {
        print("invoke log event in swift")
        Analytics.logEvent(event, parameters: params)
    }
    
    private func toNSError(throwable: KotlinThrowable) -> NSError {
        let message = throwable.message ?? String(describing: self)

        return NSError(
            domain: "KotlinException",
            code: 0,
            userInfo: [
                NSLocalizedDescriptionKey: message,
                "KotlinThrowable": self,
            ]
        )
    }
}
