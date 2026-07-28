# Robust Firebase Initialization on iOS

The app is crashing with `fatalError` in `Auth.auth()` because Firebase services are being accessed before `FirebaseApp.configure()` has fully registered the default app. This often happens in KMP projects when Koin initialization triggers Firebase service resolution early in the app lifecycle.

## Proposed Changes

### [iosApp] Component

#### [MODIFY] [iOSApp.swift](file:///Users/mayanksharma/StudioProjects/MindoList/iosApp/iosApp/iOSApp.swift)
- Introduce an `AppDelegate` to handle Firebase configuration in `didFinishLaunchingWithOptions`, which is the most reliable lifecycle hook for Firebase.
- Move `AppCheck` and `FirebaseApp.configure()` logic into the `AppDelegate`.
- Use `@UIApplicationDelegateAdaptor` to link the delegate to the SwiftUI `App`.

### [shared] Component

#### [MODIFY] [FirebaseAuthRepositoryImpl.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/repository/FirebaseAuthRepositoryImpl.kt)
- Convert `userRef` and `otpRef` to `lazy` properties to ensure they don't trigger `firebaseDatabase` resolution during the repository's constructor call.

#### [MODIFY] [TaskRepositoryImpl.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/repository/TaskRepositoryImpl.kt)
- Convert `userRef` to `lazy` for the same reason.

## Verification Plan

### Automated Tests
- Run `:shared:assembleDebug` to verify the Kotlin changes.

### Manual Verification
- Deploy the app to an iOS simulator/device and verify that it no longer crashes on startup with the `Auth.auth()` fatal error.
