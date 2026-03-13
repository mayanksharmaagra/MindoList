package com.jrprofessor.mindolist.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// NOTE: Original XML has gradients which are not supported in ImageVector.
// Gradient paths are replaced with flat colors approximated from gradient stops.
val WelcomeLogo: ImageVector = ImageVector.Builder(
    name = "WelcomeLogo",
    defaultWidth = 200.dp,
    defaultHeight = 200.dp,
    viewportWidth = 200f,
    viewportHeight = 200f
).apply {
    // White rounded rectangle background
    path(fill = SolidColor(Color(0xFFFFFFFF))) {
        moveTo(50f, 20f)
        lineTo(150f, 20f)
        arcTo(30f, 30f, 0f, false, true, 180f, 50f)
        lineTo(180f, 150f)
        arcTo(30f, 30f, 0f, false, true, 150f, 180f)
        lineTo(50f, 180f)
        arcTo(30f, 30f, 0f, false, true, 20f, 150f)
        lineTo(20f, 50f)
        arcTo(30f, 30f, 0f, false, true, 50f, 20f)
        close()
    }
    // Purple gradient header bar (approximated as flat #7C6FEE)
    path(fill = SolidColor(Color(0xFF7C6FEE))) {
        moveTo(50f, 20f)
        lineTo(150f, 20f)
        arcTo(30f, 25f, 0f, false, true, 180f, 45f)
        lineTo(180f, 45f)
        arcTo(30f, 25f, 0f, false, true, 150f, 70f)
        lineTo(50f, 70f)
        arcTo(30f, 25f, 0f, false, true, 20f, 45f)
        lineTo(20f, 45f)
        arcTo(30f, 25f, 0f, false, true, 50f, 20f)
        close()
    }
    // Purple band
    path(fill = SolidColor(Color(0xFF7C6FEE))) {
        moveTo(20f, 50f)
        horizontalLineToRelative(160f)
        verticalLineToRelative(20f)
        horizontalLineToRelative(-160f)
        close()
    }
    // Green checkbox 1
    path(fill = SolidColor(Color(0xFF48BB78))) {
        moveTo(45f, 90f)
        lineTo(55f, 90f)
        arcTo(5f, 5f, 0f, false, true, 60f, 95f)
        lineTo(60f, 105f)
        arcTo(5f, 5f, 0f, false, true, 55f, 110f)
        lineTo(45f, 110f)
        arcTo(5f, 5f, 0f, false, true, 40f, 105f)
        lineTo(40f, 95f)
        arcTo(5f, 5f, 0f, false, true, 45f, 90f)
        close()
    }
    // Checkmark 1
    path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color.White),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round
    ) {
        moveTo(45f, 100f)
        lineTo(49f, 104f)
        lineTo(55f, 95f)
    }
    // Grey line 1
    path(fill = SolidColor(Color(0xFFCBD5E0))) {
        moveTo(74f, 93f)
        lineTo(156f, 93f)
        arcTo(4f, 4f, 0f, false, true, 160f, 97f)
        lineTo(160f, 97f)
        arcTo(4f, 4f, 0f, false, true, 156f, 101f)
        lineTo(74f, 101f)
        arcTo(4f, 4f, 0f, false, true, 70f, 97f)
        lineTo(70f, 97f)
        arcTo(4f, 4f, 0f, false, true, 74f, 93f)
        close()
    }
    // Green checkbox 2
    path(fill = SolidColor(Color(0xFF48BB78))) {
        moveTo(45f, 120f)
        lineTo(55f, 120f)
        arcTo(5f, 5f, 0f, false, true, 60f, 125f)
        lineTo(60f, 135f)
        arcTo(5f, 5f, 0f, false, true, 55f, 140f)
        lineTo(45f, 140f)
        arcTo(5f, 5f, 0f, false, true, 40f, 135f)
        lineTo(40f, 125f)
        arcTo(5f, 5f, 0f, false, true, 45f, 120f)
        close()
    }
    // Checkmark 2
    path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color.White),
        strokeLineWidth = 2f,
        strokeLineCap = StrokeCap.Round
    ) {
        moveTo(45f, 130f)
        lineTo(49f, 134f)
        lineTo(55f, 125f)
    }
    // Grey line 2
    path(fill = SolidColor(Color(0xFFCBD5E0))) {
        moveTo(74f, 123f)
        lineTo(156f, 123f)
        arcTo(4f, 4f, 0f, false, true, 160f, 127f)
        lineTo(160f, 127f)
        arcTo(4f, 4f, 0f, false, true, 156f, 131f)
        lineTo(74f, 131f)
        arcTo(4f, 4f, 0f, false, true, 70f, 127f)
        lineTo(70f, 127f)
        arcTo(4f, 4f, 0f, false, true, 74f, 123f)
        close()
    }
    // Empty checkbox (outline only)
    path(
        fill = SolidColor(Color.Transparent),
        stroke = SolidColor(Color(0xFFCBD5E0)),
        strokeLineWidth = 2f
    ) {
        moveTo(45f, 150f)
        lineTo(55f, 150f)
        arcTo(5f, 5f, 0f, false, true, 60f, 155f)
        lineTo(60f, 165f)
        arcTo(5f, 5f, 0f, false, true, 55f, 170f)
        lineTo(45f, 170f)
        arcTo(5f, 5f, 0f, false, true, 40f, 165f)
        lineTo(40f, 155f)
        arcTo(5f, 5f, 0f, false, true, 45f, 150f)
        close()
    }
    // Light grey line 3
    path(fill = SolidColor(Color(0xFFE2E8F0))) {
        moveTo(74f, 153f)
        lineTo(156f, 153f)
        arcTo(4f, 4f, 0f, false, true, 160f, 157f)
        lineTo(160f, 157f)
        arcTo(4f, 4f, 0f, false, true, 156f, 161f)
        lineTo(74f, 161f)
        arcTo(4f, 4f, 0f, false, true, 70f, 157f)
        lineTo(70f, 157f)
        arcTo(4f, 4f, 0f, false, true, 74f, 153f)
        close()
    }
}.build()
