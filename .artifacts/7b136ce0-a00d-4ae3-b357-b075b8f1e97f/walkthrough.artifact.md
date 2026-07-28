# Walkthrough - Task Screen & Google Integration

I have successfully updated the task screens and integrated Google services (Tasks, Calendar, Reminders) to match the new design.

## Changes Made

### 1. Unified Data Model
- Created `TaskUIModel` and `TaskSource` enum to handle tasks from different sources (Local, Google Tasks, Google Calendar, Google Reminders).

### 2. Refined UI Components
- **TaskItem**: Updated with a new design including a left priority/source stripe, rounded checkbox, and source chips (e.g., "✓ You", "G Calendar").
- **AllTaskScreen Header**: Implemented a segmented control showing task counts for "All", "My Tasks", and "Google".
- **Date Grouping**: The "All Tasks" list now groups tasks by date (TODAY, TOMORROW, or specific dates).

### 3. Google Integration
- **DashboardViewModel**: Now observes `GoogleAuthManager`. When a user signs in with Google, it automatically fetches items from Google Calendar and Tasks.
- **Unified Filtering**: The search and status filters (All, Pending, Completed) now work across both local and Google tasks.

### 4. Search Functionality
- Integrated the search bar directly into the "All Tasks" header.

## Verification Results

- **UI Verification**: Task items now feature the stripe indicator and source chips. The header shows the correct counts for each source.
- **Search**: Verified that searching filters both local and Google-sourced tasks.
- **Filtering**: Verified that switching between "All", "My Tasks", and "Google" updates the task list correctly.

> [!NOTE]
> To see Google items, ensure you are signed in via the Integrations screen. The app will use the provided access token to fetch data from Google APIs.
