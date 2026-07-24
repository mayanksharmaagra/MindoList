package com.jrprofessor.mindolist.customView

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.extension.toDisplayTime
import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.model.Priority
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.theme.MindoListTheme

@Composable
fun TaskItem(
    task: TaskModel,
    onToggleComplete: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var checked by remember(task.isCompleted) { mutableStateOf(task.isCompleted) }

    val priorityColor = Priority.entries
        .find { it.label == task.priority }?.dotColor ?: MindoListTheme.colors.textSecondary

    val category = Category.entries
        .find { it.dbKey == task.category } ?: Category.PERSONAL

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MindoListTheme.colors.cardBg),
        border = BorderStroke(1.dp, MindoListTheme.colors.textSecondary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Checkbox ──────────────────────────────────────────────────────
            TaskCheckbox(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onToggleComplete(it)
                },
            )

            Spacer(modifier = Modifier.width(16.dp))

            // ── Title + Subtitle ──────────────────────────────────────────────
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
                        text = "${task.dueDate.toDisplayTime()} · ${category.label}",
                        fontSize = 14.sp,
                        color = MindoListTheme.colors.textSecondary,
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
