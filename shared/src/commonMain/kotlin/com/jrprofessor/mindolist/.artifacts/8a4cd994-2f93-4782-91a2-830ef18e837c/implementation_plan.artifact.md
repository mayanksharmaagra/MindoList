# Implementation Plan - Add Month/Year Picker to AllTaskScreen

Implement a custom dialog for selecting a month and year when the calendar icon is clicked in `AllTaskScreen`.

## User Review Required

> [!IMPORTANT]
> The dialog will update `state.selectedDate` to the **1st day** of the selected month and year, as requested.

## Proposed Changes

### Custom Views
#### [NEW] [MonthYearPickerDialog.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/customView/MonthYearPickerDialog.kt)
- Create a `MonthYearPickerDialog` composable.
- Header with current selected year and left/right arrows to change it.
- Grid/List of all 12 month names.
- Callback `onMonthYearSelected(Int, Int)` (year, month).

### AllTaskScreen
#### [MODIFY] [AllTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AllTaskScreen.kt)
- Add a boolean state `showMonthYearDialog` to track visibility.
- Implement the `onCalendarClick` lambda in `SmartWeeklyCalendar` to set `showMonthYearDialog = true`.
- Display `MonthYearPickerDialog` when `showMonthYearDialog` is true.
- When a month/year is selected, dispatch `DashboardAction.SelectedDate` with the 1st day of that month/year and set `showMonthYearDialog = false`.

## Verification Plan

### Manual Verification
- Navigate to `AllTaskScreen`.
- Click the calendar icon in the `SmartWeeklyCalendar`.
- Verify the dialog appears with the current year and month names.
- Change the year using arrows.
- Select a month and verify the calendar and task list update to the 1st day of that month/year.
