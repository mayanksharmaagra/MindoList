# Task Screen & Integration Update

The goal is to update the `AllTaskScreen` and `DashboardScreen` to match the provided design, including a new header with counts, search functionality, updated task item UI, and integration with Google Tasks, Calendar, and Reminders.

## User Review Required

> [!IMPORTANT]
> The implementation relies on `GoogleAuthManager` providing an `accessToken`. Ensure that Google Sign-In is properly configured in the app to populate this data.

## Proposed Changes

### [Core Models]

#### [NEW] [TaskUIModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/model/TaskUIModel.kt)
- Unified model to represent both local tasks and Google items.
- Added `TaskSource` enum: `MY_TASK`, `GOOGLE_TASK`, `GOOGLE_CALENDAR`, `GOOGLE_REMINDER`.

### [ViewModel Layer]

#### [MODIFY] [DashboardState.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/presentation/dashboard/DashboardState.kt)
- Update `tasks` to `List<TaskUIModel>`.
- Add count fields: `myTasksCount`, `googleTasksCount`, `allTasksCount`.
- Add `selectedSourceFilter`: `ALL`, `MY_TASKS`, `GOOGLE`.

#### [MODIFY] [DashboardAction.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/presentation/dashboard/DashboardAction.kt)
- Add `SourceFilterSelected(filter: String)`.

#### [MODIFY] [DashboardViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/DashboardViewModel.kt)
- Inject `GoogleAuthManager` and `GoogleCalendarRepository`.
- Implement logic to fetch Google items when authenticated.
- Update filtering and mapping logic to use `TaskUIModel`.
- Group tasks by date for the `AllTaskScreen`.

### [UI Layer]

#### [MODIFY] [TaskItem.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/customView/TaskItem.kt)
- Redesign card with darker background and rounded checkbox.
- Add left colored indicator stripe.
- Add source chips (e.g., "✓ You", "G Calendar").
- Use unified `TaskUIModel`.

#### [MODIFY] [AllTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AllTaskScreen.kt)
- Implement segmented header with task source counts.
- Add group headers for dates (e.g., "TODAY", "TOMORROW").
- Ensure search functionality is fully integrated.

#### [MODIFY] [DashboardScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/DashboardScreen.kt)
- Update to use the new `TaskItem` and source filtering if applicable.

## Verification Plan

### Automated Tests
- N/A (Manual UI verification preferred for this task)

### Manual Verification
- Deploy to Android emulator.
- Verify "All Tasks" screen header shows correct counts.
- Verify task items match the image design (stripe, chips, checkbox).
- Perform search and verify filtering works across all sources.
- Check/uncheck a task and verify state update.
- Ensure Google integration fetches and displays items (requires Google Sign-In).
