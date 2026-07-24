# Implementation Plan - Fix iOS Build Issues

The iOS build is currently failing due to several issues:
1.  **Gradle Configuration Error**: A `AndroidLocationsBuildService` exception is preventing Gradle from evaluating the project, which blocks the creation of the `shared` framework for iOS.
2.  **CocoaPods Dependency Conflict**: `FirebaseVertexAI` has a version conflict with `FirebaseAuth` regarding the `FirebaseAppCheckInterop` dependency.
3.  **Brittle Post-Install Hook**: A complex `post_install` script in the `Podfile` is causing permission errors during `pod install`.

## Proposed Changes

### [Gradle Configuration]
#### [MODIFY] [libs.versions.toml](file:///Users/mayanksharma/StudioProjects/MindoList/gradle/libs.versions.toml)
- Downgrade Kotlin from `2.3.10` to `2.1.0`.
- Downgrade AGP from `8.9.1` to `8.7.3`.

#### [MODIFY] [shared/build.gradle.kts](file:///Users/mayanksharma/StudioProjects/MindoList/shared/build.gradle.kts)
- Remove the manual version from `kotlin("plugin.serialization")`.
- Ensure `cocoapods` block is consistent.

### [CocoaPods & iOS]
#### [MODIFY] [iosApp/Podfile](file:///Users/mayanksharma/StudioProjects/MindoList/iosApp/Podfile)
- Pin Firebase pods to version `11.12.0` to resolve the `FirebaseAppCheckInterop` conflict.
- Remove the brittle `HeartbeatsPayload.swift` patching logic in the `post_install` hook.
- Clean up the `GoogleUtilities_NSData` modulemap generation if possible, or ensure it doesn't cause errors.

### [Dependency Injection]
#### [MODIFY] [shared/src/iosMain/kotlin/com/jrprofessor/mindolist/di/Koinios.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/iosMain/kotlin/com/jrprofessor/mindolist/di/Koinios.kt)
- Add a check to prevent `startKoin` from being called multiple times (to avoid runtime crashes).

## Verification Plan

### Automated Tests
- Run `./gradlew :shared:generatePodspec` to verify Gradle configuration.
- Run `cd iosApp && pod install` to verify dependency resolution.

### Manual Verification
- Build the `iosApp` in Xcode (user action).
