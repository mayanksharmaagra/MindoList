import SwiftUI
import Shared
import FirebaseCore

@main
struct iOSApp: App {

    // init() {
    //     // Koin init karo
    // initKoin()  // ya jo bhi aapka init function hai
    // }
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
