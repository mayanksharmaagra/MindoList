package com.jrprofessor.mindolist.customView

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.extension.toDisplayTime
import com.jrprofessor.mindolist.model.Priority
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.theme.BorderLight
import com.jrprofessor.mindolist.theme.TextDark
import com.jrprofessor.mindolist.theme.TextGray
import com.jrprofessor.mindolist.theme.TextMuted

@Composable
fun TaskItem(
    task: TaskModel,
    onToggleComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val checkScale by animateFloatAsState(
        targetValue = if (task.isCompleted) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "checkScale",
    )
    val titleColor by animateColorAsState(
        targetValue = if (task.isCompleted) TextMuted else TextDark,
        animationSpec = tween(durationMillis = 200),
        label = "titleColor",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Priority.entries.find {
                it.label == task.priority
            }?.stripColor?.let {
                Box(
                    modifier = Modifier
                        .width(5.dp)
                        .height(72.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                        .background(
                            it
                        ),
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Box(
                Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        (if (task.isCompleted) Priority.entries.find { it.label == task.priority }?.stripColor
                        else Color.Transparent)!!
                    )
                    .clickable { onToggleComplete(task.id) }, Alignment.Center
            ) {
                if (!task.isCompleted) {
                    Surface(
                        modifier = Modifier.size(24.dp),
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = BorderLight,
                        ),
                    ) {}
                }

                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .scale(checkScale)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
//                    fontFamily = FontFamily(
//                        Font(
//                            Res.font.roboto_condensed_regular,
//                            FontWeight.SemiBold
//                        )
//                    ),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough
                        else TextDecoration.None,
                    ),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⏱",
                        fontSize = 18.sp,
                        color = TextGray,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.dueDate.toDisplayTime(),
                        fontSize = 14.sp,
//                        fontFamily = FontFamily(
//                            Font(
//                                Res.font.roboto_condensed_regular,
//                                FontWeight.Normal
//                            )
//                        ),
                        color = TextGray,
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Priority.entries.find { it.label == task.priority }?.backgroundColor!!)
                            .padding(horizontal = 10.dp, vertical = 3.dp),
                    ) {
                        Text(
                            text = task.priority,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Priority.entries.find { it.label == task.priority }?.color!!
                            ),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}