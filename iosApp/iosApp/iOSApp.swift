// iosApp/iosApp/iOSApp.swift
import SwiftUI
import FirebaseCore
import FirebaseAppCheck
import Shared

// iosApp/iosApp/iOSApp.swift
import GoogleSignIn

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

        let providerFactory = AppCheckDebugProviderFactory()
        AppCheck.setAppCheckProviderFactory(providerFactory)
        FirebaseApp.configure()

        // Configure GIDSignIn once at launch — Kotlin's GoogleSignInLauncher.ios.kt calls signIn() directly
        if let clientID = FirebaseApp.app()?.options.clientID {
            GIDSignIn.sharedInstance.configuration = GIDConfiguration(clientID: clientID)
        }

        PlatformBridgeHolder.shared.googleSignInBridge = IosGoogleSignInBridge()
        KoinHelper().doInitKoin(aiTaskRepository: IosAiTaskRepositoryImpl())

        return true
    }

    func application(_ app: UIApplication,
                     open url: URL,
                     options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return GIDSignIn.sharedInstance.handle(url)
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
// class AppDelegate: NSObject, UIApplicationDelegate {
//     func application(_ application: UIApplication,
//                      didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
//
//         let providerFactory = AppCheckDebugProviderFactory()
//         AppCheck.setAppCheckProviderFactory(providerFactory)
//         FirebaseApp.configure()
//
//         KoinHelper().doInitKoin(aiTaskRepository: IosAiTaskRepositoryImpl())
//
//         return true
//     }
// }
//
// @main
// struct iOSApp: App {
//     @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
//
//     var body: some Scene {
//         WindowGroup {
//             ContentView()
//         }
//     }
// }
