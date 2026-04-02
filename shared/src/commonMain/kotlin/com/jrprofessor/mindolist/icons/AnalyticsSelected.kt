package com.jrprofessor.mindolist.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icons.AnalyticsSelected: ImageVector
    get() {
        if (_analyticsunselected != null) {
            return _analyticsunselected!!
        }
        _analyticsunselected =
            Builder(
                    name = "Analyticsunselected",
                    defaultWidth = 22.0.dp,
                    defaultHeight = 21.0.dp,
                    viewportWidth = 22.0f,
                    viewportHeight = 21.0f,
                )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFF3B82F6)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(2.0f, 17.0f)
                        curveTo(1.45f, 17.0f, 0.979f, 16.804f, 0.587f, 16.413f)
                        curveTo(0.196f, 16.021f, 0.0f, 15.55f, 0.0f, 15.0f)
                        curveTo(0.0f, 14.45f, 0.196f, 13.979f, 0.587f, 13.587f)
                        curveTo(0.979f, 13.196f, 1.45f, 13.0f, 2.0f, 13.0f)
                        curveTo(2.1f, 13.0f, 2.188f, 13.0f, 2.263f, 13.0f)
                        curveTo(2.338f, 13.0f, 2.417f, 13.017f, 2.5f, 13.05f)
                        lineTo(7.05f, 8.5f)
                        curveTo(7.017f, 8.417f, 7.0f, 8.337f, 7.0f, 8.262f)
                        curveTo(7.0f, 8.188f, 7.0f, 8.1f, 7.0f, 8.0f)
                        curveTo(7.0f, 7.45f, 7.196f, 6.979f, 7.588f, 6.588f)
                        curveTo(7.979f, 6.196f, 8.45f, 6.0f, 9.0f, 6.0f)
                        curveTo(9.55f, 6.0f, 10.021f, 6.196f, 10.413f, 6.588f)
                        curveTo(10.804f, 6.979f, 11.0f, 7.45f, 11.0f, 8.0f)
                        curveTo(11.0f, 8.033f, 10.983f, 8.2f, 10.95f, 8.5f)
                        lineTo(13.5f, 11.05f)
                        curveTo(13.583f, 11.017f, 13.663f, 11.0f, 13.738f, 11.0f)
                        curveTo(13.813f, 11.0f, 13.9f, 11.0f, 14.0f, 11.0f)
                        curveTo(14.1f, 11.0f, 14.188f, 11.0f, 14.262f, 11.0f)
                        curveTo(14.337f, 11.0f, 14.417f, 11.017f, 14.5f, 11.05f)
                        lineTo(18.05f, 7.5f)
                        curveTo(18.017f, 7.417f, 18.0f, 7.338f, 18.0f, 7.262f)
                        curveTo(18.0f, 7.188f, 18.0f, 7.1f, 18.0f, 7.0f)
                        curveTo(18.0f, 6.45f, 18.196f, 5.979f, 18.587f, 5.588f)
                        curveTo(18.979f, 5.196f, 19.45f, 5.0f, 20.0f, 5.0f)
                        curveTo(20.55f, 5.0f, 21.021f, 5.196f, 21.413f, 5.588f)
                        curveTo(21.804f, 5.979f, 22.0f, 6.45f, 22.0f, 7.0f)
                        curveTo(22.0f, 7.55f, 21.804f, 8.021f, 21.413f, 8.413f)
                        curveTo(21.021f, 8.804f, 20.55f, 9.0f, 20.0f, 9.0f)
                        curveTo(19.9f, 9.0f, 19.813f, 9.0f, 19.737f, 9.0f)
                        curveTo(19.663f, 9.0f, 19.583f, 8.983f, 19.5f, 8.95f)
                        lineTo(15.95f, 12.5f)
                        curveTo(15.983f, 12.583f, 16.0f, 12.663f, 16.0f, 12.738f)
                        curveTo(16.0f, 12.813f, 16.0f, 12.9f, 16.0f, 13.0f)
                        curveTo(16.0f, 13.55f, 15.804f, 14.021f, 15.413f, 14.413f)
                        curveTo(15.021f, 14.804f, 14.55f, 15.0f, 14.0f, 15.0f)
                        curveTo(13.45f, 15.0f, 12.979f, 14.804f, 12.587f, 14.413f)
                        curveTo(12.196f, 14.021f, 12.0f, 13.55f, 12.0f, 13.0f)
                        curveTo(12.0f, 12.9f, 12.0f, 12.813f, 12.0f, 12.738f)
                        curveTo(12.0f, 12.663f, 12.017f, 12.583f, 12.05f, 12.5f)
                        lineTo(9.5f, 9.95f)
                        curveTo(9.417f, 9.983f, 9.337f, 10.0f, 9.262f, 10.0f)
                        curveTo(9.188f, 10.0f, 9.1f, 10.0f, 9.0f, 10.0f)
                        curveTo(8.967f, 10.0f, 8.8f, 9.983f, 8.5f, 9.95f)
                        lineTo(3.95f, 14.5f)
                        curveTo(3.983f, 14.583f, 4.0f, 14.663f, 4.0f, 14.738f)
                        curveTo(4.0f, 14.813f, 4.0f, 14.9f, 4.0f, 15.0f)
                        curveTo(4.0f, 15.55f, 3.804f, 16.021f, 3.412f, 16.413f)
                        curveTo(3.021f, 16.804f, 2.55f, 17.0f, 2.0f, 17.0f)
                        verticalLineTo(17.0f)
                        moveTo(3.0f, 6.975f)
                        lineTo(2.375f, 5.625f)
                        lineTo(1.025f, 5.0f)
                        lineTo(2.375f, 4.375f)
                        lineTo(3.0f, 3.025f)
                        lineTo(3.625f, 4.375f)
                        lineTo(4.975f, 5.0f)
                        lineTo(3.625f, 5.625f)
                        lineTo(3.0f, 6.975f)
                        verticalLineTo(6.975f)
                        moveTo(14.0f, 6.0f)
                        lineTo(13.05f, 3.95f)
                        lineTo(11.0f, 3.0f)
                        lineTo(13.05f, 2.05f)
                        lineTo(14.0f, 0.0f)
                        lineTo(14.95f, 2.05f)
                        lineTo(17.0f, 3.0f)
                        lineTo(14.95f, 3.95f)
                        lineTo(14.0f, 6.0f)
                        verticalLineTo(6.0f)
                    }
                }
                .build()
        return _analyticsunselected!!
    }

private var _analyticsunselected: ImageVector? = null
