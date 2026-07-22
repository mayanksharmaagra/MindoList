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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.MonthYearPickerDialog
import com.jrprofessor.mindolist.customView.TaskItem
import com.jrprofessor.mindolist.customView.TasksEmptyState
import com.jrprofessor.mindolist.extension.generateMonthDates
import com.jrprofessor.mindolist.extension.isToday
import com.jrprofessor.mindolist.extension.toMonthYearLabel
import com.jrprofessor.mindolist.extension.today
import com.jrprofessor.mindolist.model.Filter
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.presentation.dashboard.DashboardAction
import com.jrprofessor.mindolist.presentation.dashboard.DashboardEvent
import com.jrprofessor.mindolist.presentation.dashboard.DashboardState
import com.jrprofessor.mindolist.theme.PrimaryBlue
import com.jrprofessor.mindolist.theme.TextDark
import com.jrprofessor.mindolist.theme.TextGray
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@Composable
fun AllTaskScreen(
    dashboardViewModel: DashboardViewModel = koinViewModel(),
) {
    val state by dashboardViewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        dashboardViewModel.dispatch(DashboardAction.DateByTask(today()))
    }
    LaunchedEffect(Unit) {
        // all tasks
        dashboardViewModel.event.collect { event ->
            when (event) {
                is DashboardEvent.Error -> showToast(event.message)
                is DashboardEvent.TaskCompleted -> showToast("Task completed ✓")
                is DashboardEvent.TaskDeleted -> showToast("Task deleted ✓")
            }
        }
    }
    var showMonthYearDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        println(">>> " + state.isLoading)
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = PrimaryBlue
            )
        } else {
            AllTaskScreenContent(
                state = state,
                onDateSelected = { date ->
                    dashboardViewModel.dispatch(DashboardAction.SelectedDate(date))
                }, markCompleted = { id, isComplete ->
                    dashboardViewModel.dispatch(DashboardAction.MarkComplete(id, isComplete))
                },
                onFilterSelected = { filter ->
                    dashboardViewModel.dispatch(DashboardAction.FilterSelected(filter))
                },
                onCalendarClick = {
                    showMonthYearDialog = true
                }
            )
        }

        if (showMonthYearDialog) {
            MonthYearPickerDialog(
                initialYear = state.selectedDate.year,
                initialMonth = state.selectedDate.month.number,
                onDismiss = { showMonthYearDialog = false },
                onMonthYearSelected = { year, month ->
                    val newDate = LocalDate(year, month, 1)
                    dashboardViewModel.dispatch(DashboardAction.SelectedDate(newDate))
                    showMonthYearDialog = false
                }
            )
        }
    }
}

@Composable
fun AllTaskScreenContent(
    state: DashboardState,
    onDateSelected: (LocalDate) -> Unit,
    markCompleted: (String, Boolean) -> Unit,
    onFilterSelected: (String) -> Unit,
    onCalendarClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        SmartWeeklyCalendar(
            selectedDate = state.selectedDate,
            onDateSelected = { date ->
                onDateSelected(date)
            }, onCalendarClick = {
                onCalendarClick()
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
            return
        }
        FilterRow(
            tasks = state.tasks,           // ← count auto calculate hoga
            selectedFilter = state.selectedFilter,
            onFilterSelected = { filter ->
                onFilterSelected(filter)
            }
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Empty state
        if (state.tasks.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                TasksEmptyState(isToday = state.selectedDate.isToday())
            }
            return
        }

        // Task list
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = state.tasks,
                key = { it.id },
            ) { task ->
                TaskItem(
                    task = task,
                    onToggleComplete = { isCompleted ->
                        markCompleted(task.id, isCompleted)
                    }
                )
            }
        }
    }
}

@Composable
fun SmartWeeklyCalendar(
    selectedDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    onDateSelected: (LocalDate) -> Unit,
    onCalendarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var displayedYear by remember(selectedDate) {  // ← selectedDate key add karo
        mutableIntStateOf(selectedDate.year)
    }
    var displayedMonth by remember(selectedDate) { // ← selectedDate key add karo
        mutableStateOf(selectedDate.month)
    }

    val monthDates = remember(displayedYear, displayedMonth) {
        generateMonthDates(displayedYear, displayedMonth)
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Month change hone pe → selected date ya today pe scroll karo
    LaunchedEffect(displayedMonth, displayedYear) {
        val targetIndex = monthDates.indexOfFirst { it == selectedDate }.takeIf { it >= 0 }
            ?: monthDates.indexOfFirst { it.isToday() }.takeIf { it >= 0 } ?: 0
        listState.animateScrollToItem(targetIndex)
    }

    // ── Prev / Next month helpers ─────────────────────────────────────────────
    fun goToPrevMonth() {
        val prev = LocalDate(displayedYear, displayedMonth, 1).minus(1, DateTimeUnit.MONTH)
        displayedYear = prev.year
        displayedMonth = prev.month
    }

    fun goToNextMonth() {
        val next = LocalDate(displayedYear, displayedMonth, 1).plus(1, DateTimeUnit.MONTH)
        displayedYear = next.year
        displayedMonth = next.month
    }

    Column(modifier = modifier.fillMaxWidth()) {

        // ── Header: Arrow + Month Year + Arrow + Calendar icon ────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left arrow
            IconButton(onClick = { goToPrevMonth() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous month",
                    tint = TextDark,
                )
            }

            // Month + Year
            Text(
                text = LocalDate(displayedYear, displayedMonth, 1).toMonthYearLabel(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
            )

            // Right arrow
            IconButton(onClick = { goToNextMonth() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next month",
                    tint = TextDark,
                )
            }

            // Calendar icon
            IconButton(onClick = onCalendarClick) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = "Full calendar",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ── Scrollable day pills ──────────────────────────────────────────────
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(
                items = monthDates,
                key = { it.toString() },
            ) { date ->
                DayPill(
                    date = date,
                    isSelected = date == selectedDate,
                    onClick = {
                        onDateSelected(date)
                        if (date.month != displayedMonth || date.year != displayedYear) {
                            displayedYear = date.year
                            displayedMonth = date.month
                        }
                    },
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
                text = date.day.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = numberColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun FilterItem(
    filter: Filter,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) PrimaryBlue else Color(0xFFDEE8FF),
        animationSpec = tween(200),
        label = "filterBg",
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF94A3B8),
        animationSpec = tween(200),
        label = "filterText",
    )
    val badgeBg by animateColorAsState(
        targetValue = /*if (isSelected) */Color.White.copy(alpha = 0.25f),//else Color(0xFFE2E8F0)
        animationSpec = tween(200),
        label = "badgeBg",
    )
    val badgeText by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF64748B),
        animationSpec = tween(200),
        label = "badgeText",
    )

    Card(
        modifier = Modifier
            .height(40.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(
                horizontal = 14.dp,
                vertical = 4.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            // Filter label
            Text(
                text = filter.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() },
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor,
            )

            // Count badge — sirf tab dikhao jab count > 0
            if (count > 0) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(badgeBg)
                        .padding(horizontal = 8.dp, vertical = 1.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = count.toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeText,
                    )
                }
            }
        }
    }
}
// ── Filter Row ────────────────────────────────────────────────────────────────

@Composable
fun FilterRow(
    tasks: List<TaskModel>,                  // count calculate karne ke liye
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Count per filter
    val counts = mapOf(
        Filter.ALL to tasks.size,
        Filter.PENDING to tasks.count { !it.isCompleted },
        Filter.COMPLETED to tasks.count { it.isCompleted },
        Filter.OVERDUE to tasks.count {
            !it.isCompleted &&
                    it.dueDate < Clock.System.now().toEpochMilliseconds()
        },
    )

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
    ) {
        items(Filter.entries) { filter ->
            FilterItem(
                filter = filter,
                count = counts[filter] ?: 0,
                isSelected = selectedFilter == filter.name,
                onClick = { onFilterSelected(filter.name) },
            )
        }
    }
}
/*
@Composable
fun DayPill(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val isToday = date.isToday()
    val isPast = date.isPast()

    val bgColor by animateColorAsState(
        targetValue = when {
            isSelected -> PrimaryBlue
            isToday    -> Color(0xFFEDE9FE)
            isPast     -> Color(0xFFF1F5F9)
            else       -> Color(0xFFF1F5F9)
        },
        animationSpec = tween(250),
        label = "pillBg",
    )

    val dayNameColor by animateColorAsState(
        targetValue = when {
            isSelected -> Color.White.copy(alpha = 0.85f)
            isToday    -> PrimaryBlue
            isPast     -> Color(0xFF94A3B8).copy(alpha = 0.5f)
            else       -> Color(0xFF94A3B8)
        },
        animationSpec = tween(250),
        label = "dayNameColor",
    )

    val numberColor by animateColorAsState(
        targetValue = when {
            isSelected -> Color.White
            isToday    -> PrimaryBlue
            isPast     -> TextDark.copy(alpha = 0.35f)
            else       -> TextDark
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
            Text(
                text = date.dayOfWeek.name.take(3),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = dayNameColor,
                textAlign = TextAlign.Center,
            )
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = numberColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}*/
