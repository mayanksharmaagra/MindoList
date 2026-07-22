package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


// -- helper class for date-------------
private val PrimaryBlue = Color(0xFF3B82F6)
data class SimpleDate(
    val year: Int,
    val month: Int,   // 1-12
    val day: Int
){
    fun plusDays(days: Int): SimpleDate {
        var date = LocalDate(year, month, day).plus(days, DateTimeUnit.DAY)
        return SimpleDate(date.year, date.month.number, date.day)
    }

    fun plusWeeks(weeks: Int): SimpleDate = plusDays(weeks * 7)

    fun isBefore(other: SimpleDate): Boolean {
        return LocalDate(year, month, day) < LocalDate(other.year, other.month, other.day)
    }

    fun daysInMonth(): Int = LocalDate(year, month, 1)
        .plus(1, DateTimeUnit.MONTH)
        .minus(1, DateTimeUnit.DAY)
        .dayOfMonth

    // 0 = Sunday, 1 = Monday ... 6 = Saturday
    fun firstDayOfWeekOffset(): Int {
        val dayOfWeek = LocalDate(year, month, 1).dayOfWeek.isoDayNumber % 7
        return dayOfWeek
    }

    fun monthName(): String = when (month) {
        1 -> "January"; 2 -> "February"; 3 -> "March"
        4 -> "April"; 5 -> "May"; 6 -> "June"
        7 -> "July"; 8 -> "August"; 9 -> "September"
        10 -> "October"; 11 -> "November"; else -> "December"
    }

    fun dayOfWeekName(): String = when (LocalDate(year, month, day).dayOfWeek) {
        DayOfWeek.MONDAY -> "Monday"; DayOfWeek.TUESDAY -> "Tuesday"
        DayOfWeek.WEDNESDAY -> "Wednesday"; DayOfWeek.THURSDAY -> "Thursday"
        DayOfWeek.FRIDAY -> "Friday"; DayOfWeek.SATURDAY -> "Saturday"
        else -> "Sunday"
    }

    fun prevMonth(): SimpleDate {
        val date = LocalDate(year, month, 1).minus(1, DateTimeUnit.MONTH)
        return SimpleDate(date.year, date.month.number, date.dayOfMonth)
    }

    fun nextMonth(): SimpleDate {
        val date = LocalDate(year, month, 1).plus(1, DateTimeUnit.MONTH)
        return SimpleDate(date.year, date.month.number, date.dayOfMonth)
    }

    fun formatted(): String =
        "${dayOfWeekName()}, ${monthName()} $day/*${getDaySuffix(day)}*/"

    fun monthYearFormatted(): String = "${monthName()} $year"
    fun monthYearDayFormatted(): String = "$day/${month.toString().padStart(2, '0')}/$year"
}

@OptIn(ExperimentalTime::class)
fun today(): SimpleDate {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return SimpleDate(now.year, now.month.number, now.dayOfMonth)
}
// ── Date Picker Full Screen Dialog ────────────────────────────────────────────

@Composable
fun DatePickerDialog(
    onDateSelected: (SimpleDate) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            /*decorFitsSystemWindows = false,*/
        )
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

// ── Date Picker Screen ────────────────────────────────────────────────────────

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
        "Next Week" to todayDate.plusWeeks(1),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
    ) {
        // ── Top Bar ───────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                )
            }
            Text(
                text = "Pick a Date",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                modifier = Modifier.align(Alignment.Center),
            )
        }

        // ── Quick Pick ────────────────────────────────────────────────────────
        Text(
            text = "QUICK PICK",
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF94A3B8),
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        )

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            quickPicks.forEach { (label, date) ->
                val isSelected = selectedDate == date
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            if (isSelected) PrimaryBlue else Color.White
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color.Transparent else Color(0xFFE2E8F0),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .clickable {
                            selectedDate = date
                            currentMonth = SimpleDate(date.year, date.month, 1)
                        }
                        .padding(horizontal = 18.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) Color.White else Color(0xFF64748B),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Calendar Card ─────────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Month navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { currentMonth = currentMonth.prevMonth() }) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Prev",
                            tint = Color(0xFF64748B),
                        )
                    }
                    Text(
                        text =  currentMonth.monthYearFormatted(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                    )
                    IconButton(onClick = { currentMonth = currentMonth.nextMonth() }) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next",
                            tint = Color(0xFF64748B),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Day headers
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF94A3B8),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar days
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

                            val isSelected = date == selectedDate
                            val isToday = date == todayDate
                            val isPast = date != null && date.isBefore(todayDate)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> PrimaryBlue
                                            else -> Color.Transparent
                                        }
                                    )
                                    .then(
                                        if (date != null && !isPast)
                                            Modifier.clickable {
                                                selectedDate = date
                                            } else Modifier
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (date != null) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = dayIndex.toString(),
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                            color = when {
                                                isSelected -> Color.White
                                                isPast -> Color(0xFFCBD5E1)
                                                isToday -> PrimaryBlue
                                                else -> Color(0xFF1E293B)
                                            },
                                        )
                                        // Today dot
                                        if (isToday && !isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(4.dp)
                                                    .clip(CircleShape)
                                                    .background(PrimaryBlue)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Selected Date Display ─────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0x0D3B82F6))
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(22.dp),
            )
            Column {
                Text(
                    text = "SELECTED DATE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.sp,
                )
                Text(
                    text = selectedDate.monthYearDayFormatted(),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ── Save Button ───────────────────────────────────────────────────────
        Button(
            onClick = { onDateSelected(selectedDate) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .height(56.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
        ) {
            Text(
                text = "Save Selection",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

// ── Day suffix helper ─────────────────────────────────────────────────────────

private fun getDaySuffix(day: Int): String = when {
    day in 11..13 -> "th"
    day % 10 == 1 -> "st"
    day % 10 == 2 -> "nd"
    day % 10 == 3 -> "rd"
    else -> "th"
}