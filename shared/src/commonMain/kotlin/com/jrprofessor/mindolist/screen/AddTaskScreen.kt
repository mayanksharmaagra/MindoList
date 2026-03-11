package com.jrprofessor.mindolist.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.customView.DatePickerDialog
import com.jrprofessor.mindolist.customView.TimePickerDialog
import com.jrprofessor.mindolist.domain.repository.logger
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.util.logging.Logger
import org.jetbrains.compose.resources.painterResource

val TAG = "AddTaskScreen"
private val logger = KotlinLogging.logger {

}
// Colors
// Colors
private val CardBackground = Color(0xFFFFFFFF)
private val PurpleAccent = Color(0xFF6C3FC7)
private val TitleColor = Color(0xFF3A3A5C)
private val PlaceholderColor = Color(0xFFB0B3C6)

// Colors
private val SegmentBackground = Color(0xFFF1F5F9)
private val SelectedBackground = Color(0xFFFFFFFF)
private val UnselectedTextColor = Color(0xFF64748B)
private val CategoryUnselectedTextColor = Color(0xFF475569)
private val RemindViewBG = Color(0xFFF8FAFC)

enum class Importance { Low, Medium, High }

enum class ReminderOption(val label: String) {
    FIVE_MINUTES("5 minutes before"),
    TEN_MINUTES("10 minutes before"),
    FIFTEEN_MINUTES("15 minutes before"),
    THIRTY_MINUTES("30 minutes before"),
    ONE_HOUR("1 hour before"),
    TWO_HOURS("2 hours before"),
    ONE_DAY("1 day before"),
}


@Composable
fun AddTaskScreen() {
    var isEnabled by remember { mutableStateOf(true) }
    var selectedReminder by remember { mutableStateOf(ReminderOption.FIFTEEN_MINUTES) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("11:30 PM") }
    var selectedDate by remember { mutableStateOf("11:30 PM") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // ← add this
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        CustomToolBar()
        Spacer(modifier = Modifier.height(15.dp))
        IdentityView()
        Spacer(modifier = Modifier.height(15.dp))
        ScheduleView(onDateSelection = {

        }, onTimeSelection = {
            showTimePicker = true
        })
        Spacer(modifier = Modifier.height(15.dp))
        ImportanceView()
        Spacer(modifier = Modifier.height(15.dp))
        CategoryView({

        })
        Spacer(modifier = Modifier.height(15.dp))
        AlertsSection(
            isEnabled = isEnabled,
            selectedReminder = selectedReminder,
            onToggle = { isEnabled = it },
            onReminderSelected = { selectedReminder = it })
    }
    if (showTimePicker) {
        TimePickerDialog(
            initialHour = 11,
            initialMinute = 30,
            onTimeSelected = { hour, minute ->
                val period = if (hour >= 12) "PM" else "AM"
                val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
                selectedTime = "${displayHour.toString().padStart(2, '0')}:${
                    minute.toString().padStart(2, '0')
                } $period"
                showTimePicker = false
                logger.debug {
                    "AddTaskScreen: $selectedTime"
                }
            },
            onDismiss = { showTimePicker = false }
        )
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                selectedDate=date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                logger.debug { "AddTaskScreen: $selectedDate" }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun AlertsSection(
    isEnabled: Boolean,
    selectedReminder: ReminderOption,
    onToggle: (Boolean) -> Unit,
    onReminderSelected: (ReminderOption) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDropdown by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // ── Header row: Bell icon + Alerts label + Toggle ─────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = Res.drawable.ic_bell), // replace with your bell icon
                    contentDescription = "Alert",
                    tint = Color(0xFF7C3AED),
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Alerts",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = isEnabled, onCheckedChange = onToggle, colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF7C3AED),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFCBD5E1),
                    )
                )
            }

            // ── Remind me row (visible only when enabled) ─────────────────────
            AnimatedVisibility(
                visible = isEnabled,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        onClick = {
                            showDropdown = true
                        },
                        shape = RoundedCornerShape(30.dp),
                        color = RemindViewBG,
                        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Remind me",
                                fontSize = 14.sp,
                                color = Color(0xFF94A3B8),
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = selectedReminder.label,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF7C3AED),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown",
                                tint = Color(0xFF7C3AED),
                                modifier = Modifier.size(18.dp),
                            )
                        }

                    }

                    // Dropdown menu
                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        modifier = Modifier
                            .background(Color.White) // ← add this
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        ReminderOption.entries.forEach { option ->
                            DropdownMenuItem(text = {
                                Text(
                                    text = option.label,
                                    fontSize = 14.sp,
                                    fontWeight = if (option == selectedReminder) FontWeight.Bold else FontWeight.Normal,
                                    color = if (option == selectedReminder) Color(0xFF7C3AED) else Color(
                                        0xFF1E293B
                                    ),
                                )
                            }, onClick = {
                                onReminderSelected(option)
                                showDropdown = false
                            }, trailingIcon = {
                                if (option == selectedReminder) {
                                    Icon(
                                        painter = painterResource(id = android.R.drawable.checkbox_on_background),
                                        contentDescription = null,
                                        tint = Color(0xFF7C3AED),
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryView(onCategorySelected: (Category) -> Unit) {
    // ← State ko Card ke bahar, Column ke bahar rakho
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            HeaderWithIcon(
                icon = painterResource(R.drawable.ic_category),
                title = "Classification"
            )
            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 6.dp),
            ) {
                items(
                    items = Category.entries,
                    key = { it.dbKey } // ← key dena zaroori hai LazyRow mein
                ) { category ->
                    CategoryItem(
                        category = category,
                        isSelected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            onCategorySelected(category)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, isSelected: Boolean, onClick: () -> Unit) {
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "categoryElevation"
    )

    Card(
        modifier = Modifier
            .wrapContentWidth()
            // ← padding(6.dp) hata diya, LazyRow mein spacedBy handle kar raha hai
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PurpleAccent else SegmentBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(category.iconRes),
                contentDescription = "Icon",
                modifier = Modifier.size(20.dp),
                tint = if (isSelected) Color.White else CategoryUnselectedTextColor
            )
            Text(
                text = category.label,
                color = if (isSelected) Color.White else CategoryUnselectedTextColor,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ImportanceView() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        var selected by remember { mutableStateOf(Importance.Medium) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            // Header: icon + label
            HeaderWithIcon(icon = painterResource(R.drawable.ic_info), title = "Importance")

            Spacer(modifier = Modifier.height(20.dp))

            // Segmented control with sliding pill
            SegmentedControl(
                options = Importance.entries,
                selected = selected,
                onSelect = { selected = it },
                label = { it.name })
        }
    }

}

@Composable
fun <T> SegmentedControl(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: (T) -> String
) {
    val selectedIndex = options.indexOf(selected)

    val animatedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = tween(durationMillis = 300),
        label = "pill_slide"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50.dp))
            .background(SegmentBackground)
            .padding(4.dp) // ← padding box ke andar
    ) {
        val itemWidth = maxWidth / options.size
        val pillHeight = 42.dp

        Box(modifier = Modifier.height(pillHeight)) {
            // Sliding white pill
            Box(
                modifier = Modifier
                    .offset(x = itemWidth * animatedIndex) // ← no extra padding needed
                    .width(itemWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50.dp))
                    .background(SelectedBackground)
            )

            // Labels row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                options.forEach { option ->
                    val isSelected = selected == option
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onSelect(option) },
                        contentAlignment = Alignment.Center // ← text center mein
                    ) {
                        Text(
                            text = label(option),
                            color = if (isSelected) PurpleAccent else UnselectedTextColor,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ScheduleView(onDateSelection: () -> Unit, onTimeSelection: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 24.dp)
        ) {
            // Header: icon + label
            HeaderWithIcon(icon = painterResource(R.drawable.ic_calendar), title = "Schedule")

            Spacer(modifier = Modifier.height(20.dp))

            CommonDayTime(painterResource(R.drawable.ic_day), title = "Today", onDateSelection)
            Spacer(modifier = Modifier.height(15.dp))

            CommonDayTime(painterResource(R.drawable.ic_clock), title = "10:00 AM", onTimeSelection)
        }
    }
}

@Composable
fun CommonDayTime(icon: Painter, title: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        color = Color(0x0D7F13EC),
        border = BorderStroke(1.dp, Color(0x1A7F13EC)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = icon, contentDescription = "Icon", modifier = Modifier.size(20.dp)
            )
            Text(
                text = title, color = TitleColor, fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun IdentityView() {
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 32.dp)
        ) {
            // Header: icon + label
            HeaderWithIcon(icon = painterResource(R.drawable.ic_container), title = "Identity")

            Spacer(modifier = Modifier.height(10.dp))

            // Title TextField
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(
                    color = TitleColor, fontSize = 22.sp, fontWeight = FontWeight.Bold
                ),
                cursorBrush = SolidColor(PurpleAccent),
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            text = "What needs to be done?",
                            color = TitleColor.copy(alpha = 0.45f),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Details TextField
            BasicTextField(
                value = details,
                onValueChange = { details = it },
                textStyle = TextStyle(
                    color = TitleColor, fontSize = 15.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp),
                singleLine = false,
                maxLines = 5,
                decorationBox = { innerTextField ->
                    if (details.isEmpty()) {
                        Text(
                            text = "Add more details...",
                            color = PlaceholderColor,
                            fontSize = 15.sp,
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun HeaderWithIcon(icon: Painter, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = icon, contentDescription = "Icon", modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title, color = TitleColor, fontSize = 18.sp, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CustomToolBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(R.drawable.ic_close), contentDescription = "Close")
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "New Task",
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily(
                Font(
                    R.font.roboto_condensed_bold,
                    FontWeight.Normal
                )
            ),
            fontSize = 20.sp,
            color = Color(0xFF000000)
        )
        Spacer(modifier = Modifier.weight(1f))
        ActionButton(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(30.dp),
            text = "Create",
            fontSize = 14.sp,
            textColor = Color.White,
            containerColor = btnColor,
            isIconVisible = false,
            padding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
        ) {

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddTaskPreview() {
    AddTaskScreen()
}

enum class Category(
    val label: String,
    val dbKey: String,           // saved in Firebase Realtime DB
    val iconRes: Int,            // R.drawable.ic_*  — replace with your actual drawable names
) {
    WORK(
        label = "Work",
        dbKey = "work",
        iconRes = R.drawable.ic_work,
    ),
    PERSONAL(
        label = "Personal",
        dbKey = "personal",
        iconRes = R.drawable.ic_personal,
    ),
    SHOPPING(
        label = "Shopping",
        dbKey = "shopping",
        iconRes = R.drawable.ic_shopping,
    ),
    HEALTH(
        label = "Health",
        dbKey = "health",
        iconRes = R.drawable.ic_health,
    ),
    EDUCATION(
        label = "Education",
        dbKey = "education",
        iconRes = R.drawable.ic_education,
    ),
    FINANCE(
        label = "Finance",
        dbKey = "finance",
        iconRes = R.drawable.ic_finance,
    ),
    SOCIAL(
        label = "Social",
        dbKey = "social",
        iconRes = R.drawable.ic_social,
    ),
    HOME(
        label = "Home",
        dbKey = "home",
        iconRes = R.drawable.ic_home,
    ),
    CREATIVE(
        label = "Creative",
        dbKey = "creative",
        iconRes = R.drawable.ic_creative,
    );

    companion object {
        fun fromDbKey(key: String): Category = entries.firstOrNull { it.dbKey == key } ?: PERSONAL
    }
}
