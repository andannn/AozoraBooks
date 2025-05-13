import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        KoinLauncherKt.startKoin(analytics: IOSAnalytics(), modules: KoinModulesKt.modules)
    }

    var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea()
        }
    }
}
