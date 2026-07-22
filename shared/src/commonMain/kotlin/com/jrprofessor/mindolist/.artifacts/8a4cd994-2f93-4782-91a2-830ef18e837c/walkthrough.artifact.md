# Walkthrough - Month/Year Picker & iOS AI Cleanup

I have completed the implementation of the Month/Year picker in `AllTaskScreen` and finalized the removal of AI dependencies from the iOS application.

## Changes Made

### 1. Month/Year Picker (AllTaskScreen)
- **[NEW] [MonthYearPickerDialog.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/customView/MonthYearPickerDialog.kt)**: A custom dialog allowing users to navigate through years and select a month.
- **[MODIFY] [AllTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AllTaskScreen.kt)**: Integrated the dialog into the `SmartWeeklyCalendar`. Clicking the calendar icon now opens the picker, and selecting a date updates the view to the 1st of that month/year.

### 2. iOS AI Cleanup
- **[MODIFY] iOS Entry Points**: Removed the `AiTaskRepository` dependency from `MainViewController.kt`, `Koinios.kt`, `ContentView.swift`, and `iOSApp.swift`.
- **ViewModel Decoupling**: Ensured `TaskViewModel` in `commonMain` handles a null AI repository gracefully, allowing the iOS app to run without AI functionality while keeping it intact for Android.

### 3. Code Quality Improvements
- **Fixed Deprecated Warnings**: Replaced all usages of the deprecated `monthNumber` property from `kotlinx-datetime` with the more idiomatic `month.number` (importing the extension property) across the codebase (`AllTaskScreen.kt`, `DatePickerDialog.kt`, `CommonExtension.kt`).

## Verification

### Android
- [x] "Smart Add" section is visible and functional with Microphone support.
- [x] `AllTaskScreen` calendar icon opens the Month/Year picker.
- [x] Selecting a month/year updates the task list correctly.

### iOS
- [x] App builds successfully.
- [x] "Smart Add" section is hidden.
- [x] No longer depends on Swift-based AI repository implementation.
