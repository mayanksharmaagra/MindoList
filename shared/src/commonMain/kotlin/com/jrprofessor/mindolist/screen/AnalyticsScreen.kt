package com.jrprofessor.mindolist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.CircularProgressBar
import com.jrprofessor.mindolist.icons.IcHealth
import com.jrprofessor.mindolist.icons.IcPersonal
import com.jrprofessor.mindolist.icons.IcWork
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.viewmodels.AnalyticsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .padding(bottom = 80.dp) // Bottom nav padding
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Analytics",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TitleColor
                )
                Text(
                    text = "Your performance overview",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGray
                )
            }
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(lightPurple)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Calendar",
                    tint = primaryPurple
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Segmented Control (Capsule)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFFF8FAFC)) // Lighter background
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf("Monthly", "Weekly", "Daily").forEach { tab ->
                val isSelected = state.selectedPeriod == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .then(
                            if (isSelected) {
                                Modifier
                                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(28.dp))
                                    .background(Color.White, shape = RoundedCornerShape(28.dp))
                            } else {
                                Modifier
                            }
                        )
                        .clickable { viewModel.setPeriod(tab) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) primaryPurple else Color(0xFF64748B),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main Circular Progress
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressBar(
                modifier = Modifier.size(240.dp),
                progress = state.completionPercentage.toFloat(),
                progressBarColor = primaryPurple,
                progressBarWidth = 24.dp,
                backgroundProgressBarColor = lightPurple.copy(alpha = 0.5f),
                backgroundProgressBarWidth = 24.dp,
                roundBorder = true
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${state.completionPercentage}%",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TitleColor
                )
                Text(
                    text = "COMPLETED",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextGray,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Trend Badge
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(20.dp))
                .background(ExpenseGreen.copy(alpha = 0.1f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = state.trendMessage,
                    color = ExpenseGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Stats Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                label = "Total Tasks",
                value = "${state.totalTasks}",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Current Streak",
                value = "${state.currentStreak} Days",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Top Categories
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Top Categories",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TitleColor
            )
            TextButton(onClick = { }) {
                Text(text = "View All", color = primaryPurple, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.categoryProgress.isEmpty()) {
            Text(
                text = "No category data available",
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray
            )
        } else {
            state.categoryProgress.forEach { stats ->
                CategoryProgressItem(
                    name = stats.name,
                    progress = stats.progress,
                    color = when (stats.name) {
                        "Work" -> primaryPurple
                        "Shopping" -> ExpenseGreen
                        else -> primaryPurple
                    },
                    icon = when (stats.name) {
                        "Work" -> IcWork
                        "Health" -> IcHealth
                        else -> IcPersonal
                    }
                )
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(text = label, color = TextGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TitleColor)
        }
    }
}

@Composable
fun CategoryProgressItem(name: String, progress: Float, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = name, fontWeight = FontWeight.Bold, color = TitleColor)
            }
            Text(text = "${(progress * 100).toInt()}%", fontWeight = FontWeight.Bold, color = TitleColor)
        }
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = color,
            trackColor = SegmentBackground,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}
