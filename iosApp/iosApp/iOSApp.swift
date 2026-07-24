// iosApp/iosApp/iOSApp.swift
import SwiftUI
import FirebaseCore
import FirebaseAppCheck

@main
struct iOSApp: App {
    init() {
        let providerFactory = AppCheckDebugProviderFactory()
        AppCheck.setAppCheckProviderFactory(providerFactory)
        FirebaseApp.configure()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
