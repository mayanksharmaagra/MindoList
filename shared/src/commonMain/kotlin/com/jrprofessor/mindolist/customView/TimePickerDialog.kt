package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.jrprofessor.mindolist.theme.*

@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    period: String,
    onTimeSelected: (hour: String, minute: String, period: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val hours = (1..12).toList()
    val minutes = (0..59).step(5).toList() 
    
    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }
    var selectedPeriod by remember { mutableStateOf(period) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
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
                        text = "Due time",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MindoListTheme.colors.textPrimary,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        onClick = onDismiss,
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
                val quickPicks = listOf("Now", "Morning", "Afternoon", "No time")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    quickPicks.forEach { label ->
                        val isSelected = label == "Morning" 
                        val chipBg = if (isSelected) MindoListTheme.colors.accent.copy(alpha = 0.1f) else MindoListTheme.colors.cardChildBg
                        val border = if (isSelected) BorderStroke(1.dp, MindoListTheme.colors.accent) else BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.05f))

                        Surface(
                            onClick = { /* Handle quick pick */ },
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

                Spacer(modifier = Modifier.height(48.dp))

                // ── Time Selection ─────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Hour Picker
                        TimeScrollPicker(
                            items = hours,
                            selectedItem = selectedHour,
                            onItemSelected = { selectedHour = it },
                            label = { it.toString().padStart(2, '0') }
                        )

                        Text(
                            ":",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MindoListTheme.colors.textPrimary
                        )

                        // Minute Picker
                        TimeScrollPicker(
                            items = minutes,
                            selectedItem = selectedMinute,
                            onItemSelected = { selectedMinute = it },
                            label = { it.toString().padStart(2, '0') }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // AM/PM Vertical Stack
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            AmPmButton(
                                label = "AM",
                                isSelected = selectedPeriod == "AM",
                                onClick = { selectedPeriod = "AM" }
                            )
                            AmPmButton(
                                label = "PM",
                                isSelected = selectedPeriod == "PM",
                                onClick = { selectedPeriod = "PM" }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // ── Confirm Button ────────────────────────────────────────────
                Button(
                    onClick = {
                        onTimeSelected(
                            selectedHour.toString().padStart(2, '0'),
                            selectedMinute.toString().padStart(2, '0'),
                            selectedPeriod
                        )
                    },
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
                        text = "Confirm time — ${selectedHour.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')} $selectedPeriod",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AmPmButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(width = 64.dp, height = 48.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MindoListTheme.colors.accent else MindoListTheme.colors.cardChildBg,
        border = if (isSelected) null else BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.05f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.Black else MindoListTheme.colors.textPrimary
            )
        }
    }
}

@Composable
fun <T> TimeScrollPicker(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    label: (T) -> String
) {
    val itemHeight = 64.dp
    val visibleCount = 5
    
    // Infinite scroll
    val repeatCount = 100
    val infiniteItems = List(repeatCount) { items }.flatten()
    val startIndex = (repeatCount / 2) * items.size + items.indexOf(selectedItem)
    
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex - (visibleCount / 2))
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LaunchedEffect(listState.firstVisibleItemIndex) {
        val centerIndex = listState.firstVisibleItemIndex + (visibleCount / 2)
        val item = infiniteItems.getOrNull(centerIndex)
        if (item != null && item != selectedItem) {
            onItemSelected(item)
        }
    }

    Box(
        modifier = Modifier
            .width(100.dp)
            .height(itemHeight * visibleCount),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight),
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent,
            border = BorderStroke(1.dp, MindoListTheme.colors.accent)
        ) {}

        LazyColumn(
            state = listState,
            flingBehavior = snapBehavior,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 0.dp)
        ) {
            items(infiniteItems.size) { index ->
                val centerIndex = listState.firstVisibleItemIndex + (visibleCount / 2)
                val isSelected = index == centerIndex
                
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label(infiniteItems[index]),
                        fontSize = if (isSelected) 32.sp else 24.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MindoListTheme.colors.textPrimary else MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
                        style = if (isSelected) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
