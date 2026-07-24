# App Testing Bug Report - MindoList

As an app tester, I have performed a review of the codebase and current UI state. Below is a list of identified bugs, UX issues, and potential crash risks.

## Critical / High Priority

### 1. Unimplemented "Add Task" Actions
In several "Empty State" screens, the primary call-to-action (CTA) buttons are not functional.
- **File:** [DashboardScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/DashboardScreen.kt)
    - **Issue:** `TodayEmptyState` is initialized with an empty lambda `{}` for `onAddTaskClick`.
- **File:** [AllTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AllTaskScreen.kt)
    - **Issue:** `NoTasksEmptyState` is initialized with an empty lambda `{}`.
- **Impact:** New users cannot add tasks from the empty state screens, creating a "dead end" in the UI.

### 2. State Reset Bug when Marking Tasks
- **File:** [DashboardViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/DashboardViewModel.kt)
- **Issue:** In `markAsCompleted`, after a successful update, the state is updated using `_allTasks.value` directly:
  ```kotlin
  _state.update { it.copy(tasks = _allTasks.value, isLoading = false) }
  ```
- **Impact:** If a user has a filter active (e.g., "Pending"), marking a task as complete will **reset the filter** and show all tasks again. This is frustrating for users managing a specific list.

### 3. Crash Risk in AddTaskScreen Initialization
- **File:** [AddTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AddTaskScreen.kt)
- **Issue:** The `initialHour` and `initialMinute` for the `TimePickerDialog` are calculated using fragile string splitting:
  ```kotlin
  initialHour = state.selectedTime.split(" ")[0].split(":")[0].toInt()
  ```
- **Impact:** If `state.selectedTime` is ever empty or in an unexpected format, the app will crash with an `IndexOutOfBoundsException` or `NumberFormatException`.

---

## Medium Priority

### 4. Limited FAB Visibility
- **File:** [HomeScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/HomeScreen.kt)
- **Issue:** The Floating Action Button (FAB) to "Add Task" is only visible on the `Dashboard` screen.
- **Impact:** Users on the `Tasks` list screen (where they are most likely looking at their workload) have to navigate back to the Dashboard to add a new task.

### 5. Inconsistent Theming in Priority Buttons
- **File:** [AddTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AddTaskScreen.kt)
- **Issue:** `PriorityButton` uses hardcoded hex colors (e.g., `0xFFE53E3E`) instead of using `MindoListTheme.colors`.
- **Impact:** These buttons might not adapt correctly to light/dark themes, leading to accessibility issues or poor contrast.

### 6. Unimplemented Search
- **File:** [AllTaskScreen.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/screen/AllTaskScreen.kt)
- **Issue:** The search icon in `AllTasksHeader` has no implementation (`onSearchClick` is a placeholder).
- **Impact:** Users cannot search through their tasks even though the UI suggests they can.

---

## Low Priority / UX Improvements

### 7. Hardcoded Strings in Empty States
- **File:** [TodayEmptyState.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/customView/TodayEmptyState.kt)
- **Issue:** Example task "Book flight tickets" is hardcoded in the component.

### 8. Date/Greeting Refresh
- **File:** [DashboardViewModel.kt](file:///Users/mayanksharma/StudioProjects/MindoList/shared/src/commonMain/kotlin/com/jrprofessor/mindolist/viewmodels/DashboardViewModel.kt)
- **Issue:** The greeting and current date are calculated on `init` and manually via `UpdateDateTime`. If the app stays open past midnight, the "Current Date" label will become stale unless `UpdateDateTime` is triggered.
