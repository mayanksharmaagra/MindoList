package com.jrprofessor.mindolist.customView

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.extension.toDisplayTime
import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.model.Priority
import com.jrprofessor.mindolist.model.TaskSource
import com.jrprofessor.mindolist.model.TaskUIModel
import com.jrprofessor.mindolist.theme.MindoListTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: TaskUIModel,
    onToggleComplete: (Boolean) -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onDelete()
                    false // Don't dismiss automatically, wait for confirmation/action
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onEdit()
                    false // Don't dismiss automatically
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            SwipeBackground(dismissState)
        },
        modifier = modifier.padding(vertical = 4.dp),
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true
    ) {
        TaskItemContent(
            task = task,
            onToggleComplete = onToggleComplete,
            checked = task.isCompleted
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(dismissState: SwipeToDismissBoxState) {
    val direction = dismissState.dismissDirection
    val color = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFFEF5350) // Red for Delete
        SwipeToDismissBoxValue.EndToStart -> Color(0xFF42A5F5) // Blue for Edit
        else -> Color.Transparent
    }
    
    val icon = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Delete
        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Edit
        else -> null
    }

    val alignment = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(color)
            .padding(horizontal = 24.dp),
        contentAlignment = alignment
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun TaskItemContent(
    task: TaskUIModel,
    onToggleComplete: (Boolean) -> Unit,
    checked: Boolean,
) {
    var localChecked by remember(checked) { mutableStateOf(checked) }
    val priorityColor = Priority.entries
        .find { it.label == task.priority }?.dotColor ?: MindoListTheme.colors.textSecondary

    val category = Category.entries
        .find { it.dbKey == task.category } ?: Category.PERSONAL

    val sourceLabel = when (task.source) {
        TaskSource.MY_TASK -> "✓ You"
        TaskSource.GOOGLE_TASK -> "G Tasks"
        TaskSource.GOOGLE_CALENDAR -> "G Calendar"
        TaskSource.GOOGLE_REMINDER -> "G Reminder"
    }

    val stripeColor = when (task.source) {
        TaskSource.MY_TASK -> priorityColor
        else -> Color(0xFF4285F4) // Google Blue
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Left Stripe ──────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(stripeColor)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ── Checkbox ──────────────────────────────────────────────────
                TaskCheckbox(
                    checked = localChecked,
                    onCheckedChange = {
                        localChecked = it
                        onToggleComplete(it)
                    },
                )

                Spacer(modifier = Modifier.width(16.dp))

                // ── Title + Subtitle ──────────────────────────────────────────
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (localChecked) MindoListTheme.colors.textSecondary else MindoListTheme.colors.textPrimary,
                        textDecoration = if (localChecked) TextDecoration.LineThrough
                        else TextDecoration.None,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (localChecked) MindoListTheme.colors.success else priorityColor)
                        )

                        val timeText = if (task.dueDate > 0) {
                            val startTime = task.dueDate.toDisplayTime()
                            if (task.duration > 0) {
                                val durationMillis = task.duration * 60 * 1000L
                                val endTime = (task.dueDate + durationMillis).toDisplayTime()
                                "$startTime - $endTime"
                            } else {
                                startTime
                            }
                        } else {
                            "No time set"
                        }

                        Text(
                            text = timeText,
                            fontSize = 14.sp,
                            color = MindoListTheme.colors.textSecondary,
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = category.label,
                        fontSize = 14.sp,
                        color = MindoListTheme.colors.textSecondary,
                    )
                }

                // ── Source Chip ───────────────────────────────────────────────
                Surface(
                    color = MindoListTheme.colors.cardChildBg,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = sourceLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MindoListTheme.colors.textSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue = if (checked) MindoListTheme.colors.success else Color.Transparent,
        animationSpec = tween(200),
        label = "checkboxBg",
    )
    val borderColor by animateColorAsState(
        targetValue = if (checked) MindoListTheme.colors.success else MindoListTheme.colors.textSecondary.copy(alpha = 0.5f),
        animationSpec = tween(200),
        label = "checkboxBorder",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = CircleShape,
            )
            .clickable(
                indication = null,
                interactionSource = remember {
                    androidx.compose.foundation.interaction.MutableInteractionSource()
                },
            ) { onCheckedChange(!checked) },
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = Color.Black,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
