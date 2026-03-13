package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcUser: ImageVector = ImageVector.Builder(
    name = "IcUser",
    defaultWidth = 800.dp,
    defaultHeight = 800.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color.Transparent)
    ) {
        moveTo(0f, 0f)
        horizontalLineToRelative(24f)
        verticalLineToRelative(24f)
        horizontalLineToRelative(-24f)
    }

    path(
        fill = SolidColor(Color(0xFF7C6FEE)),
        pathFillType = PathFillType.EvenOdd
    ) {
        moveTo(6f, 8f)
        curveTo(6f, 4.686f, 8.686f, 2f, 12f, 2f)
        curveTo(15.314f, 2f, 18f, 4.686f, 18f, 8f)
        curveTo(18f, 11.314f, 15.314f, 14f, 12f, 14f)
        curveTo(8.686f, 14f, 6f, 11.314f, 6f, 8f)
    }

    path(
        fill = SolidColor(Color(0xFF7C6FEE)),
        pathFillType = PathFillType.EvenOdd
    ) {
        moveTo(5.431f, 16.903f)
        curveTo(7.056f, 16.221f, 9.223f, 16f, 12f, 16f)
        curveTo(14.771f, 16f, 16.935f, 16.22f, 18.559f, 16.898f)
        curveTo(20.301f, 17.625f, 21.371f, 18.861f, 21.941f, 20.659f)
        curveTo(22.153f, 21.327f, 21.652f, 22f, 20.959f, 22f)
        horizontalLineTo(3.035f)
        curveTo(2.345f, 22f, 1.847f, 21.33f, 2.057f, 20.665f)
        curveTo(2.625f, 18.868f, 3.691f, 17.632f, 5.431f, 16.903f)
    }
}.build()
