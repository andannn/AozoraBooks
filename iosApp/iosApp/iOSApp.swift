import SwiftUI
import ComposeApp
import FirebaseCore
import GoogleMobileAds

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        FirebaseApp.configure()
        MobileAds.shared.start(completionHandler: nil)

        #if DEBUG
        print("Running in Debug mode")
        MainViewControllerKt.enableDebugLog()
        #else
        print("Running in Release mode")
        #endif

        return true
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    init() {
        let adViewControllerFactoryModule = KoinLauncher_iosKt.adViewControllerFactoryModule(adViewControllerFactory: IOSPlatformAdViewControllerFactory())
        KoinLauncherKt.startKoin(analytics: IOSAnalytics(), modules: KoinModulesKt.modules + [adViewControllerFactoryModule])
    }

    var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea()
        }
    }
}

