package com.jrprofessor.mindolist.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.*
import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.viewmodels.*
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showMonthYearPicker by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MindoListTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            AnalyticsHeader(onCalendarClick = { showMonthYearPicker = true })
            
            Spacer(modifier = Modifier.height(24.dp))
            
            AnalyticsFilterTabs(
                selectedFilter = state.selectedFilter,
                onFilterSelected = { viewModel.setFilter(it) }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            CompletionRateCard(
                percentage = state.completionPercentage,
                rangeLabel = state.rangeLabel
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TasksCompletedChartCard(
                data = state.chartData,
                title = "Tasks completed / ${state.selectedFilter.name.lowercase()} — ${state.selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }}"
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            StatsRow(
                streak = state.longestStreak,
                totalTasks = state.totalTasks,
                rangeLabel = state.rangeLabel
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            CategoryBreakdownSection(
                stats = state.categoryProgress,
                monthName = state.selectedDate.month.name
            )
            
            Spacer(modifier = Modifier.height(100.dp)) // Bottom nav padding
        }
    }

    if (showMonthYearPicker) {
        MonthYearPickerDialog(
            initialYear = state.selectedDate.year,
            initialMonth = state.selectedDate.monthNumber,
            onDismiss = { showMonthYearPicker = false },
            onMonthYearSelected = { year, month ->
                viewModel.setDate(LocalDate(year, month, 1))
                showMonthYearPicker = false
            }
        )
    }
}

@Composable
fun AnalyticsHeader(onCalendarClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Analytics",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MindoListTheme.colors.textPrimary,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Surface(
            onClick = onCalendarClick,
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(12.dp),
            color = MindoListTheme.colors.cardChildBg,
            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Select Month",
                    tint = MindoListTheme.colors.textPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun AnalyticsFilterTabs(
    selectedFilter: AnalyticsFilter,
    onFilterSelected: (AnalyticsFilter) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnalyticsFilter.entries.forEach { filter ->
            val isSelected = selectedFilter == filter
            Surface(
                onClick = { onFilterSelected(filter) },
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

@Composable
fun CompletionRateCard(percentage: Int, rangeLabel: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(modifier = Modifier.size(80.dp)) {
                CircularProgressBar(
                    progress = percentage.toFloat(),
                    progressBarColor = MindoListTheme.colors.success,
                    progressBarWidth = 10.dp,
                    backgroundProgressBarColor = MindoListTheme.colors.cardChildBg,
                    backgroundProgressBarWidth = 10.dp,
                    roundBorder = true,
                    isHide = true,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column {
                Text(
                    text = "$percentage%",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MindoListTheme.colors.textPrimary,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Completion rate\n$rangeLabel",
                    fontSize = 14.sp,
                    color = MindoListTheme.colors.textSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun TasksCompletedChartCard(data: List<ChartDataPoint>, title: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MindoListTheme.colors.textSecondary
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                data.forEach { point ->
                    BarChartItem(point, maxValue = (data.maxOfOrNull { it.value } ?: 10f).coerceAtLeast(5f))
                }
            }
        }
    }
}

@Composable
fun BarChartItem(point: ChartDataPoint, maxValue: Float) {
    val heightFactor = if (maxValue > 0) point.value / maxValue else 0f
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .fillMaxHeight(heightFactor.coerceAtLeast(0.1f)) // Show at least a small bar
                .clip(RoundedCornerShape(8.dp))
                .background(if (point.isHighlighted) MindoListTheme.colors.accent else MindoListTheme.colors.cardChildBg)
        )
        Text(
            text = point.label,
            fontSize = 12.sp,
            color = MindoListTheme.colors.textSecondary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatsRow(streak: Int, totalTasks: Int, rangeLabel: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            value = "$streak days",
            label = "Longest streak",
            valueColor = MindoListTheme.colors.success,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            value = "$totalTasks",
            label = "Tasks $rangeLabel",
            valueColor = MindoListTheme.colors.accent,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(value: String, label: String, valueColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = MindoListTheme.colors.textSecondary
            )
        }
    }
}

@Composable
fun CategoryBreakdownSection(stats: List<CategoryStats>, monthName: String) {
    Column {
        Text(
            text = "BY CATEGORY — $monthName",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MindoListTheme.colors.textSecondary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            stats.forEach { stat ->
                val category = Category.entries.find { it.label == stat.name } ?: Category.PERSONAL
                CategoryStatItem(
                    name = stat.name,
                    percentage = (stat.progress * 100).toInt(),
                    color = category.iconColor
                )
            }
        }
    }
}

@Composable
fun CategoryStatItem(name: String, percentage: Int, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = name,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MindoListTheme.colors.textPrimary,
            modifier = Modifier.width(80.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            CustomLinearProgressBar(
                progress = percentage.toFloat() / 100f,
                progressBarColor = color,
                backgroundProgressBarColor = MindoListTheme.colors.cardChildBg,
                progressBarHeight = 6.dp,
                backgroundProgressBarHeight = 6.dp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "$percentage%",
            fontSize = 14.sp,
            color = MindoListTheme.colors.textSecondary,
            modifier = Modifier.width(36.dp)
        )
    }
}
