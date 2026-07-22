package com.jrprofessor.mindolist.customView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomLinearProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    progressMax: Float = 1.0f,
    progressBarColor: Color = Color.Black,
    progressBarHeight: Dp = 8.dp,
    backgroundProgressBarColor: Color = Color.Gray,
    backgroundProgressBarHeight: Dp = 8.dp,
    roundBorder: Boolean = true
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(maxOf(progressBarHeight, backgroundProgressBarHeight))
    ) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        // Draw background track
        drawLine(
            color = backgroundProgressBarColor,
            start = Offset(0f, centerY),
            end = Offset(width, centerY),
            strokeWidth = backgroundProgressBarHeight.toPx(),
            cap = if (roundBorder) StrokeCap.Round else StrokeCap.Butt
        )

        // Draw progress bar
        val progressWidth = (progress / progressMax) * width
        if (progressWidth > 0) {
            drawLine(
                color = progressBarColor,
                start = Offset(0f, centerY),
                end = Offset(progressWidth, centerY),
                strokeWidth = progressBarHeight.toPx(),
                cap = if (roundBorder) StrokeCap.Round else StrokeCap.Butt
            )
        }
    }
}
