package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.DeleteConfirmationDialog
import com.jrprofessor.mindolist.customView.NoTasksEmptyState
import com.jrprofessor.mindolist.customView.TaskItem
import com.jrprofessor.mindolist.extension.today
import com.jrprofessor.mindolist.model.Filter
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.presentation.addTask.AddTaskAction
import com.jrprofessor.mindolist.presentation.dashboard.DashboardAction
import com.jrprofessor.mindolist.presentation.dashboard.DashboardEvent
import com.jrprofessor.mindolist.presentation.dashboard.DashboardState
import com.jrprofessor.mindolist.theme.MindoListTheme
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import com.jrprofessor.mindolist.viewmodels.TaskViewModel
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
fun AllTaskScreen(
    dashboardViewModel: DashboardViewModel,
    taskViewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
) {
    val state by dashboardViewModel.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        dashboardViewModel.dispatch(DashboardAction.LoadTasks)
    }
    LaunchedEffect(Unit) {
        dashboardViewModel.event.collect { event ->
            when (event) {
                is DashboardEvent.Error -> showToast(event.message)
                is DashboardEvent.TaskCompleted -> showToast("Task completed ✓")
                is DashboardEvent.TaskDeleted -> showToast("Task deleted ✓")
                is DashboardEvent.SyncSuccess -> showToast("Sync successful ✓")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MindoListTheme.colors.background)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MindoListTheme.colors.accent
            )
        } else {
            AllTaskScreenContent(
                state = state,
                onFilterSelected = { filter ->
                    dashboardViewModel.dispatch(DashboardAction.FilterSelected(filter))
                },
                onSourceFilterSelected = { source ->
                    dashboardViewModel.dispatch(DashboardAction.SourceFilterSelected(source))
                },
                markCompleted = { id, isComplete ->
                    dashboardViewModel.dispatch(DashboardAction.MarkComplete(id, isComplete))
                },
                onTogglePin = { id, isPinned ->
                    dashboardViewModel.dispatch(DashboardAction.TogglePin(id, isPinned))
                },
                onDelete = { taskId -> showDeleteDialog = taskId },
                onEdit = { task ->
                    taskViewModel.dispatch(AddTaskAction.EditTask(task))
                    onAddTaskClick()
                },
                onSearchQueryChanged = { dashboardViewModel.dispatch(DashboardAction.SearchQueryChanged(it)) },
                onToggleSearch = { dashboardViewModel.dispatch(DashboardAction.ToggleSearch) },
                onAddTaskClick = onAddTaskClick
            )
        }

        if (showDeleteDialog != null) {
            DeleteConfirmationDialog(
                onConfirm = {
                    dashboardViewModel.dispatch(DashboardAction.DeleteTask(showDeleteDialog!!))
                    showDeleteDialog = null
                },
                onDismiss = { showDeleteDialog = null }
            )
        }
    }
}

@Composable
fun AllTaskScreenContent(
    state: DashboardState,
    onFilterSelected: (String) -> Unit,
    onSourceFilterSelected: (String) -> Unit,
    markCompleted: (String, Boolean) -> Unit,
    onTogglePin: (String, Boolean) -> Unit,
    onDelete: (String) -> Unit,
    onEdit: (TaskModel) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onToggleSearch: () -> Unit,
    onAddTaskClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        AllTasksHeader(
            isSearchActive = state.isSearchActive,
            searchQuery = state.searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onToggleSearch = onToggleSearch
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        SourceSegmentedControl(
            selectedSource = state.selectedSourceFilter,
            allCount = state.allTasksCount,
            myTasksCount = state.myTasksCount,
            googleCount = state.googleTasksCount,
            onSourceSelected = onSourceFilterSelected
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        FilterChipsRow(
            selectedFilter = state.selectedFilter,
            onFilterSelected = onFilterSelected
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Task list with Date Grouping
        val groupedTasks = remember(state.tasks) {
            val today = today()
            val tomorrow = today.plus(DatePeriod(days = 1))

            state.tasks.groupBy { task ->
                if (task.dueDate == 0L) "NO DATE"
                else {
                    val instant = Instant.fromEpochMilliseconds(task.dueDate)
                    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                    
                    when {
                        date < today -> "OVERDUE"
                        date == today -> "TODAY"
                        date == tomorrow -> "TOMORROW"
                        date.month == today.month && date.year == today.year -> {
                            // Same month, show date
                            "${date.day} ${date.month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }}"
                        }
                        else -> {
                            // Different month, group by month
                            "${date.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${date.year}"
                        }
                    }
                }
            }
        }

        if (state.tasks.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                NoTasksEmptyState(
                    title = "No tasks found",
                    description = "Try changing your filters or search query.",
                    showAddButton = state.totalTasksCount == 0,
                    onAddTaskClick = onAddTaskClick
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (state.viewMode == com.jrprofessor.mindolist.presentation.settings.ViewMode.GROUPED) {
                    groupedTasks.forEach { (dateLabel, tasks) ->
                        item {
                            Text(
                                text = dateLabel,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = MindoListTheme.colors.textSecondary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(items = tasks, key = { it.id }) { task ->
                            TaskItem(
                                task = task,
                                onToggleComplete = { isCompleted ->
                                    markCompleted(task.id, isCompleted)
                                },
                                onTogglePin = { isPinned ->
                                    onTogglePin(task.id, isPinned)
                                },
                                onDelete = { onDelete(task.id) },
                                onEdit = { 
                                    if (task.originalModel is TaskModel) {
                                        onEdit(task.originalModel)
                                    }
                                }
                            )
                        }
                    }
                } else {
                    items(items = state.tasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onToggleComplete = { isCompleted ->
                                markCompleted(task.id, isCompleted)
                            },
                            onTogglePin = { isPinned ->
                                onTogglePin(task.id, isPinned)
                            },
                            onDelete = { onDelete(task.id) },
                            onEdit = { 
                                if (task.originalModel is TaskModel) {
                                    onEdit(task.originalModel)
                                }
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun SourceSegmentedControl(
    selectedSource: String,
    allCount: Int,
    myTasksCount: Int,
    googleCount: Int,
    onSourceSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MindoListTheme.colors.cardBg)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SourceTab(
            label = "All · $allCount",
            isSelected = selectedSource == "ALL",
            modifier = Modifier.weight(1f),
            onClick = { onSourceSelected("ALL") }
        )
        SourceTab(
            label = "My Tasks · $myTasksCount",
            isSelected = selectedSource == "MY_TASKS",
            modifier = Modifier.weight(1.2f),
            onClick = { onSourceSelected("MY_TASKS") }
        )
        SourceTab(
            label = "Google · $googleCount",
            isSelected = selectedSource == "GOOGLE",
            modifier = Modifier.weight(1f),
            onClick = { onSourceSelected("GOOGLE") },
            isGoogle = true
        )
    }
}

@Composable
fun SourceTab(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isGoogle: Boolean = false
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MindoListTheme.colors.accent else Color.Transparent,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isGoogle) {
                Text("G ", color = if (isSelected) Color.Black else MindoListTheme.colors.textSecondary, fontWeight = FontWeight.Bold)
            }
            Text(
                text = label,
                color = if (isSelected) Color.Black else MindoListTheme.colors.textSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AllTasksHeader(
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onToggleSearch: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSearchActive) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = { Text("Search tasks...", color = MindoListTheme.colors.textSecondary) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MindoListTheme.colors.accent,
                    unfocusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                    focusedContainerColor = MindoListTheme.colors.inputBg,
                    unfocusedContainerColor = MindoListTheme.colors.inputBg,
                    focusedTextColor = MindoListTheme.colors.textPrimary,
                    unfocusedTextColor = MindoListTheme.colors.textPrimary
                ),
                trailingIcon = {
                    IconButton(onClick = onToggleSearch) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Search",
                            tint = MindoListTheme.colors.textPrimary
                        )
                    }
                }
            )
        } else {
            Text(
                text = "All Tasks",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MindoListTheme.colors.textPrimary,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                onClick = onToggleSearch,
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = MindoListTheme.colors.cardChildBg,
                border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MindoListTheme.colors.textPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChipsRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf(Filter.ALL, Filter.PENDING, Filter.COMPLETED, Filter.OVERDUE)
    
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        filters.forEach { filter ->
            val isSelected = selectedFilter == filter.name
            
            Surface(
                onClick = { onFilterSelected(filter.name) },
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(24.dp),
                color = if (isSelected) MindoListTheme.colors.accent else MindoListTheme.colors.cardChildBg,
                border = if (isSelected) null else BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = if (isSelected) Color.Black else MindoListTheme.colors.textSecondary,
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
