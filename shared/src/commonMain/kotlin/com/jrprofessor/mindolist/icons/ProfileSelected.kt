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

public val Icons.ProfileSelected: ImageVector
    get() {
        if (_profileUnselected != null) {
            return _profileUnselected!!
        }
        _profileUnselected =
            Builder(
                    name = "ProfileSelected",
                    defaultWidth = 16.0.dp,
                    defaultHeight = 20.0.dp,
                    viewportWidth = 16.0f,
                    viewportHeight = 20.0f,
                )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFF5A5B5F)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(8.0f, 8.0f)
                        curveTo(6.9f, 8.0f, 5.958f, 7.608f, 5.175f, 6.825f)
                        curveTo(4.392f, 6.042f, 4.0f, 5.1f, 4.0f, 4.0f)
                        curveTo(4.0f, 2.9f, 4.392f, 1.958f, 5.175f, 1.175f)
                        curveTo(5.958f, 0.392f, 6.9f, 0.0f, 8.0f, 0.0f)
                        curveTo(9.1f, 0.0f, 10.042f, 0.392f, 10.825f, 1.175f)
                        curveTo(11.608f, 1.958f, 12.0f, 2.9f, 12.0f, 4.0f)
                        curveTo(12.0f, 5.1f, 11.608f, 6.042f, 10.825f, 6.825f)
                        curveTo(10.042f, 7.608f, 9.1f, 8.0f, 8.0f, 8.0f)
                        verticalLineTo(8.0f)
                        moveTo(0.0f, 16.0f)
                        verticalLineTo(13.2f)
                        curveTo(0.0f, 12.633f, 0.146f, 12.113f, 0.438f, 11.637f)
                        curveTo(0.729f, 11.163f, 1.117f, 10.8f, 1.6f, 10.55f)
                        curveTo(2.633f, 10.033f, 3.683f, 9.646f, 4.75f, 9.387f)
                        curveTo(5.817f, 9.129f, 6.9f, 9.0f, 8.0f, 9.0f)
                        curveTo(9.1f, 9.0f, 10.183f, 9.129f, 11.25f, 9.387f)
                        curveTo(12.317f, 9.646f, 13.367f, 10.033f, 14.4f, 10.55f)
                        curveTo(14.883f, 10.8f, 15.271f, 11.163f, 15.563f, 11.637f)
                        curveTo(15.854f, 12.113f, 16.0f, 12.633f, 16.0f, 13.2f)
                        verticalLineTo(16.0f)
                        horizontalLineTo(0.0f)
                        verticalLineTo(16.0f)
                        moveTo(2.0f, 14.0f)
                        horizontalLineTo(14.0f)
                        verticalLineTo(13.2f)
                        curveTo(14.0f, 13.017f, 13.954f, 12.85f, 13.863f, 12.7f)
                        curveTo(13.771f, 12.55f, 13.65f, 12.433f, 13.5f, 12.35f)
                        curveTo(12.6f, 11.9f, 11.692f, 11.563f, 10.775f, 11.337f)
                        curveTo(9.858f, 11.113f, 8.933f, 11.0f, 8.0f, 11.0f)
                        curveTo(7.067f, 11.0f, 6.142f, 11.113f, 5.225f, 11.337f)
                        curveTo(4.308f, 11.563f, 3.4f, 11.9f, 2.5f, 12.35f)
                        curveTo(2.35f, 12.433f, 2.229f, 12.55f, 2.138f, 12.7f)
                        curveTo(2.046f, 12.85f, 2.0f, 13.017f, 2.0f, 13.2f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        moveTo(8.0f, 6.0f)
                        curveTo(8.55f, 6.0f, 9.021f, 5.804f, 9.413f, 5.412f)
                        curveTo(9.804f, 5.021f, 10.0f, 4.55f, 10.0f, 4.0f)
                        curveTo(10.0f, 3.45f, 9.804f, 2.979f, 9.413f, 2.588f)
                        curveTo(9.021f, 2.196f, 8.55f, 2.0f, 8.0f, 2.0f)
                        curveTo(7.45f, 2.0f, 6.979f, 2.196f, 6.588f, 2.588f)
                        curveTo(6.196f, 2.979f, 6.0f, 3.45f, 6.0f, 4.0f)
                        curveTo(6.0f, 4.55f, 6.196f, 5.021f, 6.588f, 5.412f)
                        curveTo(6.979f, 5.804f, 7.45f, 6.0f, 8.0f, 6.0f)
                        verticalLineTo(6.0f)
                        moveTo(8.0f, 4.0f)
                        verticalLineTo(4.0f)
                        verticalLineTo(4.0f)
                        verticalLineTo(4.0f)
                        verticalLineTo(4.0f)
                        verticalLineTo(4.0f)
                        verticalLineTo(4.0f)
                        verticalLineTo(4.0f)
                        verticalLineTo(4.0f)
                        verticalLineTo(4.0f)
                        moveTo(8.0f, 14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                        verticalLineTo(14.0f)
                    }
                }
                .build()
        return _profileUnselected!!
    }

private var _profileUnselected: ImageVector? = null

@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = Icons.ProfileUnselected, contentDescription = null)
    }
}
