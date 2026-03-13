package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AnalyticsUnSelectedIcon: ImageVector = ImageVector.Builder(
    name = "AnalyticsSelectedIcon",
    defaultWidth = 17.dp,
    defaultHeight = 17.dp,
    viewportWidth = 17f,
    viewportHeight = 17f
).apply {
    path(
        fill = SolidColor(Color(0xFF94A3B8)),
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(2.016f, 16.031f)
        curveTo(3.094f, 16.031f, 4.031f, 15.094f, 4.031f, 14.016f)
        verticalLineTo(7.031f)
        curveTo(4.031f, 5.906f, 3.094f, 5.016f, 2.016f, 5.016f)
        curveTo(0.938f, 5.016f, 0f, 5.906f, 0f, 7.031f)
        verticalLineTo(14.016f)
        curveTo(0f, 15.094f, 0.938f, 16.031f, 2.016f, 16.031f)
        close()

        moveTo(12f, 11.016f)
        verticalLineTo(14.016f)
        curveTo(12f, 15.094f, 12.938f, 16.031f, 14.016f, 16.031f)
        curveTo(15.094f, 16.031f, 16.031f, 15.094f, 16.031f, 14.016f)
        verticalLineTo(11.016f)
        curveTo(16.031f, 9.938f, 15.094f, 9f, 14.016f, 9f)
        curveTo(12.938f, 9f, 12f, 9.938f, 12f, 11.016f)
        close()

        moveTo(8.016f, 16.031f)
        curveTo(9.094f, 16.031f, 10.031f, 15.094f, 10.031f, 14.016f)
        verticalLineTo(2.016f)
        curveTo(10.031f, 0.938f, 9.094f, 0f, 8.016f, 0f)
        curveTo(6.938f, 0f, 6f, 0.938f, 6f, 2.016f)
        verticalLineTo(14.016f)
        curveTo(6f, 15.094f, 6.938f, 16.031f, 8.016f, 16.031f)
        close()
    }
}.build()