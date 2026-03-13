package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcCamera: ImageVector = ImageVector.Builder(
    name = "IcCamera",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f
).apply {
    path(
        fill = SolidColor(Color(0xFFFFFFFF))
    ) {
        moveTo(12f, 15.2f)
        curveToRelative(1.77f, 0f, 3.2f, -1.43f, 3.2f, -3.2f)
        reflectiveCurveToRelative(-1.43f, -3.2f, -3.2f, -3.2f)
        reflectiveCurveToRelative(-3.2f, 1.43f, -3.2f, 3.2f)
        reflectiveCurveToRelative(1.43f, 3.2f, 3.2f, 3.2f)
        moveTo(16f, 3.33f)
        curveToRelative(0f, -0.46f, -0.37f, -0.83f, -0.83f, -0.83f)
        horizontalLineToRelative(-2.34f)
        curveToRelative(-0.46f, 0f, -0.83f, 0.37f, -0.83f, 0.83f)
        lineTo(12f, 4f)
        horizontalLineToRelative(-2f)
        lineTo(10f, 3.33f)
        curveToRelative(0f, -0.46f, -0.37f, -0.83f, -0.83f, -0.83f)
        lineTo(6.83f, 2.5f)
        curveToRelative(-0.46f, 0f, -0.83f, 0.37f, -0.83f, 0.83f)
        lineTo(6f, 4f)
        lineTo(4f, 4f)
        curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
        verticalLineToRelative(12f)
        curveToRelative(0f, 1.1f, 0.9f, 2f, 2f, 2f)
        horizontalLineToRelative(16f)
        curveToRelative(1.1f, 0f, 2f, -0.9f, 2f, -2f)
        lineTo(22f, 6f)
        curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
        horizontalLineToRelative(-2f)
        lineTo(18f, 3.33f)
        moveTo(12f, 17f)
        curveToRelative(-2.76f, 0f, -5f, -2.24f, -5f, -5f)
        reflectiveCurveToRelative(2.24f, -5f, 5f, -5f)
        reflectiveCurveToRelative(5f, 2.24f, 5f, 5f)
        reflectiveCurveToRelative(-2.24f, 5f, -5f, 5f)
    }
}.build()
