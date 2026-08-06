package com.jrprofessor.mindolist.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.customView.ActionButton
import com.jrprofessor.mindolist.customView.CategorySelectionDialog
import com.jrprofessor.mindolist.customView.DatePickerDialog
import com.jrprofessor.mindolist.customView.TimePickerDialog
import com.jrprofessor.mindolist.getPlatform
import com.jrprofessor.mindolist.icons.IcBell
import com.jrprofessor.mindolist.icons.IcCalendar
import com.jrprofessor.mindolist.icons.IcClock
import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.model.Priority
import com.jrprofessor.mindolist.presentation.addTask.AddTaskAction
import com.jrprofessor.mindolist.presentation.addTask.AddTaskEvent
import com.jrprofessor.mindolist.presentation.addTask.AddTaskUiState
import com.jrprofessor.mindolist.theme.*
import com.jrprofessor.mindolist.utils.RequestMicrophonePermission
import com.jrprofessor.mindolist.utils.showToast
import com.jrprofessor.mindolist.viewmodels.TaskViewModel
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
    viewModel: TaskViewModel,
    settingsViewModel: com.jrprofessor.mindolist.viewmodels.SettingsViewmodel = koinViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val settingsState by settingsViewModel.state.collectAsState()
    
    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.dispatch(AddTaskAction.ResetState)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.addTaskEffect.collectLatest { effect ->
            when (effect) {
                is AddTaskEvent.Error -> showToast(effect.error)
                is AddTaskEvent.Success -> {
                    showToast(effect.message)
                    onNavigateBack()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MindoListTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            AddTaskToolbar(onNavigateBack)
            Spacer(modifier = Modifier.height(30.dp))

            if (settingsState.aiExtractionEnabled) {
                AiExtractionSection(state, viewModel)

                Spacer(modifier = Modifier.height(30.dp))
                ManualDivider()
                Spacer(modifier = Modifier.height(25.dp))
            }

            ManualInputSection(
                state = state,
                viewModel = viewModel,
                onDateClick = { showDatePicker = true },
                onTimeClick = { showTimePicker = true },
                onCategoryClick = { showCategoryDialog = true }
            )

            Spacer(modifier = Modifier.height(40.dp))

            ActionButton(
                text = if (state.isEditMode) "Update task" else "Save task",
                isLoading = state.isLoading,
                isEnabled = state.title.isNotBlank() && !state.isLoading,
                containerColor = MindoListAccentFixed,
                textColor = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = { viewModel.dispatch(AddTaskAction.SaveClicked) }
            )
            Spacer(modifier = Modifier.height(40.dp))
        }

        if (showTimePicker) {
            val (initialHour, initialMinute, period) = runCatching {
                val timeParts = state.selectedTime.split(" ")
                val hMin = timeParts[0].split(":")
                Triple(hMin[0].toInt(), hMin[1].toInt(), timeParts[1])
            }.getOrDefault(Triple(12, 0, "PM"))

            TimePickerDialog(
                initialHour = initialHour,
                initialMinute = initialMinute,
                period = period,
                onTimeSelected = { formattedTime ->
                    viewModel.dispatch(AddTaskAction.TimeSelected(formattedTime))
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }
        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { date ->
                    viewModel.dispatch(AddTaskAction.DateSelected(date.monthYearDayFormatted()))
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
        if (showCategoryDialog) {
            CategorySelectionDialog(
                selectedCategory = state.category,
                onCategorySelected = {
                    viewModel.dispatch(AddTaskAction.CategoryChanged(it))
                },
                onDismiss = { showCategoryDialog = false }
            )
        }
    }
}

@Composable
fun AddTaskToolbar(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            onClick = onNavigateBack,
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(12.dp),
            color = MindoListTheme.colors.cardChildBg,
            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = MindoListTheme.colors.textPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Add Task",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MindoListTheme.colors.textPrimary,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(44.dp))
    }
}

@Composable
fun AiExtractionSection(state: AddTaskUiState, viewModel: TaskViewModel) {
//    if (!getPlatform().isAndroid) return
    
    var showPermissionRequest by remember { mutableStateOf(false) }

    if (showPermissionRequest) {
        RequestMicrophonePermission(
            onPermissionGranted = {
                showPermissionRequest = false
                viewModel.dispatch(AddTaskAction.ToggleRecording)
            },
            onPermissionDenied = {
                showPermissionRequest = false
                showToast("Microphone permission is required for voice input")
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MindoListTheme.colors.accent.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MindoListTheme.colors.accent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Describe it your way",
                    color = MindoListTheme.colors.textPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            OutlinedTextField(
                value = state.naturalInput,
                onValueChange = { viewModel.dispatch(AddTaskAction.NaturalInputChanged(it)) },
                placeholder = {
                    Text(
                        if (state.isRecording) "Listening..." else "\"Team sync tomorrow 10am, high priority, work\"",
                        color = if (state.isRecording) MindoListAccentFixed else MindoListTheme.colors.textSecondary,
                        fontSize = 16.sp
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { 
                        if (state.isRecording) {
                            viewModel.dispatch(AddTaskAction.ToggleRecording)
                        } else {
                            showPermissionRequest = true
                        }
                    }) {
                        Icon(
                            imageVector = if (state.isRecording) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = if (state.isRecording) "Stop Recording" else "Start Recording",
                            tint = if (state.isRecording) MindoListTheme.colors.error else MindoListTheme.colors.textSecondary
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                    unfocusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                    focusedContainerColor = MindoListTheme.colors.inputBg,
                    unfocusedContainerColor = MindoListTheme.colors.inputBg,
                    focusedTextColor = MindoListTheme.colors.textPrimary,
                    unfocusedTextColor = MindoListTheme.colors.textPrimary
                )
            )

            ActionButton(
                text = "Extract with AI",
                isLoading = state.isParsingAi,
                isEnabled = state.naturalInput.isNotBlank() && !state.isParsingAi,
                containerColor = MindoListAccentFixed,
                textColor = Color.Black,
                fontWeight = FontWeight.Bold,
                icon = Icons.Default.AutoAwesome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                iconTint = Color.Black,
                onClick = { viewModel.dispatch(AddTaskAction.ParseAiClicked) }
            )

            AnimatedVisibility(
                visible = state.aiError != null,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Text(
                    text = state.aiError ?: "",
                    color = MindoListTheme.colors.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ManualDivider() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)
        )
        Text(
            "OR FILL MANUALLY",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MindoListTheme.colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun ManualInputSection(
    state: AddTaskUiState,
    viewModel: TaskViewModel,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onCategoryClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        InputField(
            label = "TITLE",
            value = state.title,
            onValueChange = { viewModel.dispatch(AddTaskAction.TitleChanged(it)) },
            placeholder = "Team sync call",
            maxLength = 100
        )

        InputField(
            label = "DESCRIPTION",
            value = state.description,
            onValueChange = { viewModel.dispatch(AddTaskAction.DescriptionChanged(it)) },
            placeholder = "Add notes (optional)",
            minHeight = 80.dp,
            maxLength = 500,
            showCount = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ClickableInputField(
                    label = "DUE DATE",
                    value = state.selectedDate,
                    onClick = onDateClick,
                    icon = IcCalendar
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                ClickableInputField(
                    label = "DUE TIME",
                    value = state.selectedTime,
                    onClick = onTimeClick,
                    icon = IcClock
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "PRIORITY",
                color = MindoListTheme.colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Priority.entries.forEach { priority ->
                    PriorityButton(
                        priority = priority,
                        isSelected = state.priority == priority,
                        onClick = { viewModel.dispatch(AddTaskAction.PriorityChanged(priority)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                "CATEGORY",
                color = MindoListTheme.colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            ClickableInputField(
                label = "", 
                value = state.category.label,
                onClick = onCategoryClick,
                icon = Icons.Default.KeyboardArrowDown
            )
        }

        DurationSection(
            selectedDuration = state.duration,
            onDurationSelected = { viewModel.dispatch(AddTaskAction.DurationChanged(it)) }
        )

        AlertsSection(
            viewModel = viewModel,
            isEnabled = state.reminderEnabled,
            selectedReminder = state.reminderOption,
            onToggle = { viewModel.dispatch(AddTaskAction.ReminderToggled(it)) },
            onReminderSelected = { viewModel.dispatch(AddTaskAction.ReminderValue(it)) })
    }
}
@Composable
fun DurationSection(
    selectedDuration: Int,
    onDurationSelected: (Int) -> Unit
) {
    val options = listOf(
        15 to "15 min",
        30 to "30 min",
        45 to "45 min",
        60 to "1 hr",
        90 to "1.5 hrs",
        120 to "2 hrs",
        150 to "2.5 hrs",
        180 to "3 hrs"
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "TASK DURATION",
            color = MindoListTheme.colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(options) { (minutes, label) ->
                val isSelected = selectedDuration == minutes
                Surface(
                    onClick = { onDurationSelected(minutes) },
                    modifier = Modifier.height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MindoListTheme.colors.accent else MindoListTheme.colors.inputBg,
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) MindoListTheme.colors.accent else MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.Black else MindoListTheme.colors.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
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

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "ALERTS",
            color = MindoListTheme.colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = IcBell,
                        contentDescription = "Alert",
                        tint = MindoListAccentFixed,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Enable Reminder",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MindoListTheme.colors.textPrimary,
                        modifier = Modifier.weight(1f),
                    )
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = onToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MindoListAccentFixed,
                            uncheckedThumbColor = MindoListTheme.colors.textSecondary,
                            uncheckedTrackColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.1f),
                        )
                    )
                }

                AnimatedVisibility(
                    visible = isEnabled,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            onClick = { showDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MindoListTheme.colors.inputBg,
                            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Remind me",
                                    fontSize = 14.sp,
                                    color = MindoListTheme.colors.textSecondary,
                                    modifier = Modifier.weight(1f),
                                )
                                Text(
                                    text = selectedReminder.label,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MindoListTheme.colors.accent,
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Dropdown",
                                    tint = MindoListTheme.colors.accent,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false },
                            modifier = Modifier
                                .background(MindoListTheme.colors.cardBg)
                                .fillMaxWidth(0.8f)
                        ) {
                            ReminderOption.entries.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option.label,
                                            fontSize = 14.sp,
                                            fontWeight = if (option == selectedReminder) FontWeight.Bold else FontWeight.Normal,
                                            color = if (option == selectedReminder) MindoListTheme.colors.accent else MindoListTheme.colors.textPrimary,
                                        )
                                    },
                                    onClick = {
                                        onReminderSelected(option)
                                        showDropdown = false
                                    },
                                    trailingIcon = {
                                        if (option == selectedReminder) {
                                            Icon(
                                                imageVector = Icons.Default.CheckBoxOutlineBlank,
                                                contentDescription = null,
                                                tint = MindoListTheme.colors.accent,
                                                modifier = Modifier.size(16.dp),
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    minHeight: Dp = 56.dp,
    maxLength: Int? = null,
    showCount: Boolean = false
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            color = MindoListTheme.colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    if (maxLength == null || it.length <= maxLength) {
                        onValueChange(it)
                    }
                },
                placeholder = { Text(placeholder, color = MindoListTheme.colors.textSecondary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = minHeight),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                    unfocusedBorderColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f),
                    focusedContainerColor = MindoListTheme.colors.inputBg,
                    unfocusedContainerColor = MindoListTheme.colors.inputBg,
                    focusedTextColor = MindoListTheme.colors.textPrimary,
                    unfocusedTextColor = MindoListTheme.colors.textPrimary
                )
            )
            
            if (showCount && maxLength != null) {
                Text(
                    text = "${value.length}/$maxLength",
                    color = MindoListTheme.colors.textSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 12.dp)
                )
            }
        }
    }
}

@Composable
fun ClickableInputField(
    label: String,
    value: String,
    onClick: () -> Unit,
    icon: ImageVector
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            color = MindoListTheme.colors.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            color = MindoListTheme.colors.inputBg,
            border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value.ifBlank { "No time" },
                    color = if (value.isBlank()) MindoListTheme.colors.textSecondary else MindoListTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MindoListTheme.colors.textSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun PriorityButton(
    priority: Priority,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) {
        when (priority) {
            Priority.HIGH -> MindoListTheme.colors.error.copy(alpha = 0.5f)
            Priority.MEDIUM -> MindoListTheme.colors.accent.copy(alpha = 0.5f)
            Priority.LOW -> MindoListTheme.colors.textSecondary.copy(alpha = 0.5f)
        }
    } else MindoListTheme.colors.textSecondary.copy(alpha = 0.1f)

    val bgColor = if (isSelected) {
        when (priority) {
            Priority.HIGH -> MindoListTheme.colors.error.copy(alpha = 0.15f)
            Priority.MEDIUM -> MindoListTheme.colors.accent.copy(alpha = 0.15f)
            Priority.LOW -> MindoListTheme.colors.textSecondary.copy(alpha = 0.15f)
        }
    } else MindoListTheme.colors.inputBg

    Surface(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = priority.label.lowercase().replaceFirstChar { it.uppercase() },
                color = if (isSelected) Color.White else MindoListTheme.colors.textSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
