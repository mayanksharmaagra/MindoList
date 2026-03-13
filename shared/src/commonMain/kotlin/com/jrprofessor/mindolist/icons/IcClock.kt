package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcClock: ImageVector = ImageVector.Builder(
    name = "IcClock",
    defaultWidth = 20.dp,
    defaultHeight = 20.dp,
    viewportWidth = 20f,
    viewportHeight = 20f
).apply {
    path(
        fill = SolidColor(Color(0xFF7F13EC))
    ) {
        moveTo(13.3f, 14.7f)
        lineTo(14.7f, 13.3f)
        lineTo(11f, 9.6f)
        verticalLineTo(5f)
        horizontalLineTo(9f)
        verticalLineTo(10.4f)
        lineTo(13.3f, 14.7f)
        moveTo(10f, 20f)
        curveTo(8.617f, 20f, 7.317f, 19.737f, 6.1f, 19.212f)
        curveTo(4.883f, 18.688f, 3.825f, 17.975f, 2.925f, 17.075f)
        curveTo(2.025f, 16.175f, 1.313f, 15.117f, 0.788f, 13.9f)
        curveTo(0.262f, 12.683f, 0f, 11.383f, 0f, 10f)
        curveTo(0f, 8.617f, 0.262f, 7.317f, 0.788f, 6.1f)
        curveTo(1.313f, 4.883f, 2.025f, 3.825f, 2.925f, 2.925f)
        curveTo(3.825f, 2.025f, 4.883f, 1.313f, 6.1f, 0.788f)
        curveTo(7.317f, 0.262f, 8.617f, 0f, 10f, 0f)
        curveTo(11.383f, 0f, 12.683f, 0.262f, 13.9f, 0.788f)
        curveTo(15.117f, 1.313f, 16.175f, 2.025f, 17.075f, 2.925f)
        curveTo(17.975f, 3.825f, 18.688f, 4.883f, 19.212f, 6.1f)
        curveTo(19.737f, 7.317f, 20f, 8.617f, 20f, 10f)
        curveTo(20f, 11.383f, 19.737f, 12.683f, 19.212f, 13.9f)
        curveTo(18.688f, 15.117f, 17.975f, 16.175f, 17.075f, 17.075f)
        curveTo(16.175f, 17.975f, 15.117f, 18.688f, 13.9f, 19.212f)
        curveTo(12.683f, 19.737f, 11.383f, 20f, 10f, 20f)
        moveTo(10f, 18f)
        curveTo(12.217f, 18f, 14.104f, 17.221f, 15.663f, 15.663f)
        curveTo(17.221f, 14.104f, 18f, 12.217f, 18f, 10f)
        curveTo(18f, 7.783f, 17.221f, 5.896f, 15.663f, 4.338f)
        curveTo(14.104f, 2.779f, 12.217f, 2f, 10f, 2f)
        curveTo(7.783f, 2f, 5.896f, 2.779f, 4.338f, 4.338f)
        curveTo(2.779f, 5.896f, 2f, 7.783f, 2f, 10f)
        curveTo(2f, 12.217f, 2.779f, 14.104f, 4.338f, 15.663f)
        curveTo(5.896f, 17.221f, 7.783f, 18f, 10f, 18f)
    }
}.build()
