package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcCalendar: ImageVector = ImageVector.Builder(
    name = "IcCalendar",
    defaultWidth = 15.dp,
    defaultHeight = 17.dp,
    viewportWidth = 15f,
    viewportHeight = 17f
).apply {
    path(
        fill = SolidColor(Color(0xFF7F13EC))
    ) {
        moveTo(1.667f, 16.667f)
        curveTo(1.208f, 16.667f, 0.816f, 16.503f, 0.49f, 16.177f)
        curveTo(0.163f, 15.851f, 0f, 15.458f, 0f, 15f)
        verticalLineTo(3.333f)
        curveTo(0f, 2.875f, 0.163f, 2.483f, 0.49f, 2.156f)
        curveTo(0.816f, 1.83f, 1.208f, 1.667f, 1.667f, 1.667f)
        horizontalLineTo(2.5f)
        verticalLineTo(0f)
        horizontalLineTo(4.167f)
        verticalLineTo(1.667f)
        horizontalLineTo(10.833f)
        verticalLineTo(0f)
        horizontalLineTo(12.5f)
        verticalLineTo(1.667f)
        horizontalLineTo(13.333f)
        curveTo(13.792f, 1.667f, 14.184f, 1.83f, 14.51f, 2.156f)
        curveTo(14.837f, 2.483f, 15f, 2.875f, 15f, 3.333f)
        verticalLineTo(15f)
        curveTo(15f, 15.458f, 14.837f, 15.851f, 14.51f, 16.177f)
        curveTo(14.184f, 16.503f, 13.792f, 16.667f, 13.333f, 16.667f)
        horizontalLineTo(1.667f)
        moveTo(1.667f, 15f)
        horizontalLineTo(13.333f)
        verticalLineTo(6.667f)
        horizontalLineTo(1.667f)
        verticalLineTo(15f)
        moveTo(1.667f, 5f)
        horizontalLineTo(13.333f)
        verticalLineTo(3.333f)
        horizontalLineTo(1.667f)
        verticalLineTo(5f)
        moveTo(1.667f, 5f)
        verticalLineTo(3.333f)
        verticalLineTo(5f)
    }
}.build()
