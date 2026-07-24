package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.theme.MindoListTheme
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

/**
 * Dashed rounded-rect border, drawn on top of whatever background/content
 * the modifier chain already has (used since Compose has no built-in dashed border).
 */
private fun Modifier.dashedBorder(
    color: Color,
    cornerRadius: Dp,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 6.dp,
    gapLength: Dp = 4.dp
): Modifier = this.drawWithContent {
    drawContent()
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(cornerRadius.toPx()),
        style = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength.toPx(), gapLength.toPx()), 0f
            )
        )
    )
}

/**
 * Small "clear day" illustration: a ringed sun with a checkmark inside,
 * plus a soft backdrop circle and a gentle hill line — drawn with Canvas
 * so no image assets are needed.
 */
@Composable
private fun ClearDayIllustration(modifier: Modifier = Modifier) {
    val accentColor = MindoListTheme.colors.accent
    val mintColor = MindoListTheme.colors.mint
    val lineColor = MindoListTheme.colors.textSecondary.copy(alpha = 0.2f)
    val backdropColor = MindoListTheme.colors.textPrimary.copy(alpha = 0.05f)

    Canvas(modifier = modifier.size(104.dp)) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)

        // soft backdrop circle
        drawCircle(
            color = backdropColor,
            radius = w * 0.41f,
            center = center
        )

        // sun ring
        val sunRadius = w * 0.13f
        val sunCenter = Offset(center.x, center.y - h * 0.02f)
        drawCircle(
            color = accentColor.copy(alpha = 0.9f),
            radius = sunRadius,
            center = sunCenter,
            style = Stroke(width = 3.dp.toPx())
        )

        // sun rays
        val rayLen = w * 0.05f
        val rayGap = sunRadius + w * 0.02f
        val rayAngles = listOf(0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f)
        rayAngles.forEach { angleDeg ->
            val angle = angleDeg * PI / 180.0
            val start = Offset(
                sunCenter.x + (rayGap * cos(angle)).toFloat(),
                sunCenter.y + (rayGap * sin(angle)).toFloat()
            )
            val end = Offset(
                sunCenter.x + ((rayGap + rayLen) * cos(angle)).toFloat(),
                sunCenter.y + ((rayGap + rayLen) * sin(angle)).toFloat()
            )
            drawLine(
                color = accentColor.copy(alpha = 0.7f),
                start = start,
                end = end,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // checkmark inside the sun
        val checkPath = Path().apply {
            moveTo(sunCenter.x - sunRadius * 0.45f, sunCenter.y)
            lineTo(sunCenter.x - sunRadius * 0.1f, sunCenter.y + sunRadius * 0.4f)
            lineTo(sunCenter.x + sunRadius * 0.5f, sunCenter.y - sunRadius * 0.45f)
        }
        drawPath(
            path = checkPath,
            color = mintColor,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // gentle hill/wave line along the base
        val wavePath = Path().apply {
            moveTo(w * 0.18f, h * 0.8f)
            cubicTo(w * 0.28f, h * 0.72f, w * 0.38f, h * 0.72f, w * 0.46f, h * 0.8f)
            cubicTo(w * 0.54f, h * 0.88f, w * 0.64f, h * 0.88f, w * 0.72f, h * 0.8f)
            cubicTo(w * 0.78f, h * 0.74f, w * 0.82f, h * 0.74f, w * 0.86f, h * 0.8f)
        }
        drawPath(
            path = wavePath,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

/**
 * Empty state shown in the Dashboard's "Today's tasks" section
 * when the user has no tasks due today.
 */
@Composable
fun TodayEmptyState(
    onAddTaskClick: () -> Unit,
    modifier: Modifier = Modifier,
    nextTaskTitle: String? = null,
    nextTaskWhen: String? = null
) {
    val colors = MindoListTheme.colors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.cardBg, RoundedCornerShape(18.dp))
            .dashedBorder(color = colors.textSecondary.copy(alpha = 0.1f), cornerRadius = 18.dp)
            .padding(horizontal = 20.dp, vertical = 34.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClearDayIllustration()

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Nothing due today",
            color = colors.textPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Your day is clear. Enjoy it, or get a head start on something upcoming.",
            color = colors.textSecondary,
            fontSize = 12.5.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.widthIn(max = 230.dp)
        )

        Spacer(Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = colors.accent.copy(alpha = 0.1f),
            border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.4f)),
            onClick = onAddTaskClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = "Add a task for today",
                    color = colors.accent,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (nextTaskTitle != null) {
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier
                        .size(5.dp)
                        .background(colors.mint, CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(text = "Next up: ", color = colors.textSecondary, fontSize = 11.5.sp)
                Text(text = nextTaskTitle, color = colors.textPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                if (nextTaskWhen != null) {
                    Text(text = " — $nextTaskWhen", color = colors.textSecondary, fontSize = 11.5.sp)
                }
            }
        }
    }
}
