package com.jrprofessor.mindolist.screen

//import mindolist.shared.generated.resources.roboto_condensed_regular
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jrprofessor.mindolist.customView.CircularProgressBar
import com.jrprofessor.mindolist.customView.TaskItem
import com.jrprofessor.mindolist.customView.TasksEmptyState
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.extension.today
import com.jrprofessor.mindolist.model.TaskCardData
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.presentation.dashboard.DashboardAction
import com.jrprofessor.mindolist.presentation.dashboard.DashboardEvent
import com.jrprofessor.mindolist.presentation.dashboard.DashboardState
import com.jrprofessor.mindolist.theme.ErrorRed
import com.jrprofessor.mindolist.theme.ErrorRedDark
import com.jrprofessor.mindolist.theme.ErrorRedLight
import com.jrprofessor.mindolist.theme.ErrorRedMuted
import com.jrprofessor.mindolist.theme.PrimaryBlue
import com.jrprofessor.mindolist.theme.PrimaryBlueDark
import com.jrprofessor.mindolist.theme.PrimaryIndigo
import com.jrprofessor.mindolist.theme.SuccessGreen
import com.jrprofessor.mindolist.theme.TextDark
import com.jrprofessor.mindolist.theme.TextDeep
import com.jrprofessor.mindolist.theme.TextGray
import com.jrprofessor.mindolist.theme.TextSlate
import com.jrprofessor.mindolist.theme.WarningAmber
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import com.jrprofessor.mindolist.utils.Logger
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.Instant

// Color Constants

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    navigateToAllTasks: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.dispatch(DashboardAction.DateByTask(today()))
        viewModel.event.collect { event ->
            when (event) {
                is DashboardEvent.Error -> showToast(event.message)
                is DashboardEvent.TaskCompleted -> showToast("Task completed ✓")
                is DashboardEvent.TaskDeleted -> showToast("Task deleted ✓")
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = PrimaryBlue
            )
        } else {
            DashboardContent(
                state = state,
                markCompleted = { taskId, isCompleted ->
                    viewModel.dispatch(DashboardAction.MarkComplete(taskId, isCompleted))
                },
                navigateToAllTasks = navigateToAllTasks
            )
        }
    }
}

@Composable
fun DashboardContent(
    state: DashboardState,
    markCompleted: (String, Boolean) -> Unit,
    navigateToAllTasks: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        UserProfileSection(
            user = state.user,
            greeting = state.greeting,
            currentDate = state.currentDate
        )

        Spacer(modifier = Modifier.height(24.dp))

        CardSection(state.tasks)

        Spacer(modifier = Modifier.height(24.dp))

        CardForDailyProgress(state.tasks)

        Spacer(modifier = Modifier.height(24.dp))

        TodayTask(state.tasks, markCompleted, navigateToAllTasks)

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun TodayTask(
    todayTasks: List<TaskModel> = emptyList(),
    markCompleted: (String, Boolean) -> Unit,
    navigateToAllTasks: () -> Unit
) {
    Logger.error{"TodayTask called"+todayTasks.size}
  /*
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
   val todayTasks = tasks.filter { task ->
        println("due date : ${task.dueDate}")
        val taskDate = Instant.fromEpochMilliseconds(task.dueDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        taskDate == today
    }*/
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Tasks",
                style = MaterialTheme.typography.titleLarge,
//                fontFamily = FontFamily(
//                    Font(
//                        Res.font.roboto_condensed_regular,
//                        FontWeight.SemiBold
//                    )
//                ),
                color = TextDark
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "View All",
                style = MaterialTheme.typography.titleMedium,
//                fontFamily = FontFamily(
//                    Font(
//                        Res.font.roboto_condensed_regular,
//                        FontWeight.Normal
//                    )
//                ),
                color = PrimaryIndigo,
                modifier = Modifier.clickable {
                    navigateToAllTasks()
                }
            )
        }
        if (todayTasks.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                todayTasks.forEach { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = { isComplete ->
                            markCompleted(task.id, isComplete)
                        },
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(20.dp))
            TasksEmptyState()
        }
    }
}



@Composable
fun CardForDailyProgress(todayTasks: List<TaskModel>) {
    val startAngle by remember { mutableFloatStateOf(0f) }
    val progressBarWidth by remember { mutableStateOf(7.dp) }
    val backgroundProgressBarWidth by remember { mutableStateOf(9.dp) }
    val roundBorder by remember { mutableStateOf(true) }

    // ── Progress calculate karo ───────────────────────────────────────────────────


   /*
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
    val todayTasks = tasks.filter { task ->
        val taskDate = Instant.fromEpochMilliseconds(task.dueDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        taskDate == today
    }*/

    val completedToday = todayTasks.filter { it.isCompleted }.size
    val totalToday = todayTasks.size

    val progressPercent: Float = (if (totalToday == 0) 0
    else ((completedToday * 100) / totalToday)).toFloat()

    val progressMessage = when {
        totalToday == 0 -> "No tasks for today yet"
        progressPercent == 100f -> "All done! Great work!"
        progressPercent >= 75 -> "Almost there, keep going!"
        progressPercent >= 50 -> "Halfway through!"
        progressPercent > 0 -> "Just getting started!"
        else -> "Let's get started!"
    }

    val animProgress by animateFloatAsState(progressPercent)


    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 24.dp, vertical = 24.dp
                ), verticalAlignment = Alignment.CenterVertically
        ) {

            CircularProgressBar(
                modifier = Modifier.size(70.dp),
                progress = animProgress,
                progressMax = 100f,
                progressBarColor = btnColor,
                progressBarWidth = progressBarWidth,
                backgroundProgressBarColor = TextGray,
                backgroundProgressBarWidth = backgroundProgressBarWidth,
                roundBorder = roundBorder,
                startAngle = startAngle
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Your Progress",
                    style = MaterialTheme.typography.titleMedium,
//                    fontFamily = FontFamily(Font(Res.font.roboto_condensed_regular, FontWeight.SemiBold)),
                    color = TextDark
                )
                Text(
                    text = progressMessage,
                    style = MaterialTheme.typography.bodyMedium,
//                    fontFamily = FontFamily(Font(Res.font.roboto_condensed_regular, FontWeight.SemiBold)),
                    color = TextSlate
                )
            }
        }
    }
}

@Composable
fun CardSection(tasks: List<TaskModel>) {
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

    val todayStart = today
        .atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    val taskViewList = getTaskCards()
    taskViewList.forEach { itemList ->
        itemList.forEach { card ->
            card.taskCount = when (card.title) {
                "Today" -> tasks.filter { task ->
                    val taskDate = Instant.fromEpochMilliseconds(task.dueDate)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                    taskDate == today
                }.size

                "Completed" -> tasks.filter { it.isCompleted }.size

                "Pending" -> tasks.filter { !it.isCompleted }.size

                "Overdue" -> tasks.filter { task ->
                    task.dueDate < todayStart && !task.isCompleted
                }.size

                else -> 0
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        taskViewList.forEach { rowCards ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCards.forEach { card ->
                    CardTaskSection(card,
//                        FontFamily(Font(Res.font.roboto_condensed_regular,
//                            FontWeight.SemiBold))
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.CardTaskSection(
    data: TaskCardData,
//    semiBoldFont: FontFamily,
) {

    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = data.cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = data.title.uppercase(),
                style = MaterialTheme.typography.titleMedium,
//                fontFamily = semiBoldFont,
                color = data.titleColor
            )
            Text(
                text = data.taskCount.toString(),
                style = MaterialTheme.typography.headlineMedium,
//                fontFamily = semiBoldFont,
                color = data.taskCountColor
            )
            Text(
                text = data.taskMsg,
                style = MaterialTheme.typography.bodyLarge,
//                fontFamily = FontFamily(Font(Res.font.roboto_condensed_regular, FontWeight.Normal)),
                color = data.taskMsgColor
            )
        }
    }
}

@Composable
fun UserProfileSection(
    user: User?,
    greeting: String,
    currentDate: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$greeting, ${user?.displayName?.split(" ")?.get(0) ?: "User"} 👋",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
//            fontFamily = FontFamily(
//                Font(
//                    Res.font.roboto_condensed_regular,
//                    FontWeight.SemiBold
//                )
//            ),
            color = TextDeep
        )
        Text(
            text = currentDate,
            style = MaterialTheme.typography.labelSmall,
//            fontFamily = FontFamily(
//                Font(
//                    Res.font.roboto_condensed_regular,
//                    FontWeight.Normal
//                )
//            ),
            color = TextSlate
        )
    }
}

private fun getTaskCards() = arrayListOf(
    arrayListOf(
        TaskCardData(
            "Today",
            0,
            "Total Tasks",
            Color.White,
            PrimaryIndigo,
            TextDark,
            TextGray
        ),
        TaskCardData(
            "Completed",
            0,
            "Well done!",
            Color.White,
            SuccessGreen,
            TextDark,
            TextGray
        ),
    ),
    arrayListOf(
        TaskCardData(
            "Pending",
            0,
            "To be done",
            Color.White,
            WarningAmber,
            TextDark,
            TextGray
        ),
        TaskCardData(
            "Overdue",
            0,
            "Action required",
            ErrorRedLight,
            ErrorRed,
            ErrorRedDark,
            ErrorRedMuted
        ),
    )
)

@Composable
fun CurrentDateCard(currentDate: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                PrimaryBlue,
                                PrimaryBlueDark
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextDeep
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentDate,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}