package com.jrprofessor.mindolist.customView


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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

// ── Time Picker Dialog ────────────────────────────────────────────────────────

@Composable
fun TimePickerDialog(
    initialHour: Int = 11,
    initialMinute: Int = 30,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val hours = (1..12).toList()
    val minutes = (0..59).toList()
    val periods = listOf("AM", "PM")

    var selectedHour by remember { mutableIntStateOf(initialHour) }
    var selectedMinute by remember { mutableIntStateOf(initialMinute) }
    var selectedPeriod by remember { mutableStateOf(if (initialHour >= 12) "PM" else "AM") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // ── Header ────────────────────────────────────────────────────
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFF1E293B),
                        )
                    }
                    Text(
                        text = "Choose Time",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ── Scroll Picker ─────────────────────────────────────────────
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    // Selected highlight pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFEDE9FE)),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Hour picker
                        ScrollPicker(
                            items = hours,
                            selectedItem = selectedHour,
                            onItemSelected = { selectedHour = it },
                            label = { it.toString().padStart(2, '0') },
                            modifier = Modifier.width(80.dp),
                        )

                        Text(
                            text = ":",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7C3AED),
                            modifier = Modifier.padding(bottom = 4.dp),
                        )

                        // Minute picker
                        ScrollPicker(
                            items = minutes,
                            selectedItem = selectedMinute,
                            onItemSelected = { selectedMinute = it },
                            label = { it.toString().padStart(2, '0') },
                            modifier = Modifier.width(80.dp),
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // AM/PM picker
                        ScrollPicker(
                            items = periods,
                            selectedItem = selectedPeriod,
                            onItemSelected = { selectedPeriod = it },
                            label = { it },
                            modifier = Modifier.width(64.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hint text
                Text(
                    text = "Scroll to select the reminder time",
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8),
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ── Set Time Button ───────────────────────────────────────────
                Button(
                    onClick = {
                        val hour24 = if (selectedPeriod == "PM" && selectedHour != 12) selectedHour + 12
                        else if (selectedPeriod == "AM" && selectedHour == 12) 0
                        else selectedHour
                        onTimeSelected(hour24, selectedMinute)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Set Time",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // ── Cancel Button ─────────────────────────────────────────────
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                    elevation = ButtonDefaults.buttonElevation(0.dp),
                ) {
                    Text(
                        text = "Cancel",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                    )
                }
            }
        }
    }
}

// ── Generic Scroll Picker ─────────────────────────────────────────────────────

@Composable
fun <T> ScrollPicker(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    label: (T) -> String,
    modifier: Modifier = Modifier,
) {
    val visibleCount = 5
    val itemHeightDp = 52.dp
    val scope = rememberCoroutineScope()

    // Infinite scroll — repeat list many times
    val repeatCount = 100
    val infiniteItems = List(repeatCount) { items }.flatten()
    val startIndex = repeatCount / 2 * items.size + items.indexOf(selectedItem)

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex - 2)
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Detect selected item on scroll
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val centerIndex = listState.firstVisibleItemIndex + 2
        val item = infiniteItems.getOrNull(centerIndex)
        if (item != null && item != selectedItem) {
            onItemSelected(item)
        }
    }

    Box(
        modifier = modifier.height(itemHeightDp * visibleCount),
        contentAlignment = Alignment.Center,
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = snapBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(infiniteItems.size) { index ->
                val centerIndex = listState.firstVisibleItemIndex + 2
                val isSelected = index == centerIndex

                Box(
                    modifier = Modifier
                        .height(itemHeightDp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = label(infiniteItems[index]),
                        fontSize = if (isSelected) 26.sp else 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Color(0xFF7C3AED) else Color(0xFFCBD5E1),
                    )
                }
            }
        }
    }
}