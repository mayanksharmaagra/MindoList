package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IvBack: ImageVector = ImageVector.Builder(
    name = "IvBack",
    defaultWidth = 20.dp,
    defaultHeight = 20.dp,
    viewportWidth = 20f,
    viewportHeight = 20f
).apply {
    path(
        fill = SolidColor(Color(0xFF1C1B1F))
    ) {
        moveTo(6.521f, 10.833f)
        lineTo(11.188f, 15.5f)
        lineTo(10f, 16.667f)
        lineTo(3.333f, 10f)
        lineTo(10f, 3.333f)
        lineTo(11.188f, 4.5f)
        lineTo(6.521f, 9.167f)
        horizontalLineTo(16.667f)
        verticalLineTo(10.833f)
        horizontalLineTo(6.521f)
    }
}.build()
