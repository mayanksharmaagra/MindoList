package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcBell: ImageVector = ImageVector.Builder(
    name = "IcBell",
    defaultWidth = 14.dp,
    defaultHeight = 17.dp,
    viewportWidth = 14f,
    viewportHeight = 17f
).apply {
    path(
        fill = SolidColor(Color(0xFF7F13EC))
    ) {
        moveTo(0f, 14.167f)
        verticalLineTo(12.5f)
        horizontalLineTo(1.667f)
        verticalLineTo(6.667f)
        curveTo(1.667f, 5.514f, 2.014f, 4.49f, 2.708f, 3.594f)
        curveTo(3.403f, 2.698f, 4.306f, 2.111f, 5.417f, 1.833f)
        verticalLineTo(1.25f)
        curveTo(5.417f, 0.903f, 5.538f, 0.608f, 5.781f, 0.365f)
        curveTo(6.024f, 0.122f, 6.319f, 0f, 6.667f, 0f)
        curveTo(7.014f, 0f, 7.309f, 0.122f, 7.552f, 0.365f)
        curveTo(7.795f, 0.608f, 7.917f, 0.903f, 7.917f, 1.25f)
        verticalLineTo(1.833f)
        curveTo(9.028f, 2.111f, 9.931f, 2.698f, 10.625f, 3.594f)
        curveTo(11.319f, 4.49f, 11.667f, 5.514f, 11.667f, 6.667f)
        verticalLineTo(12.5f)
        horizontalLineTo(13.333f)
        verticalLineTo(14.167f)
        horizontalLineTo(0f)
        moveTo(6.667f, 16.667f)
        curveTo(6.208f, 16.667f, 5.816f, 16.503f, 5.49f, 16.177f)
        curveTo(5.163f, 15.851f, 5f, 15.458f, 5f, 15f)
        horizontalLineTo(8.333f)
        curveTo(8.333f, 15.458f, 8.17f, 15.851f, 7.844f, 16.177f)
        curveTo(7.517f, 16.503f, 7.125f, 16.667f, 6.667f, 16.667f)
        moveTo(3.333f, 12.5f)
        horizontalLineTo(10f)
        verticalLineTo(6.667f)
        curveTo(10f, 5.75f, 9.674f, 4.965f, 9.021f, 4.313f)
        curveTo(8.368f, 3.66f, 7.583f, 3.333f, 6.667f, 3.333f)
        curveTo(5.75f, 3.333f, 4.965f, 3.66f, 4.313f, 4.313f)
        curveTo(3.66f, 4.965f, 3.333f, 5.75f, 3.333f, 6.667f)
        verticalLineTo(12.5f)
    }
}.build()
