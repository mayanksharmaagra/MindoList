package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.jrprofessor.mindolist.customView.TaskItem
import com.jrprofessor.mindolist.customView.TodayEmptyState
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.extension.today
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.model.TaskUIModel
import com.jrprofessor.mindolist.presentation.addTask.AddTaskAction
import com.jrprofessor.mindolist.presentation.dashboard.DashboardAction
import com.jrprofessor.mindolist.presentation.dashboard.DashboardEvent
import com.jrprofessor.mindolist.presentation.dashboard.DashboardState
import com.jrprofessor.mindolist.theme.MindoListTheme
import com.jrprofessor.mindolist.theme.MindoListAccentFixed
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import com.jrprofessor.mindolist.viewmodels.TaskViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    taskViewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
    navigateToAllTasks: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.dispatch(DashboardAction.DateByTask(today()))
        viewModel.event.collect { event ->
            when (event) {
                is DashboardEvent.Error -> showToast(event.message)
                DashboardEvent.TaskCompleted -> showToast("Task completed ✓")
                DashboardEvent.TaskDeleted -> showToast("Task deleted ✓")
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
            DashboardContent(
                state = state,
                markCompleted = { taskId, isCompleted ->
                    viewModel.dispatch(DashboardAction.MarkComplete(taskId, isCompleted))
                },
                onDelete = { taskId -> showDeleteDialog = taskId },
                onEdit = { task ->
                    taskViewModel.dispatch(AddTaskAction.EditTask(task))
                    onAddTaskClick()
                },
                onAddTaskClick = onAddTaskClick,
                navigateToAllTasks = navigateToAllTasks
            )
        }

        if (showDeleteDialog != null) {
            DeleteConfirmationDialog(
                onConfirm = {
                    viewModel.dispatch(DashboardAction.DeleteTask(showDeleteDialog!!))
                    showDeleteDialog = null
                },
                onDismiss = { showDeleteDialog = null }
            )
        }
    }
}

@Composable
fun DashboardContent(
    state: DashboardState,
    markCompleted: (String, Boolean) -> Unit,
    onDelete: (String) -> Unit,
    onEdit: (TaskModel) -> Unit,
    onAddTaskClick: () -> Unit,
    navigateToAllTasks: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        UserProfileSection(
            user = state.user,
            greeting = state.greeting,
            currentDate = state.currentDate
        )

        Spacer(modifier = Modifier.height(24.dp))

        SummaryCardsRow(state.todayTasks)

        Spacer(modifier = Modifier.height(32.dp))

        TodayTask(state.todayTasks, markCompleted, onDelete, onEdit, onAddTaskClick, navigateToAllTasks)

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun UserProfileSection(
    user: User?,
    greeting: String,
    currentDate: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = greeting,
                style = MaterialTheme.typography.bodyLarge,
                color = MindoListTheme.colors.textSecondary
            )
            Text(
                text = user?.displayName ?: "User",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = MindoListTheme.colors.textPrimary
            )
        }

        // Circular Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MindoListAccentFixed),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user?.displayName?.firstOrNull()?.toString()?.uppercase() ?: "U",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun SummaryCardsRow(tasks: List<TaskUIModel>) {
    val todayTasks = tasks // Assuming filtered in VM
    val pendingTasks = tasks.filter { !it.isCompleted }
    val completedCount = tasks.count { it.isCompleted }
    val totalCount = tasks.size
    val completionPercent = if (totalCount > 0) (completedCount * 100 / totalCount) else 0

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            label = "Today",
            value = todayTasks.size.toString(),
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            label = "Pending",
            value = pendingTasks.size.toString(),
            valueColor = MindoListTheme.colors.accent,
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            label = "Completed",
            value = "$completionPercent%",
            valueColor = MindoListTheme.colors.success,
            modifier = Modifier.weight(1.2f)
        )
    }
}

@Composable
fun SummaryCard(
    label: String,
    value: String,
    valueColor: Color = MindoListTheme.colors.textPrimary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = valueColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MindoListTheme.colors.textSecondary
            )
        }
    }
}

@Composable
fun TodayTask(
    todayTasks: List<TaskUIModel> = emptyList(),
    markCompleted: (String, Boolean) -> Unit,
    onDelete: (String) -> Unit,
    onEdit: (TaskModel) -> Unit,
    onAddTaskClick: () -> Unit,
    navigateToAllTasks: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TODAY'S TASKS",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = MindoListTheme.colors.textSecondary
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "See all",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MindoListTheme.colors.accent,
                modifier = Modifier.clickable { navigateToAllTasks() }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        if (todayTasks.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                todayTasks.forEach { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = { isComplete ->
                            markCompleted(task.id, isComplete)
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
            TodayEmptyState(
                onAddTaskClick = onAddTaskClick,
                modifier = Modifier.padding(20.dp),
                nextTaskTitle = "Book flight tickets",
                nextTaskWhen = "tomorrow"
            )
        }
    }
}
