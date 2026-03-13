package com.jrprofessor.mindolist.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.CircularProgressBar
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.model.Priority
import com.jrprofessor.mindolist.model.Task
import com.jrprofessor.mindolist.model.TaskCardData
import com.jrprofessor.mindolist.presentation.DashboardEvent
import com.jrprofessor.mindolist.presentation.DashboardState
import com.jrprofessor.mindolist.screen.CardTaskSection
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import mindolist.shared.generated.resources.Res
//import mindolist.shared.generated.resources.roboto_condensed_regular
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.Font

// Color Constants
private val PrimaryBlue = Color(0xFF3B82F6)
private val PrimaryBlueDark = Color(0xFF2563EB)
private val PrimaryIndigo = Color(0xFF4F46E5)
private val SuccessGreen = Color(0xFF059669)
private val WarningAmber = Color(0xFFD97706)
private val ErrorRed = Color(0xFFDC2626)
private val ErrorRedDark = Color(0xFFB91C1C)
private val ErrorRedLight = Color(0xFFFEE2E2)
private val ErrorRedMuted = Color(0xFFF87171)
private val TextDark = Color(0xFF1E293B)
private val TextMuted = Color(0xFFB0B8C8)
private val TextGray = Color(0xFF94A3B8)
private val TextSlate = Color(0xFF64748B)
private val TextDeep = Color(0xFF1A1625)
private val BorderLight = Color(0xFFCBD5E1)

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
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
            DashboardContent(state = state)
        }
    }
}

@Composable
fun DashboardContent(state: DashboardState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        UserProfileSection(
            user = state.user,
            greeting = state.greeting,
            currentDate = state.currentDate
        )

        Spacer(modifier = Modifier.height(24.dp))

        CardSection()

        Spacer(modifier = Modifier.height(24.dp))

        CardForDailyProgress()

        Spacer(modifier = Modifier.height(24.dp))

        TodayTask()

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun TodayTask() {
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(1, "Review project proposal", "10:00 AM", Priority.HIGH),
                Task(2, "Team sync meeting", "02:30 PM", Priority.MEDIUM),
                Task(3, "Buy groceries", "08:00 AM", Priority.LOW, isCompleted = true),
                Task(4, "Client presentation", "04:00 PM", Priority.HIGH),
            )
        )
    }
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
                color = PrimaryIndigo
            )
        }
        tasks.forEach { task ->
            TaskItem(
                task = task,
                onToggleComplete = { id ->
                    tasks = tasks.map {
                        if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
                    }
                },
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val checkScale by animateFloatAsState(
        targetValue = if (task.isCompleted) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "checkScale",
    )
    val titleColor by animateColorAsState(
        targetValue = if (task.isCompleted) TextMuted else TextDark,
        animationSpec = tween(durationMillis = 200),
        label = "titleColor",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(task.priority.stripColor),
            )

            Spacer(modifier = Modifier.width(14.dp))

            Box(
                Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (task.isCompleted) task.priority.stripColor
                        else Color.Transparent
                    )
                    .clickable { onToggleComplete(task.id) }, Alignment.Center
            ) {
                if (!task.isCompleted) {
                    Surface(
                        modifier = Modifier.size(24.dp),
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = BorderLight,
                        ),
                    ) {}
                }

                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .scale(checkScale)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
//                    fontFamily = FontFamily(
//                        Font(
//                            Res.font.roboto_condensed_regular,
//                            FontWeight.SemiBold
//                        )
//                    ),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough
                        else TextDecoration.None,
                    ),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⏱",
                        fontSize = 16.sp,
                        color = TextGray,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.time,
//                        fontFamily = FontFamily(
//                            Font(
//                                Res.font.roboto_condensed_regular,
//                                FontWeight.Normal
//                            )
//                        ),
                        color = TextGray,
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(task.priority.backgroundColor)
                            .padding(horizontal = 10.dp, vertical = 3.dp),
                    ) {
                        Text(
                            text = task.priority.label,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = task.priority.color,
                            ),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
fun CardForDailyProgress() {
    val progress = 50f
    val startAngle by remember { mutableFloatStateOf(0f) }
    val progressBarWidth by remember { mutableStateOf(10.dp) }
    val backgroundProgressBarWidth by remember { mutableStateOf(12.dp) }
    val roundBorder by remember { mutableStateOf(true) }
    val animProgress by animateFloatAsState(progress)

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
                modifier = Modifier.size(80.dp),
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
                    style = MaterialTheme.typography.titleLarge,
//                    fontFamily = FontFamily(Font(Res.font.roboto_condensed_regular, FontWeight.SemiBold)),
                    color = TextDark
                )
                Text(
                    text = "No tasks for today yet",
                    style = MaterialTheme.typography.bodyMedium,
//                    fontFamily = FontFamily(Font(Res.font.roboto_condensed_regular, FontWeight.SemiBold)),
                    color = TextSlate
                )
            }
        }
    }
}

@Composable
fun CardSection() {


    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        getTaskCards().forEach { rowCards ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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
        modifier = Modifier
            .weight(1f)
            .padding(4.dp),
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

private fun getTaskCards() = listOf(
    listOf(
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
    listOf(
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

@Composable
fun DashboardPreview() {
    val sampleState = DashboardState(
        user = User(
            uid = "123",
            email = "user@example.com",
            displayName = "John Doe",
            photoUrl = null
        ),
        currentDate = "Wednesday, Feb 18, 2026",
        greeting = "Good Morning",
        isLoading = false
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(20.dp))
            UserProfileSection(
                user = sampleState.user,
                greeting = sampleState.greeting,
                currentDate = sampleState.currentDate
            )
            Spacer(modifier = Modifier.height(24.dp))
            CardSection()
            Spacer(modifier = Modifier.height(24.dp))
            CardForDailyProgress()
            Spacer(modifier = Modifier.height(24.dp))
            TodayTask()
        }
    }
}
