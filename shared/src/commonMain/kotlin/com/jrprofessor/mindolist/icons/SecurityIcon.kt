// @ImageVectorWizard:vectorXml=PHZlY3RvciB4bWxuczphbmRyb2lkPSJodHRwOi8vc2NoZW1hcy5hbmRyb2lkLmNvbS9hcGsvcmVzL2FuZHJvaWQiCiAgICBhbmRyb2lkOndpZHRoPSIxNmRwIgogICAgYW5kcm9pZDpoZWlnaHQ9IjIwZHAiCiAgICBhbmRyb2lkOnZpZXdwb3J0V2lkdGg9IjE2IgogICAgYW5kcm9pZDp2aWV3cG9ydEhlaWdodD0iMjAiPgogIDxwYXRoCiAgICAgIGFuZHJvaWQ6cGF0aERhdGE9Ik04LDIwQzUuNjgzLDE5LjQxNyAzLjc3MSwxOC4wODcgMi4yNjMsMTYuMDEzQzAuNzU0LDEzLjkzOCAwLDExLjYzMyAwLDkuMVYzTDgsMEwxNiwzVjkuMUMxNiwxMS42MzMgMTUuMjQ2LDEzLjkzOCAxMy43MzgsMTYuMDEzQzEyLjIyOSwxOC4wODcgMTAuMzE3LDE5LjQxNyA4LDIwWk04LDE3LjlDOS42MTcsMTcuNCAxMC45NjcsMTYuNDEzIDEyLjA1LDE0LjkzOEMxMy4xMzMsMTMuNDYyIDEzLjc2NywxMS44MTcgMTMuOTUsMTBIOFYyLjEyNUwyLDQuMzc1VjkuMUMyLDkuMjgzIDIsOS40MzMgMiw5LjU1QzIsOS42NjcgMi4wMTcsOS44MTcgMi4wNSwxMEg4VjE3LjlaIgogICAgICBhbmRyb2lkOmZpbGxDb2xvcj0iIzdGMTNFQyIvPgo8L3ZlY3Rvcj4K
package com.jrprofessor.mindolist.icons

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Icons.SecurityIcon: ImageVector
    get() {
        if (_securityIcon != null) {
            return _securityIcon!!
        }
        _securityIcon =
            Builder(
                    name = "SecurityIcon",
                    defaultWidth = 16.0.dp,
                    defaultHeight = 20.0.dp,
                    viewportWidth = 16.0f,
                    viewportHeight = 20.0f,
                )
                .apply {
                    path(
                        fill = SolidColor(Color(0xFF7F13EC)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(8.0f, 20.0f)
                        curveTo(5.683f, 19.417f, 3.771f, 18.087f, 2.263f, 16.013f)
                        curveTo(0.754f, 13.938f, 0.0f, 11.633f, 0.0f, 9.1f)
                        verticalLineTo(3.0f)
                        lineTo(8.0f, 0.0f)
                        lineTo(16.0f, 3.0f)
                        verticalLineTo(9.1f)
                        curveTo(16.0f, 11.633f, 15.246f, 13.938f, 13.738f, 16.013f)
                        curveTo(12.229f, 18.087f, 10.317f, 19.417f, 8.0f, 20.0f)
                        close()
                        moveTo(8.0f, 17.9f)
                        curveTo(9.617f, 17.4f, 10.967f, 16.413f, 12.05f, 14.938f)
                        curveTo(13.133f, 13.462f, 13.767f, 11.817f, 13.95f, 10.0f)
                        horizontalLineTo(8.0f)
                        verticalLineTo(2.125f)
                        lineTo(2.0f, 4.375f)
                        verticalLineTo(9.1f)
                        curveTo(2.0f, 9.283f, 2.0f, 9.433f, 2.0f, 9.55f)
                        curveTo(2.0f, 9.667f, 2.017f, 9.817f, 2.05f, 10.0f)
                        horizontalLineTo(8.0f)
                        verticalLineTo(17.9f)
                        close()
                    }
                }
                .build()
        return _securityIcon!!
    }

private var _securityIcon: ImageVector? = null
