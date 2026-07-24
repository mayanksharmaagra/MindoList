# Fix Speech-to-Text "Error 9" (Insufficient Permissions)

The reported "error 9" corresponds to `ERROR_INSUFFICIENT_PERMISSIONS` in the Android `SpeechRecognizer` API. This means the app has not been granted the `RECORD_AUDIO` permission at runtime, or the speech recognition service lacks necessary access.

## User Review Required

> [!IMPORTANT]
> This fix requires adding runtime permission handling for the microphone on Android. The user will be prompted to grant audio recording permission when they first click the microphone button.

## Proposed Changes

### [shared]

#### [MODIFY] [AndroidSpeechToTextParser.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/androidMain/kotlin/com/jrprofessor/mindolist/utils/SpeechRecognizer.android.kt)
- Update `startListening` to include `RecognizerIntent.EXTRA_CALLING_PACKAGE`.
- Improve error reporting to explicitly mention permission issues when error code 9 is received.

#### [MODIFY] [AddTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AddTaskScreen.kt)
- Implement a platform-specific check for microphone permissions.
- On Android, use `rememberLauncherForActivityResult` with `ActivityResultContracts.RequestPermission()` to request `Manifest.permission.RECORD_AUDIO` when the user toggles recording.

## Verification Plan

### Manual Verification
1. Open the "Add Task" screen on an Android device.
2. Click the microphone icon.
3. Verify that a system permission dialog appears asking for microphone access.
4. Grant the permission and verify that the "Listening..." state is entered without "Error 9".
5. Speak and verify transcription works.
