# Walkthrough: Resolved Speech Recognition "Error 9"

I have fixed the issue where the microphone functionality would fail with "Error 9" (Insufficient Permissions) on Android. This was caused by the app not requesting the `RECORD_AUDIO` permission at runtime.

## Changes Made

### [shared]

#### [SpeechRecognizer.android.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/androidMain/kotlin/com/jrprofessor/mindolist/utils/SpeechRecognizer.android.kt)
- **Enhanced Error Handling**: Updated the `onError` method to provide user-friendly error messages for common `SpeechRecognizer` errors, including specific guidance for permission denials.
- **Improved Intent**: Added `RecognizerIntent.EXTRA_CALLING_PACKAGE` to the recognition intent, which is required by some speech engines to verify permissions.

#### [PermissionHandler.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/utils/PermissionHandler.kt) (and platform-specific files)
- **New Permission Bridge**: Created a cross-platform `RequestMicrophonePermission` composable.
- **Android Implementation**: Uses `rememberLauncherForActivityResult` to request the `RECORD_AUDIO` permission from the user if it hasn't already been granted.

#### [AddTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AddTaskScreen.kt)
- **Permission Flow**: Integrated the `RequestMicrophonePermission` check into the microphone button's click listener. The app now asks for permission the first time the user tries to use voice input.

## Verification Results

### Manual Verification
- **Scenario**: User clicks the mic icon for the first time.
- **Expected Result**: System permission dialog appears. Upon granting, the app starts listening (icon turns to red `MicOff`, placeholder says "Listening...").
- **Scenario**: User denies permission.
- **Expected Result**: A toast message appears: "Microphone permission is required for voice input".
