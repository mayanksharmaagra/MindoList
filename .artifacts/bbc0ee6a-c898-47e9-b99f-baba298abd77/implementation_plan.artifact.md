# Fix IllegalStateException: Vertically scrollable component measured with infinity maximum height

The crash is caused by nested scrollable layouts in `IntegrationsScreen.kt`. Both `IntegrationsScreen` and its child component `IntegrationsConnectedScreen` have `Modifier.verticalScroll(rememberScrollState())` applied to their main `Column`s. In Jetpack Compose, nesting scrollable components in the same direction without bounded constraints is disallowed and results in an `IllegalStateException`.

## Proposed Changes

### IntegrationsScreen Component

#### [MODIFY] [IntegrationsScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/IntegrationsScreen.kt)

- Remove `.verticalScroll(rememberScrollState())` from the `Column` inside `IntegrationsConnectedScreen`.
- Ensure the outer `Column` in `IntegrationsScreen` remains scrollable to allow the entire content to be scrolled together.
- Remove the redundant `import androidx.compose.foundation.rememberScrollState` and `import androidx.compose.foundation.verticalScroll` if they become unused in `IntegrationsConnectedScreen`'s scope (though they are still used in the file). Actually, I'll just remove the modifier.

## Verification Plan

### Manual Verification
- Deploy the app and navigate to the Integrations screen.
- Verify that the screen no longer crashes upon loading or interaction.
- Verify that the entire content (including the connected account section) is scrollable as a single unit.
