# Fix Time Quick Picks in TimePickerDialog

The "Now", "Morning", "Afternoon", and "No time" quick pick options in the `TimePickerDialog` are currently non-functional. This plan implements the logic for these options.

## Proposed Changes

### UI Components
#### [MODIFY] [TimePickerDialog.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/customView/TimePickerDialog.kt)
- Update `onTimeSelected` signature to `(String) -> Unit` to easily handle "No time" (empty string).
- Implement `onClick` for quick pick chips:
    - **Now**: Use the current system time.
    - **Morning**: Set to 09:00 AM.
    - **Afternoon**: Set to 02:00 PM.
    - **No time**: Immediately call `onTimeSelected("")` and dismiss.
- Ensure the quick pick chips show the correct selection state (though they are one-time actions, maybe highlighting them briefly or just updating the picker is enough).

### Screens
#### [MODIFY] [AddTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AddTaskScreen.kt)
- Update `TimePickerDialog` call to match the new `onTimeSelected` signature.

## Verification Plan
### Manual Verification
- Open "Add Task" screen.
- Click on "DUE TIME".
- Click "Now": Verify the picker updates to the current time.
- Click "Morning": Verify the picker updates to 09:00 AM.
- Click "Afternoon": Verify the picker updates to 02:00 PM.
- Click "No time": Verify the dialog dismisses and the "DUE TIME" field in `AddTaskScreen` becomes empty or shows "No time".
- Verify that saving a task with "No time" results in a task with only a date (check `TaskViewModel.buildDueDateMillis` logic).
