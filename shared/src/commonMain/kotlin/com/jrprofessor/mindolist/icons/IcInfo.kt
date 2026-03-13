package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val IcInfo: ImageVector = ImageVector.Builder(
    name = "IcInfo",
    defaultWidth = 4.dp,
    defaultHeight = 15.dp,
    viewportWidth = 4f,
    viewportHeight = 15f
).apply {
    path(
        fill = SolidColor(Color(0xFF7F13EC))
    ) {
        moveTo(1.667f, 15f)
        curveTo(1.208f, 15f, 0.816f, 14.837f, 0.49f, 14.51f)
        curveTo(0.163f, 14.184f, 0f, 13.792f, 0f, 13.333f)
        curveTo(0f, 12.875f, 0.163f, 12.483f, 0.49f, 12.156f)
        curveTo(0.816f, 11.83f, 1.208f, 11.667f, 1.667f, 11.667f)
        curveTo(2.125f, 11.667f, 2.517f, 11.83f, 2.844f, 12.156f)
        curveTo(3.17f, 12.483f, 3.333f, 12.875f, 3.333f, 13.333f)
        curveTo(3.333f, 13.792f, 3.17f, 14.184f, 2.844f, 14.51f)
        curveTo(2.517f, 14.837f, 2.125f, 15f, 1.667f, 15f)
        verticalLineTo(15f)
        moveTo(0f, 10f)
        verticalLineTo(0f)
        horizontalLineTo(3.333f)
        verticalLineTo(10f)
        horizontalLineTo(0f)
        verticalLineTo(10f)
    }
}.build()
