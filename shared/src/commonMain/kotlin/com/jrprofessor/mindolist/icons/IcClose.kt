package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcClose: ImageVector = ImageVector.Builder(
    name = "IcClose",
    defaultWidth = 40.dp,
    defaultHeight = 40.dp,
    viewportWidth = 40f,
    viewportHeight = 40f
).apply {
    path(
        fill = SolidColor(Color(0xFF0F172A))
    ) {
        moveTo(14.4f, 27f)
        lineTo(13f, 25.6f)
        lineTo(18.6f, 20f)
        lineTo(13f, 14.4f)
        lineTo(14.4f, 13f)
        lineTo(20f, 18.6f)
        lineTo(25.6f, 13f)
        lineTo(27f, 14.4f)
        lineTo(21.4f, 20f)
        lineTo(27f, 25.6f)
        lineTo(25.6f, 27f)
        lineTo(20f, 21.4f)
        lineTo(14.4f, 27f)
        verticalLineTo(27f)
    }
}.build()
