# Implementation Plan - Network Check Refinement & ViewModel Architecture

Refine network connectivity checks in `FirebaseAuthRepositoryImpl.kt` and improve the architecture of `IntegrationsScreen.kt` by moving repository calls into the `GoogleCalendarViewModel`.

## Proposed Changes

### [shared] Repositories

#### [MODIFY] [FirebaseAuthRepositoryImpl.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/repository/FirebaseAuthRepositoryImpl.kt)
- Add missing network connectivity checks to `saveUserToDatabase`, `uploadProfileImage`, `sendPasswordResetEmail`, `isOtpValid`, and `getResendCooldown`.

### [shared] ViewModels

#### [MODIFY] [GoogleCalendarViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/GoogleCalendarViewModel.kt)
- Inject `FirebaseAuthRepository`.
- Add `updateGoogleIntegration(googleEmail: String, accessToken: String)` method.
- Add `disconnectGoogleIntegration()` method.
- Update `onGoogleSignInSuccess` to be a private implementation detail or ensure it's called correctly from the screen.

### [shared] UI

#### [MODIFY] [IntegrationsScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/IntegrationsScreen.kt)
- Remove direct injection of `FirebaseAuthRepository`.
- Delegate Google integration update and disconnect logic to `GoogleCalendarViewModel`.
- This follows the recommended MVVM architecture, ensuring that the UI doesn't talk to the data layer directly.

## Verification Plan

### Automated Tests
- Run `./gradlew :shared:assembleDebug` to ensure compilation.
- Run `./gradlew :shared:compileKotlinIosSimulatorArm64` for iOS compatibility.

### Manual Verification
- Test "Connect Google account" flow in `IntegrationsScreen`.
- Test "Disconnect" flow in `IntegrationsScreen`.
- Verify that "No internet connection" error appears when offline for all Auth operations.
