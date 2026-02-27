package com.jrprofessor.mindolist.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jrprofessor.mindolist.R
import com.jrprofessor.mindolist.customView.CircularProgressBar
import com.jrprofessor.mindolist.dashbaord.DashboardViewModel
import com.jrprofessor.mindolist.data.model.Priority
import com.jrprofessor.mindolist.data.model.Task
import com.jrprofessor.mindolist.data.model.TaskCardData
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.presentation.DashboardState
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF3B82F6)
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
            .verticalScroll(rememberScrollState()) // ← add this
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // User Profile Section
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
    val condensedSemiBold = remember {
        FontFamily(
            Font(
                R.font.roboto_condensed_regular,
                FontWeight.SemiBold
            )
        )
    }
    val condensedNormal = remember {
        FontFamily(
            Font(
                R.font.roboto_condensed_regular,
                FontWeight.Normal
            )
        )
    }
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
                fontFamily = condensedSemiBold,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "View All",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = condensedNormal,
                color = Color(0xFF4F46E5)
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
        targetValue = if (task.isCompleted) Color(0xFFB0B8C8) else Color(0xFF1E293B),
        animationSpec = tween(durationMillis = 200),
        label = "titleColor",
    )
    val condensedSemiBold = remember {
        FontFamily(
            Font(
                R.font.roboto_condensed_regular,
                FontWeight.SemiBold
            )
        )
    }
    val condensedNormal = remember {
        FontFamily(
            Font(
                R.font.roboto_condensed_regular,
                FontWeight.Normal
            )
        )
    }

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
            // Priority strip
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(72.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(task.priority.stripColor),
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Checkbox
            Box(
                Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (task.isCompleted) task.priority.stripColor
                        else Color.Transparent
                    )
                    .then(
                        if (!task.isCompleted) Modifier.background(
                            Color.Transparent
                        ) else Modifier
                    )
                    .clickable { onToggleComplete(task.id) }, Alignment.Center
            ) {
                // Uncompleted ring
                if (!task.isCompleted) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent),
                    )
                    Surface(
                        modifier = Modifier.size(24.dp),
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.5.dp,
                            color = Color(0xFFCBD5E1),
                        ),
                    ) {}
                }

                // Checkmark
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

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontFamily = condensedSemiBold,
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
                    // Clock icon (simple text substitute; swap with painterResource if available)
                    Text(
                        text = "⏱",
                        fontSize = 16.sp,
                        color = Color(0xFF94A3B8),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.time,
                        fontFamily = condensedNormal,
                        color = Color(0xFF94A3B8),
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Priority chip
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
    val _progress = 50f
    val progress by remember { mutableFloatStateOf(_progress ?: 0f) }
    val startAngle by remember { mutableFloatStateOf(0f) }
    val progressBarWidth by remember { mutableStateOf(10.dp) }
    val backgroundProgressBarWidth by remember { mutableStateOf(12.dp) }
    val roundBorder by remember { mutableStateOf(true) }
    val animProgress by animateFloatAsState(progress)
    val condensedSemiBold = remember {
        FontFamily(Font(R.font.roboto_condensed_regular, FontWeight.SemiBold))
    }
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
                backgroundProgressBarColor = Color(0xFF94A3B8),
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
                    fontFamily = condensedSemiBold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "No tasks for today yet",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = condensedSemiBold,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
fun CardSection() {
    val condensedSemiBold = remember {
        FontFamily(Font(R.font.roboto_condensed_regular, FontWeight.SemiBold))
    }

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
                    CardTaskSection(card, condensedSemiBold)
                }
            }
        }
    }
}

@Composable
fun RowScope.CardTaskSection(
    data: TaskCardData,
    semiBoldFont: FontFamily,
) {
    val normalFont = remember {
        FontFamily(Font(R.font.roboto_condensed_regular, FontWeight.Normal))
    }

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
                text = data.title.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.titleMedium,
                fontFamily = semiBoldFont,
                color = data.titleColor
            )
            Text(
                text = data.taskCount.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = semiBoldFont,
                color = data.taskCountColor
            )
            Text(
                text = data.taskMsg,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = normalFont,
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
        // User Info
        Text(
            text = "$greeting, ${user?.displayName?.split(" ")[0] ?: "User"} 👋",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_regular,
                    FontWeight.SemiBold
                )
            ),
            color = Color(0xFF1A1625)
        )
        Text(
            text = currentDate,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_regular,
                    FontWeight.Normal
                )
            ),
            color = Color(0xFF64748B)
        )
    }
}

// Centralised card definitions
private fun getTaskCards() = listOf(
    listOf(
        TaskCardData(
            "Today",
            0,
            "Total Tasks",
            Color(0xFFFFFFFF),
            Color(0xFF4F46E5),
            Color(0xFF1E293B),
            Color(0xFF94A3B8)
        ),
        TaskCardData(
            "Completed",
            0,
            "Well done!",
            Color(0xFFFFFFFF),
            Color(0xFF059669),
            Color(0xFF1E293B),
            Color(0xFF94A3B8)
        ),
    ),
    listOf(
        TaskCardData(
            "Pending",
            0,
            "To be done",
            Color(0xFFFFFFFF),
            Color(0xFFD97706),
            Color(0xFF1E293B),
            Color(0xFF94A3B8)
        ),
        TaskCardData(
            "Overdue",
            0,
            "Action required",
            Color(0xFFFEE2E2),
            Color(0xFFDC2626),
            Color(0xFFB91C1C),
            Color(0xFFF87171)
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
            // Calendar Icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF3B82F6),
                                Color(0xFF2563EB)
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

            // Date Text
            Column {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1A1625)
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

// ═══════════════════════════════════════════════════════════════════════════
// PREVIEW
// ═══════════════════════════════════════════════════════════════════════════

@Preview(showBackground = true)
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




