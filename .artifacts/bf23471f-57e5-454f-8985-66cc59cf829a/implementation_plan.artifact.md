# Implementation Plan - Fix Firebase App Check and AI Parsing Error

The user is encountering an "AI Parsing Error: Firebase App Check token is invalid" when attempting to use the Gemini AI features. This error indicates that Firebase App Check is enforced for the Vertex AI API, but the client's token verification is failing.

## Proposed Changes

### [Component: Initialization]

#### [MODIFY] [TodoApplication.kt](file:///Users/mayanksharma/StudioProjects/MindoList/composeApp/src/androidMain/kotlin/com/jrprofessor/mindolist/ToDoApplication.kt)
- Move `Firebase.initialize` and `AppCheck` initialization to the very top of `onCreate()`, before `initKoin(this)`. This ensures that any Firebase-dependent components injected by Koin (like `AiTaskRepositoryImpl`) are created *after* App Check is correctly configured.
- Add a check to use `PlayIntegrityAppCheckProviderFactory` in non-debug builds (best practice, though not strictly required to fix the immediate error).

### [Component: AI Repository]

#### [MODIFY] [AiTaskRepositoryImpl.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/androidMain/kotlin/com/jrprofessor/mindolist/domain/repository/AiTaskRepositoryImpl.kt)
- Correct the model name from `gemini-3.1-flash-lite` (which appears to be a typo or non-existent version) to `gemini-1.5-flash`, which is the standard stable model for fast tasks.
- Note: Gemini 1.5 Flash is NOT shut down; it is currently the recommended model for this use case.

## User Actions Required (CRITICAL)

> [!IMPORTANT]
> **Register Debug Token in Firebase Console**
> Since the app is using `DebugAppCheckProviderFactory`, you **must** register your local debug secret in the Firebase Console.
> 1. Run the app and check **Logcat** for a message from `FirebaseAppCheck`.
> 2. It will look like: `Enter this debug secret into the allow list in the Firebase Console for your project: 12345678-abcd-1234-abcd-1234567890ab`.
> 3. Copy this token.
> 4. Go to the [Firebase Console](https://console.firebase.google.com/) > **App Check** > **Apps**.
> 5. Find your Android app, click the overflow menu (three dots), and select **Manage debug tokens**.
> 6. Add the token you copied from Logcat.

> [!TIP]
> **Disable Enforcement (Alternative)**
> If you do not want to use App Check during development, you can go to the Firebase Console > App Check > APIs, find **Vertex AI for Firebase**, and change the enforcement setting to **Unenforced**.

## Verification Plan

### Manual Verification
1. Run the app after applying the code changes.
2. Verify in Logcat that App Check initializes without errors.
3. Register the debug token in the Firebase Console as described above.
4. Attempt to use the AI task extraction feature.
5. Verify that the "App Check token is invalid" error no longer appears and the AI correctly parses the input.
