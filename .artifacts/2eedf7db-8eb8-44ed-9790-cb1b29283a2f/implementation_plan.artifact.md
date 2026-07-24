# Change Password & Delete Account Screens Implementation Plan

Design and implement the `ChangePasswordScreen` and `DeleteAccountScreen` matching the provided UI mockups and integrate them into the existing settings flow.

## User Review Required

> [!IMPORTANT]
> - The new screens will follow the dark theme (`MindoListBg`).
> - Password strength logic will be implemented in the `ChangePasswordViewModel`.
> - Account deletion will require typing "DELETE" and checking a confirmation box as a safety measure.

## Proposed Changes

### [Navigation & Routes]
- **Modify [Screen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/navGraph/AppNavigation.kt)**: Add `ChangePassword` and `DeleteAccount` screen routes.

### [Presentation Layer]
- **New [ChangePasswordViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/ChangePasswordViewModel.kt)**:
    - Manage state for current, new, and confirm password fields.
    - Implement real-time validation and strength calculation (segments 1-4).
    - Provide `onUpdatePassword` action.
- **New [DeleteAccountViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/DeleteAccountViewModel.kt)**:
    - Manage state for confirmation text ("DELETE") and checkbox status.
    - Provide `onDeleteAccount` action.

### [UI Components]
- **New [ChangePasswordScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/ChangePasswordScreen.kt)**:
    - Implement themed header, password fields with visibility toggles, strength bar, and validation checklist.
- **New [DeleteAccountScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/DeleteAccountScreen.kt)**:
    - Implement warning header, bullet points with 'x' icons, confirmation text field, and agreement checkbox.

### [Integration]
- **Modify [SettingsScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/SettingsScreen.kt)**:
    - Add `onChangePasswordClick` and `onDeleteAccountClick` navigation callbacks.
    - Link the menu items to these callbacks.
- **Modify [BottomNavGraph.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/navGraph/BottomNavGraph.kt)**:
    - Add composable entries for the new screens.
- **Modify [AppModule.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/di/AppModule.kt)**: Register the new ViewModels.

## Verification Plan

### Automated Tests
- Verify that password strength segments update correctly based on input complexity.
- Verify that the "Delete my account" button is only enabled when "DELETE" is typed and the checkbox is checked.

### Manual Verification
- Visual comparison with the provided mockup images.
- Test navigation from Settings -> Change Password -> Back.
- Test navigation from Settings -> Delete Account -> Back.
- Verify visibility toggles in the password fields.
