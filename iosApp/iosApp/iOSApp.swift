import SwiftUI
import Shared
import FirebaseCore

@main
struct iOSApp: App {

    init() {
        // ✅ Firebase configure
        FirebaseApp.configure()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
