package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * "M + check" logo mark as a Compose ImageVector.
 *
 * Built from three rounded stroke paths on a 200x200 viewport:
 *  - dark indigo   : left leg of the M
 *  - mid violet    : right diagonal + right leg of the M
 *  - amber         : the checkmark, drawn last so it sits on top of the M
 *
 * Usage:
 *   Icon(imageVector = LogoMCheck, contentDescription = "TaskLedger logo", tint = Color.Unspecified)
 *   (pass tint = Color.Unspecified so Icon doesn't flatten the multi-color paths to one color)
 */
val AppLogo: ImageVector
    get() {
        if (_LogoMCheck != null) return _LogoMCheck!!
        _LogoMCheck = ImageVector.Builder(
            name = "LogoMCheck",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 200f,
            viewportHeight = 200f
        ).apply {

            // Left leg of the M — dark indigo
            path(
                stroke = SolidColor(Color(0xFF3E2E82)),
                strokeLineWidth = 22f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(40f, 150f)
                lineTo(40f, 62f)
                lineTo(100f, 122f)
            }

            // Right diagonal + right leg of the M — mid violet
            path(
                stroke = SolidColor(Color(0xFF7C5CF0)),
                strokeLineWidth = 22f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(100f, 122f)
                lineTo(160f, 62f)
                lineTo(160f, 150f)
            }

            // Amber checkmark — drawn last so it overlaps the M's middle vertex
            path(
                stroke = SolidColor(Color(0xFFE8A94C)),
                strokeLineWidth = 24f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(70f, 118f)
                lineTo(96f, 144f)
                lineTo(150f, 90f)
            }
        }.build()
        return _LogoMCheck!!
    }

private var _LogoMCheck: ImageVector? = null