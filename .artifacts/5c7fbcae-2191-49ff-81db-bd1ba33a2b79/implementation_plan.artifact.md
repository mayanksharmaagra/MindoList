# Implementation Plan - Integrations Screen

Create a new "Integrations" screen that allows users to connect their Google account, and link it from the "Integrations" item in the Settings screen.

## User Review Required

> [!NOTE]
> I will be adding a new navigation route `Integration` to the `Screen` sealed class and wiring it up in the `BottomNavGraph`.
> The "Integrations" screen UI will be implemented as shown in the provided image, featuring a Google connection prompt with a custom illustration.

## Proposed Changes

### [Component] Navigation

#### [MODIFY] [AppNavigation.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/navGraph/AppNavigation.kt)
- Add `Integration` object to the `Screen` sealed class.

#### [MODIFY] [BottomNavGraph.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/navGraph/BottomNavGraph.kt)
- Add a new `composable` route for `Screen.Integration.route` that renders the `IntegrationsScreen`.
- Pass the `onIntegrationClick` callback to `SettingsScreen`.

### [Component] UI - Settings

#### [MODIFY] [SettingsScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/SettingsScreen.kt)
- Update `SettingsScreen` signature to include `onIntegrationClick: () -> Unit`.
- Call `onIntegrationClick` when the "Integrations" menu item is clicked.

### [Component] UI - Integrations Screen

#### [NEW] [IntegrationsScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/IntegrationsScreen.kt)
- Implement the `IntegrationsScreen` composable.
- Include a back button header.
- Create a custom illustration (Google Logo + Checkmark with backdrop and accents).
- Implement the "Connect Google account" button and other text elements as per the image.

## Verification Plan

### Manual Verification
1. Navigate to the **Settings** screen.
2. Click on the **Integrations** item under the "CONNECTIONS" section.
3. Verify that the **Integrations** screen opens.
4. Verify the UI matches the design in the provided image (Header, Illustration, Description, Chips, Button).
5. Click the back button on the Integrations screen and verify it returns to the Settings screen.
