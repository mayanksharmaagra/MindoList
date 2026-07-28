package com.jrprofessor.mindolist.customView

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
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

@Composable
fun TaskItem(
    task: TaskUIModel,
    onToggleComplete: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var checked by remember(task.isCompleted) { mutableStateOf(task.isCompleted) }

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
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
                    checked = checked,
                    onCheckedChange = {
                        checked = it
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
                        color = if (checked) MindoListTheme.colors.textSecondary else MindoListTheme.colors.textPrimary,
                        textDecoration = if (checked) TextDecoration.LineThrough
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
                                .background(if (checked) MindoListTheme.colors.success else priorityColor)
                        )

                        Text(
                            text = if (task.dueDate > 0) {
                                "${task.dueDate.toDisplayTime()} · ${category.label}"
                            } else {
                                "No time set · ${category.label}"
                            },
                            fontSize = 14.sp,
                            color = MindoListTheme.colors.textSecondary,
                        )
                    }
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
