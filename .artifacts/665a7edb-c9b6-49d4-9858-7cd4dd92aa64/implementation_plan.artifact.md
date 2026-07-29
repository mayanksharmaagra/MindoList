# Implementation Plan - Fix Google API 403 Forbidden Error

The 403 error indicates that the server understands the request but refuses to authorize it. This typically happens because:
1.  **Wrong Token**: The token being used is not a valid Access Token for the requested scopes (already fixed in `AndroidGoogleAuthManager`, but maybe not synced to where it's used).
2.  **API Not Enabled**: The Google Calendar or Google Tasks API is not enabled in the Google Cloud Console for the project.
3.  **Missing Sync**: `DashboardViewModel` observes the token from Firebase Realtime Database, but `AndroidGoogleAuthManager` only updates its local state.

## Proposed Changes

### [shared] Component

#### [MODIFY] [GoogleCalendarRepository.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/GoogleCalendarRepository.kt)
- Add detailed error logging for Ktor requests. If a request fails, log the response status and the response body to help diagnose the exact cause (e.g., "API not enabled").

#### [MODIFY] [AndroidGoogleAuthManager.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/androidMain/kotlin/com/jrprofessor/mindolist/utils/AndroidGoogleAuthManager.kt)
- Update `refreshAccessToken` to use the correct scope string.
- Scopes: `oauth2:https://www.googleapis.com/auth/calendar.readonly https://www.googleapis.com/auth/tasks.readonly`. (Ensuring no typo).

#### [MODIFY] [DashboardViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/DashboardViewModel.kt)
- Inject `GoogleAuthManager`.
- When `googleAccessToken` is received from the database, call `googleAuthManager.refreshAccessToken()` to ensure we have a fresh, valid Access Token before calling `fetchAll`.
- Use the refreshed token for API calls.

## User Review Required

> [!IMPORTANT]
> Please ensure that the **Google Calendar API** and **Google Tasks API** are enabled in the Google Cloud Console for your project (the one associated with `server_client_id`).

## Verification Plan

### Automated Tests
- Run `./gradlew :shared:assembleDebug` to verify compilation.

### Manual Verification
- Check Logcat for "Google API Error" to see the exact reason if 403 persists.
- Verify if items appear after the token sync is fixed in `DashboardViewModel`.
