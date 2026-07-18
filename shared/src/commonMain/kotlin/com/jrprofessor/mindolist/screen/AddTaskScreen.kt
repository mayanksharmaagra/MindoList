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
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.customView.DatePickerDialog
import com.jrprofessor.mindolist.customView.TimePickerDialog
import com.jrprofessor.mindolist.icons.IcBell
import com.jrprofessor.mindolist.icons.IcCalendar
import com.jrprofessor.mindolist.icons.IcCategory
import com.jrprofessor.mindolist.icons.IcClock
import com.jrprofessor.mindolist.icons.IcClose
import com.jrprofessor.mindolist.icons.IcContainer
import com.jrprofessor.mindolist.icons.IcDay
import com.jrprofessor.mindolist.icons.IcInfo
import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.model.Priority
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.jrprofessor.mindolist.presentation.addTask.AddTaskAction
import com.jrprofessor.mindolist.presentation.addTask.AddTaskEvent
import com.jrprofessor.mindolist.presentation.addTask.AddTaskUiState
import com.jrprofessor.mindolist.theme.CardBackground
import com.jrprofessor.mindolist.theme.CategoryUnselectedTextColor
import com.jrprofessor.mindolist.theme.PlaceholderColor
import com.jrprofessor.mindolist.theme.PrimaryBlue
import com.jrprofessor.mindolist.theme.RemindViewBG
import com.jrprofessor.mindolist.theme.SegmentBackground
import com.jrprofessor.mindolist.theme.SelectedBackground
import com.jrprofessor.mindolist.theme.TitleColor
import com.jrprofessor.mindolist.theme.UnselectedTextColor
import com.jrprofessor.mindolist.theme.backgroundColor
import com.jrprofessor.mindolist.theme.btnColor
import com.jrprofessor.mindolist.utils.Logger
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.getPlatform
import com.jrprofessor.mindolist.viewmodels.TaskViewModel
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

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
fun AddTaskScreen(
    viewModel: TaskViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {


    val state by viewModel.state.collectAsState()
    // Loading → State se handle karo
    LaunchedEffect(Unit) {
        viewModel.addTaskEffect.collectLatest { effect ->
            when (effect) {
                is AddTaskEvent.Error -> {
                    showToast(effect.error)
                }

                is AddTaskEvent.Success -> {
                    showToast(effect.message)
                    onNavigateBack()
                }
            }
        }
    }
    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }


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
            AddTaskContent(
                state = state,
                onNavigateBack,
                viewModel,
                onDateSelection = {
                    showDatePicker = it
                }, onTimeSelection = {
                    showTimePicker = it
                }
            )
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            initialHour = state.selectedTime.split(" ")[0].split(":")[0].toInt(),
            initialMinute = state.selectedTime.split(" ")[0].split(":")[1].toInt(),
            period = state.selectedTime.split(" ")[1],
            onTimeSelected = { hour, minute,period ->
                viewModel.dispatch(
                    AddTaskAction.TimeSelected(
                        "$hour:$minute $period"
                    )
                )
                showTimePicker = false
                Logger.debug {
                    "AddTaskScreen: ${state.selectedTime}"
                }
            },
            onDismiss = { showTimePicker = false }
        )
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                viewModel.dispatch(
                    AddTaskAction.DateSelected(date.monthYearDayFormatted())
                )
                Logger.debug { "AddTaskScreen: ${state.selectedDate}" }
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@Composable
fun AddTaskContent(
    state: AddTaskUiState,
    onNavigateBack: () -> Unit,
    viewModel: TaskViewModel,
    onDateSelection: (Boolean) -> Unit,
    onTimeSelection: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // ← add this
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        CustomToolBar(viewModel,onNavigateBack)
        Spacer(modifier = Modifier.height(15.dp))

        // ── AI Input Section ──────────────────────────────
        if (getPlatform().isAndroid) {
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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HeaderWithIcon(icon = Icons.Default.AutoAwesome, title = "Smart Add")

                    OutlinedTextField(
                        value = state.naturalInput,
                        onValueChange = { viewModel.dispatch(AddTaskAction.NaturalInputChanged(it)) },
                        placeholder = {
                            Text(
                                "e.g. Remind me to call Mom tomorrow at 6 PM",
                                fontSize = 14.sp,
                                color = PlaceholderColor
                            )
                        },
                        leadingIcon = {
                            IconButton(onClick = { viewModel.dispatch(AddTaskAction.ToggleRecording) }) {
                                Icon(
                                    imageVector = if (state.isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                    contentDescription = "Speak",
                                    tint = if (state.isRecording) Color.Red else PrimaryBlue
                                )
                            }
                        },
                        trailingIcon = {
                            if (state.isParsingAi) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                IconButton(onClick = { viewModel.dispatch(AddTaskAction.ParseAiClicked) }) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Parse with AI",
                                        tint = PrimaryBlue
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TitleColor,
                            unfocusedTextColor = TitleColor,
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = RemindViewBG,
                            unfocusedContainerColor = RemindViewBG
                        ),
                        minLines = 2
                    )

                    state.aiError?.let {
                        Text(
                            it, color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
        IdentityView(state, viewModel)
        Spacer(modifier = Modifier.height(15.dp))
        ScheduleView(
            state,
            viewModel = viewModel,
            onDateSelection = onDateSelection,
            onTimeSelection = onTimeSelection
        )
        Spacer(modifier = Modifier.height(15.dp))
        ImportanceView(viewModel, state)
        Spacer(modifier = Modifier.height(15.dp))
        CategoryView(state = state) { category ->
            viewModel.dispatch(AddTaskAction.CategoryChanged(category))
        }
        Spacer(modifier = Modifier.height(15.dp))
        AlertsSection(
            viewModel = viewModel,
            isEnabled = state.reminderEnabled,
            selectedReminder = state.reminderOption,
            onToggle = { viewModel.dispatch(AddTaskAction.ReminderToggled(it)) },
            onReminderSelected = { viewModel.dispatch(AddTaskAction.ReminderValue(it)) })
    }
}

@Composable
fun AlertsSection(
    isEnabled: Boolean,
    selectedReminder: ReminderOption,
    onToggle: (Boolean) -> Unit,
    onReminderSelected: (ReminderOption) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel,
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
                    imageVector = IcBell, // replace with your bell icon
                    contentDescription = "Alert",
                    tint = PrimaryBlue,
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
                        checkedTrackColor = PrimaryBlue,
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
                                color = PrimaryBlue,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown",
                                tint = PrimaryBlue,
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
                                    color = if (option == selectedReminder) PrimaryBlue else Color(
                                        0xFF1E293B
                                    ),
                                )
                            }, onClick = {
                                onReminderSelected(option)
                                showDropdown = false
                            }, trailingIcon = {
                                if (option == selectedReminder) {
                                    Icon(
                                        imageVector = Icons.Default.CheckBoxOutlineBlank,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
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
fun CategoryView(state: AddTaskUiState, onCategorySelected: (Category) -> Unit) {
    // ← State ko Card ke bahar, Column ke bahar rakho
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
                icon = IcCategory,
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
                        isSelected = state.category == category,
                        onClick = {
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
            containerColor = if (isSelected) PrimaryBlue else SegmentBackground
        ), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = category.iconRes,
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
fun ImportanceView(viewModel: TaskViewModel, state: AddTaskUiState) {
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
            HeaderWithIcon(icon = IcInfo, title = "Importance")

            Spacer(modifier = Modifier.height(20.dp))

            // Segmented control with sliding pill
            SegmentedControl(
                options = Priority.entries,
                selected = state.priority,
                onSelect = { viewModel.dispatch(AddTaskAction.PriorityChanged(it)) },
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
                            color = if (isSelected) PrimaryBlue else UnselectedTextColor,
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
fun ScheduleView(
    state: AddTaskUiState,
    viewModel: TaskViewModel,
    onDateSelection: (Boolean) -> Unit,
    onTimeSelection: (Boolean) -> Unit,
) {

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
            HeaderWithIcon(icon = IcCalendar, title = "Schedule")

            Spacer(modifier = Modifier.height(20.dp))

            CommonDayTime(viewModel, IcDay, title = state.selectedDate, onDateSelection)
            Spacer(modifier = Modifier.height(15.dp))

            CommonDayTime(
                viewModel,
                IcClock,
                title = state.selectedTime,
                onTimeSelection
            )
        }
    }
}

@Composable
fun CommonDayTime(
    viewModel: TaskViewModel,
    icon: ImageVector,
    title: String,
    onClick: (Boolean) -> Unit
) {
    Surface(
        onClick = { onClick(true) },
        shape = RoundedCornerShape(50.dp),
        color = Color(0x0D3B82F6),
        border = BorderStroke(1.dp, Color(0x1A3B82F6)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                imageVector = icon,
                contentDescription = "Icon",
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(PrimaryBlue)
            )
            Text(
                text = title, color = TitleColor, fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun IdentityView(state: AddTaskUiState, viewModel: TaskViewModel) {
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
            HeaderWithIcon(icon = IcContainer, title = "Identity")

            Spacer(modifier = Modifier.height(10.dp))

            // Title TextField
            BasicTextField(
                value = state.title,
                onValueChange = { viewModel.dispatch(AddTaskAction.TitleChanged(it)) },
                textStyle = TextStyle(
                    color = TitleColor, fontSize = 22.sp, fontWeight = FontWeight.Bold
                ), cursorBrush = SolidColor(PrimaryBlue),
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                decorationBox = { innerTextField ->
                    if (state.title.isEmpty()) {
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
                value = state.description,
                onValueChange = { viewModel.dispatch(AddTaskAction.DescriptionChanged(it)) },
                textStyle = TextStyle(
                    color = TitleColor, fontSize = 15.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp),
                singleLine = false,
                maxLines = 5,
                decorationBox = { innerTextField ->
                    if (state.description.isEmpty()) {
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
fun HeaderWithIcon(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            imageVector = icon,
            contentDescription = "Icon",
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(PrimaryBlue)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title, color = TitleColor, fontSize = 18.sp, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CustomToolBar(viewModel: TaskViewModel, onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onNavigateBack,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = IcClose,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "New Task",
            style = MaterialTheme.typography.titleMedium,
//            fontFamily = FontFamily(
//                Font(
//                    Res.font.roboto_condensed_bold,
//                    FontWeight.Normal
//                )
//            ),
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
            viewModel.dispatch(AddTaskAction.SaveClicked)
        }
    }
}

@Composable
fun AddTaskPreview() {
    AddTaskScreen()
}