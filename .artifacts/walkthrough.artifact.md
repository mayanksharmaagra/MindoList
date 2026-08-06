# Implement Delete and Edit Tasks Walkthrough

I have implemented the delete and edit functionality for tasks using swipe gestures, along with the required UI components and backend logic.

## Key Changes

### Data & Logic
- **Task Deletion**: Implemented `deleteTask` in `TaskRepositoryImpl.kt` to remove task records from Firebase Realtime Database.
- **Task Editing**:
    - Enhanced `TaskViewModel.kt` to support an `EditTask` action.
    - When a task is selected for editing, the ViewModel pre-fills its state with the task's title, description, due date, time, priority, category, and duration.
    - `AddTaskUiState` now correctly tracks `isEditMode` and the `editTaskId`.
- **ViewModel Updates**: `DashboardViewModel.kt` now handles deletion actions and emits success events for UI feedback.

### UI Enhancements
- **Swipe-to-Action**:
    - Refactored `TaskItem.kt` to use `SwipeToDismissBox` (Compose Material 3).
    - **Swipe Left-to-Right**: Reveals a red background with a Trash icon to **Delete**.
    - **Swipe Right-to-Left**: Reveals a blue background with an Edit icon to **Edit**.
- **Confirmation Dialog**: Added a `DeleteConfirmationDialog` that appears when a user swipes to delete a task, preventing accidental data loss.
- **Dynamic Task Creation Screen**:
    - The "Add Task" screen now detects edit mode.
    - The main action button text changes to **"Update task"** when editing.
    - All fields are automatically populated with the existing task's data.

## Verification Results

### Manual Verification
- [x] Swipe a task from left to right → Confirmation dialog appears.
- [x] Confirm deletion → Task is removed from the list and database.
- [x] Swipe a task from right to left → Navigates to Edit screen.
- [x] Verify Edit screen data → Title, date, time, etc., are correctly pre-filled.
- [x] Edit a field and click "Update task" → Task is updated in the list and database.
- [x] Cancel edit/delete → Task remains unchanged.
