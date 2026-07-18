import SwiftUI
import Shared
import FirebaseCore

@main
struct iOSApp: App {

    private let aiTaskRepository = IosAiTaskRepositoryImpl()

    init() {
        // ✅ Firebase configure
        FirebaseApp.configure()
    }

    var body: some Scene {
        WindowGroup {
            ContentView(aiTaskRepository: aiTaskRepository)
        }
    }
}
