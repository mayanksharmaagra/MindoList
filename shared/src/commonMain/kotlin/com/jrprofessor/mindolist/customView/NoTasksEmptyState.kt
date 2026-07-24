package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jrprofessor.mindolist.theme.MindoListTheme

/**
 * Hand-drawn illustration: soft circle backdrop, dashed ring, a clipboard
 * with three ticked-off rows, and floating sparkle/dot accents.
 */
@Composable
private fun EmptyClipboardIllustration(modifier: Modifier = Modifier) {
    val colors = MindoListTheme.colors
    val accentColor = colors.accent
    val mintColor = colors.mint
    val panelColor = colors.textSecondary.copy(alpha = 0.05f)
    val lineColor = colors.textSecondary.copy(alpha = 0.1f)

    Canvas(modifier = modifier.size(190.dp)) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)

        // outer soft backdrop
        drawCircle(color = panelColor, radius = w * 0.44f, center = center)
        // dashed ring
        drawCircle(
            color = lineColor,
            radius = w * 0.30f,
            center = center,
            style = Stroke(
                width = 1.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 4.dp.toPx()))
            )
        )

        // clipboard body
        val boardW = w * 0.30f
        val boardH = h * 0.42f
        val boardLeft = center.x - boardW / 2f
        val boardTop = center.y - boardH / 2f - h * 0.02f
        drawRoundRect(
            color = colors.cardChildBg,
            topLeft = Offset(boardLeft, boardTop),
            size = androidx.compose.ui.geometry.Size(boardW, boardH),
            cornerRadius = CornerRadius(10.dp.toPx())
        )
        drawRoundRect(
            color = lineColor,
            topLeft = Offset(boardLeft, boardTop),
            size = androidx.compose.ui.geometry.Size(boardW, boardH),
            cornerRadius = CornerRadius(10.dp.toPx()),
            style = Stroke(width = 1.dp.toPx())
        )
        // clip clasp
        drawRoundRect(
            color = colors.textSecondary.copy(alpha = 0.2f),
            topLeft = Offset(center.x - boardW * 0.24f, boardTop - h * 0.025f),
            size = androidx.compose.ui.geometry.Size(boardW * 0.48f, h * 0.035f),
            cornerRadius = CornerRadius(6.dp.toPx())
        )

        // three checklist rows
        val rowXCircle = boardLeft + boardW * 0.22f
        val rowXLine = boardLeft + boardW * 0.40f
        val rowLineWidths = listOf(boardW * 0.40f, boardW * 0.32f, boardW * 0.44f)
        val rowYs = listOf(
            boardTop + boardH * 0.28f,
            boardTop + boardH * 0.50f,
            boardTop + boardH * 0.72f
        )
        rowYs.forEachIndexed { i, y ->
            drawCircle(color = mintColor, radius = 6.dp.toPx(), center = Offset(rowXCircle, y))
            val checkPath = Path().apply {
                moveTo(rowXCircle - 3.dp.toPx(), y)
                lineTo(rowXCircle - 0.5f.dp.toPx(), y + 2.5.dp.toPx())
                lineTo(rowXCircle + 3.5f.dp.toPx(), y - 3.dp.toPx())
            }
            drawPath(
                path = checkPath,
                color = colors.cardChildBg, // Contrast checkmark against the circle
                style = Stroke(width = 1.6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawRoundRect(
                color = colors.textSecondary.copy(alpha = 0.4f),
                topLeft = Offset(rowXLine, y - 2.75.dp.toPx()),
                size = androidx.compose.ui.geometry.Size(rowLineWidths[i], 5.5.dp.toPx()),
                cornerRadius = CornerRadius(2.75.dp.toPx())
            )
        }

        // decorative sparkle (4-point star) helper
        fun drawSparkle(cx: Float, cy: Float, r: Float, color: Color, alpha: Float) {
            val path = Path().apply {
                moveTo(cx, cy - r)
                cubicTo(cx, cy - r * 0.3f, cx + r * 0.3f, cy, cx + r, cy)
                cubicTo(cx + r * 0.3f, cy, cx, cy + r * 0.3f, cx, cy + r)
                cubicTo(cx, cy + r * 0.3f, cx - r * 0.3f, cy, cx - r, cy)
                cubicTo(cx - r * 0.3f, cy, cx, cy - r * 0.3f, cx, cy - r)
                close()
            }
            drawPath(path = path, color = color.copy(alpha = alpha))
        }

        drawSparkle(w * 0.82f, h * 0.22f, w * 0.045f, accentColor, 0.9f)
        drawSparkle(w * 0.20f, h * 0.68f, w * 0.035f, mintColor, 0.7f)
        drawCircle(color = accentColor, radius = w * 0.02f, center = Offset(w * 0.16f, h * 0.24f), alpha = 0.7f)
        drawCircle(color = mintColor, radius = w * 0.017f, center = Offset(w * 0.84f, h * 0.62f), alpha = 0.8f)
    }
}

/**
 * Empty state shown on the All Tasks screen when the user has no tasks at all.
 */
@Composable
fun NoTasksEmptyState(
    onAddTaskClick: () -> Unit,
    modifier: Modifier = Modifier,
    aiHintExample: String = "Remind me to call mom tomorrow",
    onAiHintClick: (() -> Unit)? = null
) {
    val colors = MindoListTheme.colors

    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmptyClipboardIllustration()

        Spacer(Modifier.height(6.dp))

        Text(
            text = "ALL CAUGHT UP",
            color = colors.mint,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "No tasks here yet",
            color = colors.textPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Your list is clear. Add something you need to get done, or just type it naturally and let AI fill it in.",
            color = colors.textSecondary,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 260.dp)
        )

        Spacer(Modifier.height(22.dp))

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = colors.accent,
            onClick = onAddTaskClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp)
            ) {
                PlusGlyph(color = Color.Black)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Add your first task",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        val hintText = buildAnnotatedString {
            append("or try ")
            withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.Bold)) {
                append("\"$aiHintExample\"")
            }
        }
        Text(
            text = hintText,
            fontSize = 12.5.sp,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            modifier = if (onAiHintClick != null) {
                Modifier.padding(bottom = 4.dp)
                    .clickable { onAiHintClick() }
            } else Modifier
        )
    }
}

/** Minimal hand-drawn "+" so this file needs no icon-library dependency. */
@Composable
private fun PlusGlyph(color: Color, size: androidx.compose.ui.unit.Dp = 14.dp) {
    Canvas(modifier = Modifier.size(size)) {
        val stroke = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
        drawLine(color, Offset(this.size.width / 2f, 0f), Offset(this.size.width / 2f, this.size.height), stroke.width, stroke.cap)
        drawLine(color, Offset(0f, this.size.height / 2f), Offset(this.size.width, this.size.height / 2f), stroke.width, stroke.cap)
    }
}
