import SwiftUI
import FirebaseCore
import FirebaseAppCheck
import Shared
import GoogleSignIn
import UserNotifications

class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

        let providerFactory = AppCheckDebugProviderFactory()
        AppCheck.setAppCheckProviderFactory(providerFactory)
        FirebaseApp.configure()

        UNUserNotificationCenter.current().delegate = self
        
        // Request notification permission on startup to ensure system is ready
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { _, _ in }

        // Configure GIDSignIn once at launch
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

    // Show notifications when app is in foreground
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.banner, .list, .sound])
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
