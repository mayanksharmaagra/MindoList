package com.jrprofessor.mindolist.screen

//import mindolist.shared.generated.resources.roboto_condensed_regular
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.TaskItem
import com.jrprofessor.mindolist.customView.TasksEmptyState
import com.jrprofessor.mindolist.presentation.DashboardEvent
import com.jrprofessor.mindolist.presentation.DashboardState
import com.jrprofessor.mindolist.theme.PrimaryBlue
import com.jrprofessor.mindolist.theme.TextDark
import com.jrprofessor.mindolist.theme.TextGray
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.TaskViewModel
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun AllTaskScreen(
    viewModel: TaskViewModel = koinViewModel(),
) {
    val state by viewModel.taskState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTasks(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )           // all tasks
        viewModel.eventTask.collect { event ->
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
            AllTaskScreenContent(state = state) { date ->
                viewModel.loadTasks(date)
            }
        }
    }
}

@Composable
fun AllTaskScreenContent(state: DashboardState, onDateSelected: (LocalDate) -> Unit) {
    var selectedDate by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        WeeklyCalendarStrip(
            selectedDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
                onDateSelected(date)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (state.tasks.isEmpty()) {
            TasksEmptyState()
        } else {
            state.tasks.forEach { task ->
                TaskItem(
                    task = task,
                    onToggleComplete = { id ->

                    },
                )
            }
        }
    }
}

@Composable
fun WeeklyCalendarStrip(
    selectedDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var currentWeekStart by remember {
        mutableStateOf(
            selectedDate.minus(selectedDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
        )
    }

    val weekDays = remember(currentWeekStart) {
        (0..6).map { offset ->
            currentWeekStart.plus(offset, DateTimeUnit.DAY)
        }
    }

    val monthYearLabel = remember(currentWeekStart) {
        val month = currentWeekStart.month.name
            .lowercase().replaceFirstChar { it.uppercase() }
        "$month ${currentWeekStart.year}"
    }

    Column(modifier = modifier.fillMaxWidth()) {

        // ── Month + navigation ────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                currentWeekStart = currentWeekStart.minus(7, DateTimeUnit.DAY)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous week",
                    tint = TextDark,
                )
            }
            Text(
                text = monthYearLabel,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
            )
            IconButton(onClick = {
                currentWeekStart = currentWeekStart.plus(7, DateTimeUnit.DAY)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next week",
                    tint = TextDark,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Day pills ─────────────────────────────────────────────────────────
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(weekDays) { date ->
                DayPill(
                    date = date,
                    isSelected = date == selectedDate,
                    onClick = { onDateSelected(date) },
                )
            }
        }
    }
}
// ── Day Pill ──────────────────────────────────────────────────────────────────

@Composable
fun DayPill(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

    val isToday = date == today
//    val isPast = date < today   // ← add karo

    val bgColor by animateColorAsState(
        targetValue = when {
            isSelected -> PrimaryBlue
//            isPast -> Color(0xFFF1F5F9)  // ← past = lighter grey
            else -> Color(0xFFF8F9FE)
        },
        animationSpec = tween(250),
        label = "pillBg",
    )
    val dayNameColor by animateColorAsState(
        targetValue = when {
            isSelected -> Color.White.copy(alpha = 0.85f)
            isToday -> PrimaryBlue
//            isPast -> TextGray.copy(alpha = 0.4f)
            else -> TextGray
        },
        animationSpec = tween(250),
        label = "dayNameColor",
    )
    val numberColor by animateColorAsState(
        targetValue = when {
            isSelected -> Color.White
            isToday    -> PrimaryBlue
//            isPast     -> TextGray.copy(alpha = 0.4f)  // ← past = faded
            else       -> TextGray
        },
        animationSpec = tween(250),
        label = "numberColor",
    )

    Surface(
        modifier = Modifier
            .width(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            // MON, TUE, WED...
            Text(
                text = date.dayOfWeek.name.take(3),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = dayNameColor,
                textAlign = TextAlign.Center,
            )
            // 12, 13, 14...
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = numberColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ── Data ──────────────────────────────────────────────────────────────────────

data class CalendarDay(
    val date: LocalDate,
    val dayLetter: String,   // "S", "M", "T"...
    val dayNumber: Int,
)