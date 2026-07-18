# Implementation Plan - Restrict AI to Android & Add Mic Support

This plan covers removing AI-related "Smart Add" features from iOS and implementing a Microphone-based speech-to-text input for the Android "Smart Add" feature.

## User Review Required

> [!IMPORTANT]
> The "Smart Add" feature (AI-powered task parsing) will be completely hidden on iOS.
> On Android, a new Microphone icon will be added to allow users to speak their task prompt.

## Proposed Changes

### 1. Platform & Permissions
- **[MODIFY] [Platform.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/Platform.kt)**: Add `val isAndroid: Boolean` to the interface.
- **[MODIFY] [Platform.android.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/androidMain/kotlin/com/jrprofessor/mindolist/Platform.android.kt)**: Set `isAndroid = true`.
- **[MODIFY] [Platform.ios.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/iosMain/kotlin/com/jrprofessor/mindolist/Platform.ios.kt)**: Set `isAndroid = false`.
- **[MODIFY] [AndroidManifest.xml](file:///Users/mayanksharma/StudioProjects/MindoList/composeApp/src/androidMain/AndroidManifest.xml)**: Add `android.permission.RECORD_AUDIO`.

### 2. Speech-to-Text (STT) Service
- **[NEW] [SpeechRecognizer.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/utils/SpeechRecognizer.kt)**: Define an interface for STT.
- **[NEW] [SpeechRecognizer.android.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/androidMain/kotlin/com/jrprofessor/mindolist/utils/SpeechRecognizer.android.kt)**: Implement using Android's `SpeechRecognizer` API.
- **[NEW] [SpeechRecognizer.ios.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/iosMain/kotlin/com/jrprofessor/mindolist/utils/SpeechRecognizer.ios.kt)**: Dummy implementation for iOS.
- **[MODIFY] [AppModule.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/di/AppModule.kt)**: Register the STT service in Koin.

### 3. UI & ViewModel
- **[MODIFY] [AddTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AddTaskScreen.kt)**:
    - Hide the entire "Smart Add" section if `getPlatform().isAndroid` is false.
    - Add a Microphone `IconButton` as a leading icon in the `OutlinedTextField`.
    - Implement the logic to start/stop listening and update the `naturalInput` state.
- **[MODIFY] [TaskViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/TaskViewModel.kt)**:
    - Handle the `AiTaskRepository` as an optional dependency (for iOS).

## Verification Plan

### Manual Verification
1. **Android**:
    - Verify "Smart Add" is visible.
    - Click Mic button, speak a task (e.g., "Buy milk tomorrow"), and ensure it populates the text field.
    - Verify AI parsing still works.
2. **iOS**:
    - Verify "Smart Add" section is hidden.
    - Ensure the app builds and runs without crashing due to missing dependencies.
