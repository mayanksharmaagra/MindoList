package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Pixel-perfect implementation of the MindoList logo.
 * Consists of a two-tone "M" and a two-tone checkmark.
 */
@Composable
fun MindoLogo(
    modifier: Modifier = Modifier,
    darkPurple: Color = Color(0xFF6234B3),
    lightPurple: Color = Color(0xFF8E54E9),
    checkmarkDark: Color = Color(0xFFD97706),
    checkmarkLight: Color = Color(0xFFF3A84A)
) {
    Canvas(modifier = modifier.size(100.dp)) {
        val width = size.width
        val height = size.height
        // Proportionate stroke width matching the design (approx 11%)
        val strokeWidth = width * 0.11f

        // 1. Left Part of the "M"
        val leftMPath = Path().apply {
            moveTo(width * 0.25f, height * 0.75f) // Left leg bottom
            lineTo(width * 0.25f, height * 0.35f) // Left leg top
            lineTo(width * 0.50f, height * 0.58f) // Middle valley
        }

        // 2. Right Part of the "M"
        val rightMPath = Path().apply {
            moveTo(width * 0.50f, height * 0.58f) // Middle valley
            lineTo(width * 0.75f, height * 0.35f) // Right shoulder
            lineTo(width * 0.75f, height * 0.75f) // Right leg bottom
        }

        // 3. Checkmark Left Segment (Darker)
        val checkmarkLeftPath = Path().apply {
            moveTo(width * 0.38f, height * 0.54f) // Start
            lineTo(width * 0.50f, height * 0.68f) // Vertex
        }

        // 4. Checkmark Right Segment (Lighter)
        val checkmarkRightPath = Path().apply {
            moveTo(width * 0.50f, height * 0.68f) // Vertex
            lineTo(width * 0.88f, height * 0.32f) // End
        }

        // Draw "M" segments
        drawPath(
            path = leftMPath,
            color = darkPurple,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        drawPath(
            path = rightMPath,
            color = lightPurple,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw Checkmark segments (On top of M)
        drawPath(
            path = checkmarkLeftPath,
            color = checkmarkDark,
            style = Stroke(
                width = strokeWidth * 1.05f, // Slightly thicker for emphasis
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        drawPath(
            path = checkmarkRightPath,
            color = checkmarkLight,
            style = Stroke(
                width = strokeWidth * 1.05f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Preview
@Composable
fun MindoLogoPreview() {
    Box(modifier = Modifier.size(200.dp)) {
        MindoLogo(modifier = Modifier.size(100.dp))
    }
}
