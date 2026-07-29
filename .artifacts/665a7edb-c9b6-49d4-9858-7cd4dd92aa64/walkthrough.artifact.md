# Walkthrough - Diagnostic Logging & Token Sync for Google API

I have updated the application to help diagnose and fix the 403 Forbidden error when calling Google APIs.

## Changes

### [shared] Component

#### [GoogleCalendarRepository.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/GoogleCalendarRepository.kt)
- **Enhanced Diagnostics**: Added logic to capture and log the response body when an API call fails with a non-2xx status code. This will allow us to see the exact error message from Google (e.g., "Calendar API has not been used in project... before or it is disabled").

#### [DashboardViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/DashboardViewModel.kt)
- **Token Synchronization**: Injected `GoogleAuthManager` and updated `observeGoogleAuth` to call `refreshAccessToken()` whenever a token is received from the database. This ensures that the app uses a fresh, locally-generated Access Token for API calls, rather than relying on potentially stale or incorrect tokens stored in the database.

## Verification Results

### Automated Tests
- Successfully ran `./gradlew :shared:assembleDebug`.

### Manual Verification
- **IMPORTANT**: If the 403 persists, check the Android Logcat for tags starting with `Logger`. You should see a detailed error message from Google.
- **Action Required**: Ensure that both **Google Calendar API** and **Google Tasks API** are enabled in your project's [Google Cloud Console](https://console.cloud.google.com/).
