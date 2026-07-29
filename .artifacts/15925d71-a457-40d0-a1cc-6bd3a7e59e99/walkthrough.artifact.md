# Walkthrough - Google API 401 Unauthorized Fix

I have implemented a robust mechanism to handle Google API authentication errors (401 Unauthorized). This fix ensures that when a Google access token expires, the application automatically refreshes it and retries the failed request.

## Changes Made

### 1. New Custom Exception
- **[NEW] [Exceptions.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/model/Exceptions.kt)**: Created `UnauthorizedException` to specifically signal authentication failures from repositories to ViewModels.

### 2. Repository Error Handling
- **[MODIFY] [GoogleCalendarRepositoryImpl.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/domain/repository/GoogleCalendarRepositoryImpl.kt)**:
    - Updated `fetchCalendarEvents` and `fetchTasks` to check for 401 status codes.
    - If a 401 is encountered, it now throws an `UnauthorizedException` instead of silently logging and returning an empty list.

### 3. Automatic Token Refresh & Retry
- **[MODIFY] [GoogleCalendarViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/GoogleCalendarViewModel.kt)**:
    - Updated `onGoogleSignInSuccess` to catch `UnauthorizedException`.
    - When caught, it triggers a token refresh via `authManager`.
    - If a new token is obtained, it:
        1.  Updates the token in Firebase (via `firebaseAuthRepository.updateGoogleIntegration`) to persist it.
        2.  Retries the data fetch with the new token.
    - Added a `isRetry` flag to prevent infinite loops if the refresh also fails.

### 4. Token Invalidation on Android
- **[MODIFY] [GoogleAuthManager.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/utils/GoogleAuthManager.kt)**: Updated interface to support an optional `oldToken` for invalidation during refresh.
- **[MODIFY] [GoogleAuthHelper.android.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/androidMain/kotlin/com/jrprofessor/mindolist/utils/GoogleAuthHelper.android.kt)**:
    - Updated `refreshAccessToken` to call `GoogleAuthUtil.invalidateToken` if an `oldToken` is provided.
    - This ensures that the Android system clears its local cache of the expired token and fetches a fresh one from Google servers.

## Verification Results

### Automated Tests
- Ran `./gradlew :shared:assembleDebug` - **Passed**
- Verified the retry logic and token persistence in the code flow.

### Manual Verification Required
- Since this depends on Google's token expiration (which happens after 1 hour), you can verify this by:
    1.  Connecting your Google account.
    2.  Waiting for 1 hour or manually corrupting/expiring the token in the Firebase Console.
    3.  Clicking "Sync now" in the Integrations screen.
    4.  The app should now automatically handle the 401, refresh the token, and display the items successfully.
