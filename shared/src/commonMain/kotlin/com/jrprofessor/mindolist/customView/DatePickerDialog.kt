package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jrprofessor.mindolist.theme.*
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// -- helper class for date-------------
data class SimpleDate(
    val year: Int,
    val month: Int,   // 1-12
    val day: Int
) {
    fun plusDays(days: Int): SimpleDate {
        val date = LocalDate(year, month, day).plus(days, DateTimeUnit.DAY)
        return SimpleDate(date.year, date.month.number, date.dayOfMonth)
    }

    fun plusWeeks(weeks: Int): SimpleDate = plusDays(weeks * 7)

    fun isBefore(other: SimpleDate): Boolean {
        return LocalDate(year, month, day) < LocalDate(other.year, other.month, other.day)
    }

    fun daysInMonth(): Int = LocalDate(year, month, 1)
        .plus(1, DateTimeUnit.MONTH)
        .minus(1, DateTimeUnit.DAY)
        .dayOfMonth

    fun firstDayOfWeekOffset(): Int {
        return LocalDate(year, month, 1).dayOfWeek.isoDayNumber % 7
    }

    fun monthName(): String = when (month) {
        1 -> "January"; 2 -> "February"; 3 -> "March"
        4 -> "April"; 5 -> "May"; 6 -> "June"
        7 -> "July"; 8 -> "August"; 9 -> "September"
        10 -> "October"; 11 -> "November"; else -> "December"
    }

    fun prevMonth(): SimpleDate {
        val date = LocalDate(year, month, 1).minus(1, DateTimeUnit.MONTH)
        return SimpleDate(date.year, date.month.number, date.dayOfMonth)
    }

    fun nextMonth(): SimpleDate {
        val date = LocalDate(year, month, 1).plus(1, DateTimeUnit.MONTH)
        return SimpleDate(date.year, date.month.number, date.dayOfMonth)
    }

    fun monthYearFormatted(): String = "${monthName()} $year"
    fun monthYearDayFormatted(): String = "$day/${month.toString().padStart(2, '0')}/$year"
}

@OptIn(ExperimentalTime::class)
fun today(): SimpleDate {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return SimpleDate(now.year, now.month.number, now.dayOfMonth)
}

@Composable
fun DatePickerDialog(
    onDateSelected: (SimpleDate) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        DatePickerScreen(
            onDateSelected = {
                onDateSelected(it)
                onDismiss()
            },
            onBack = onDismiss,
        )
    }
}

@Composable
fun DatePickerScreen(
    onDateSelected: (SimpleDate) -> Unit,
    onBack: () -> Unit,
) {
    val todayDate = remember { today() }
    var selectedDate by remember { mutableStateOf(todayDate) }
    var currentMonth by remember { mutableStateOf(SimpleDate(todayDate.year, todayDate.month, 1)) }

    val quickPicks = listOf(
        "Today" to todayDate,
        "Tomorrow" to todayDate.plusDays(1),
        "Next week" to todayDate.plusWeeks(1),
        "No date" to null,
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MindoListTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // ── Header ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Due date",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary,
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Surface(
                    onClick = onBack,
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MindoListTheme.colors.cardChildBg,
                    border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MindoListTheme.colors.textPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Quick Pick ────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                quickPicks.forEach { (label, date) ->
                    val isSelected = date != null && selectedDate == date
                    val chipBg = if (isSelected) MindoListTheme.colors.accent.copy(alpha = 0.1f) else MindoListTheme.colors.cardChildBg
                    val border = if (isSelected) BorderStroke(1.dp, MindoListTheme.colors.accent) else BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.05f))

                    Surface(
                        onClick = {
                            if (date != null) {
                                selectedDate = date
                                currentMonth = SimpleDate(date.year, date.month, 1)
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = chipBg,
                        border = border
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MindoListTheme.colors.accent else MindoListTheme.colors.textSecondary,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Calendar Header ───────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currentMonth.monthYearFormatted(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        onClick = { currentMonth = currentMonth.prevMonth() },
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = MindoListTheme.colors.cardChildBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ChevronLeft, "Prev", tint = MindoListTheme.colors.textPrimary)
                        }
                    }
                    Surface(
                        onClick = { currentMonth = currentMonth.nextMonth() },
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = MindoListTheme.colors.cardChildBg
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ChevronRight, "Next", tint = MindoListTheme.colors.textPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Calendar Grid ─────────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth()) {
                // Day names
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val firstDayOffset = currentMonth.firstDayOfWeekOffset()
                val daysInMonth = currentMonth.daysInMonth()
                val totalCells = firstDayOffset + daysInMonth
                val rows = (totalCells + 6) / 7

                repeat(rows) { row ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        repeat(7) { col ->
                            val dayIndex = row * 7 + col - firstDayOffset + 1
                            val date = if (dayIndex in 1..daysInMonth)
                                SimpleDate(currentMonth.year, currentMonth.month, dayIndex) else null

                            val isSelected = date != null && date == selectedDate
                            val isToday = date != null && date == todayDate

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) MindoListTheme.colors.accent else Color.Transparent)
                                    .clickable(enabled = date != null) {
                                        if (date != null) selectedDate = date
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (date != null) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = dayIndex.toString(),
                                            fontSize = 18.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = when {
                                                isSelected -> MindoListTheme.colors.background
                                                date.month != currentMonth.month -> MindoListTheme.colors.textSecondary.copy(alpha = 0.3f)
                                                else -> MindoListTheme.colors.textPrimary
                                            }
                                        )
                                        if (isToday && !isSelected) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .clip(CircleShape)
                                                    .background(MindoListTheme.colors.success)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── Confirm Button ────────────────────────────────────────────
            Button(
                onClick = { onDateSelected(selectedDate) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MindoListTheme.colors.textPrimary,
                    contentColor = MindoListTheme.colors.background
                )
            ) {
                Text(
                    text = "Confirm date",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
