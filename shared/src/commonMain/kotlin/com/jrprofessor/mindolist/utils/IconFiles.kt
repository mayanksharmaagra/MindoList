package com.jrprofessor.mindolist.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// ── Icon size: 48dp, no background, colorful strokes/fills ───────────────────

// ── 1. Personal ───────────────────────────────────────────────────────────────
val PersonalIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Personal",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Head circle
        path(
            fill = SolidColor(Color(0xFF818CF8)),
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(24f, 6f)
            arcTo(8f, 8f, 0f, false, true, 24f, 22f)
            arcTo(8f, 8f, 0f, false, true, 24f, 6f)
        }
        // Body
        path(
            stroke = SolidColor(Color(0xFF6366F1)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(10f, 42f)
            curveTo(10f, 33f, 16f, 26f, 24f, 26f)
            curveTo(32f, 26f, 38f, 33f, 38f, 42f)
        }
    }.build()

// ── 2. Work ───────────────────────────────────────────────────────────────────
val WorkIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Work",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Briefcase body
        path(
            fill = SolidColor(Color(0xFFBAE6FD)),
            stroke = SolidColor(Color(0xFF0EA5E9)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(8f, 18f)
            lineTo(8f, 38f)
            arcTo(2f, 2f, 0f, false, false, 10f, 40f)
            lineTo(38f, 40f)
            arcTo(2f, 2f, 0f, false, false, 40f, 38f)
            lineTo(40f, 18f)
            arcTo(2f, 2f, 0f, false, false, 38f, 16f)
            lineTo(10f, 16f)
            arcTo(2f, 2f, 0f, false, false, 8f, 18f)
            close()
        }
        // Handle
        path(
            stroke = SolidColor(Color(0xFF0EA5E9)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(18f, 16f)
            lineTo(18f, 12f)
            arcTo(2f, 2f, 0f, false, true, 20f, 10f)
            lineTo(28f, 10f)
            arcTo(2f, 2f, 0f, false, true, 30f, 12f)
            lineTo(30f, 16f)
        }
        // Middle line
        path(
            stroke = SolidColor(Color(0xFF0EA5E9)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(8f, 27f)
            lineTo(40f, 27f)
        }
    }.build()

// ── 3. Shopping ───────────────────────────────────────────────────────────────
val ShoppingIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Shopping",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Bag body
        path(
            fill = SolidColor(Color(0xFFFDE68A)),
            stroke = SolidColor(Color(0xFFF59E0B)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(12f, 18f)
            lineTo(36f, 18f)
            lineTo(38f, 40f)
            lineTo(10f, 40f)
            close()
        }
        // Handle
        path(
            stroke = SolidColor(Color(0xFFF59E0B)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(18f, 18f)
            lineTo(18f, 13f)
            arcTo(6f, 6f, 0f, false, true, 30f, 13f)
            lineTo(30f, 18f)
        }
        // Dots
        path(fill = SolidColor(Color(0xFFF59E0B))) {
            moveTo(20f, 27f)
            arcTo(1.5f, 1.5f, 0f, false, true, 20f, 30f)
            arcTo(1.5f, 1.5f, 0f, false, true, 20f, 27f)
        }
        path(fill = SolidColor(Color(0xFFF59E0B))) {
            moveTo(28f, 27f)
            arcTo(1.5f, 1.5f, 0f, false, true, 28f, 30f)
            arcTo(1.5f, 1.5f, 0f, false, true, 28f, 27f)
        }
    }.build()

// ── 4. Health ─────────────────────────────────────────────────────────────────
val HealthIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Health",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Heart
        path(
            fill = SolidColor(Color(0xFFFECDD3)),
            stroke = SolidColor(Color(0xFFE11D48)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(24f, 38f)
            curveTo(24f, 38f, 8f, 28f, 8f, 17f)
            arcTo(8f, 8f, 0f, false, true, 24f, 13f)
            arcTo(8f, 8f, 0f, false, true, 40f, 17f)
            curveTo(40f, 28f, 24f, 38f, 24f, 38f)
            close()
        }
        // Plus sign
        path(
            stroke = SolidColor(Color(0xFFE11D48)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(24f, 19f)
            lineTo(24f, 27f)
            moveTo(20f, 23f)
            lineTo(28f, 23f)
        }
    }.build()

// ── 5. Finance ────────────────────────────────────────────────────────────────
val FinanceIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Finance",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Coin circle
        path(
            fill = SolidColor(Color(0xFFFEF08A)),
            stroke = SolidColor(Color(0xFFCA8A04)),
            strokeLineWidth = 2.5f
        ) {
            moveTo(24f, 8f)
            arcTo(16f, 16f, 0f, false, true, 24f, 40f)
            arcTo(16f, 16f, 0f, false, true, 24f, 8f)
            close()
        }
        // Dollar sign
        path(
            stroke = SolidColor(Color(0xFFCA8A04)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(24f, 14f)
            lineTo(24f, 34f)
            moveTo(20f, 18f)
            curveTo(20f, 18f, 20f, 15f, 24f, 15f)
            curveTo(28f, 15f, 28f, 18f, 28f, 20f)
            curveTo(28f, 22f, 24f, 24f, 24f, 24f)
            curveTo(24f, 24f, 28f, 24f, 28f, 28f)
            curveTo(28f, 32f, 24f, 33f, 20f, 30f)
        }
    }.build()

// ── 6. Education ──────────────────────────────────────────────────────────────
val EducationIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Education",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Book body
        path(
            fill = SolidColor(Color(0xFFBBF7D0)),
            stroke = SolidColor(Color(0xFF16A34A)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(12f, 10f)
            lineTo(34f, 10f)
            arcTo(2f, 2f, 0f, false, true, 36f, 12f)
            lineTo(36f, 38f)
            arcTo(2f, 2f, 0f, false, true, 34f, 40f)
            lineTo(12f, 40f)
            arcTo(2f, 2f, 0f, false, false, 10f, 38f)
            lineTo(10f, 12f)
            arcTo(2f, 2f, 0f, false, true, 12f, 10f)
            close()
        }
        // Spine
        path(
            stroke = SolidColor(Color(0xFF16A34A)),
            strokeLineWidth = 2.5f
        ) {
            moveTo(17f, 10f)
            lineTo(17f, 40f)
        }
        // Lines
        path(
            stroke = SolidColor(Color(0xFF16A34A)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(22f, 18f)
            lineTo(31f, 18f)
            moveTo(22f, 24f)
            lineTo(31f, 24f)
            moveTo(22f, 30f)
            lineTo(28f, 30f)
        }
    }.build()

// ── 7. Travel ─────────────────────────────────────────────────────────────────
val TravelIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Travel",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Plane body
        path(
            fill = SolidColor(Color(0xFFE0E7FF)),
            stroke = SolidColor(Color(0xFF6366F1)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(8f, 24f)
            lineTo(20f, 18f)
            lineTo(38f, 10f)
            lineTo(30f, 28f)
            lineTo(20f, 28f)
            lineTo(16f, 38f)
            lineTo(12f, 30f)
            close()
        }
        // Wing
        path(
            fill = SolidColor(Color(0xFF6366F1))
        ) {
            moveTo(20f, 18f)
            lineTo(30f, 28f)
            lineTo(20f, 28f)
            close()
        }
    }.build()

// ── 8. Home ───────────────────────────────────────────────────────────────────
val HomeIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Home",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Roof
        path(
            fill = SolidColor(Color(0xFFFCA5A5)),
            stroke = SolidColor(Color(0xFFDC2626)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(24f, 8f)
            lineTo(40f, 22f)
            lineTo(8f, 22f)
            close()
        }
        // Walls
        path(
            fill = SolidColor(Color(0xFFFEE2E2)),
            stroke = SolidColor(Color(0xFFDC2626)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(12f, 22f)
            lineTo(12f, 40f)
            lineTo(36f, 40f)
            lineTo(36f, 22f)
            close()
        }
        // Door
        path(
            fill = SolidColor(Color(0xFFDC2626)),
            stroke = SolidColor(Color(0xFFDC2626)),
            strokeLineWidth = 1.5f
        ) {
            moveTo(20f, 40f)
            lineTo(20f, 30f)
            arcTo(4f, 4f, 0f, false, true, 28f, 30f)
            lineTo(28f, 40f)
            close()
        }
    }.build()

// ── 9. Family ─────────────────────────────────────────────────────────────────
val FamilyIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Family",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Adult 1 head
        path(fill = SolidColor(Color(0xFFFBBF24))) {
            moveTo(16f, 8f)
            arcTo(5f, 5f, 0f, false, true, 16f, 18f)
            arcTo(5f, 5f, 0f, false, true, 16f, 8f)
        }
        // Adult 1 body
        path(
            stroke = SolidColor(Color(0xFFF59E0B)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(8f, 42f)
            curveTo(8f, 35f, 11f, 30f, 16f, 30f)
            curveTo(21f, 30f, 24f, 35f, 24f, 42f)
        }
        // Adult 2 head
        path(fill = SolidColor(Color(0xFF818CF8))) {
            moveTo(32f, 8f)
            arcTo(5f, 5f, 0f, false, true, 32f, 18f)
            arcTo(5f, 5f, 0f, false, true, 32f, 8f)
        }
        // Adult 2 body
        path(
            stroke = SolidColor(Color(0xFF6366F1)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(24f, 42f)
            curveTo(24f, 35f, 27f, 30f, 32f, 30f)
            curveTo(37f, 30f, 40f, 35f, 40f, 42f)
        }
        // Kid head
        path(fill = SolidColor(Color(0xFF6EE7B7))) {
            moveTo(24f, 20f)
            arcTo(3.5f, 3.5f, 0f, false, true, 24f, 27f)
            arcTo(3.5f, 3.5f, 0f, false, true, 24f, 20f)
        }
    }.build()

// ── 10. Social ────────────────────────────────────────────────────────────────
val SocialIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Social",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Chat bubble 1
        path(
            fill = SolidColor(Color(0xFFDDD6FE)),
            stroke = SolidColor(Color(0xFF7C3AED)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(8f, 10f)
            arcTo(2f, 2f, 0f, false, true, 10f, 8f)
            lineTo(30f, 8f)
            arcTo(2f, 2f, 0f, false, true, 32f, 10f)
            lineTo(32f, 22f)
            arcTo(2f, 2f, 0f, false, true, 30f, 24f)
            lineTo(14f, 24f)
            lineTo(8f, 30f)
            lineTo(8f, 10f)
            close()
        }
        // Chat bubble 2
        path(
            fill = SolidColor(Color(0xFFC7D2FE)),
            stroke = SolidColor(Color(0xFF6366F1)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(18f, 26f)
            lineTo(36f, 26f)
            arcTo(2f, 2f, 0f, false, true, 38f, 28f)
            lineTo(38f, 38f)
            arcTo(2f, 2f, 0f, false, true, 36f, 40f)
            lineTo(22f, 40f)
            lineTo(16f, 46f)
            lineTo(16f, 28f)
            arcTo(2f, 2f, 0f, false, true, 18f, 26f)
            close()
        }
    }.build()

// ── 11. Meetings ──────────────────────────────────────────────────────────────
val MeetingsIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Meetings",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Calendar body
        path(
            fill = SolidColor(Color(0xFFE0F2FE)),
            stroke = SolidColor(Color(0xFF0284C7)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(8f, 14f)
            arcTo(2f, 2f, 0f, false, true, 10f, 12f)
            lineTo(38f, 12f)
            arcTo(2f, 2f, 0f, false, true, 40f, 14f)
            lineTo(40f, 40f)
            arcTo(2f, 2f, 0f, false, true, 38f, 42f)
            lineTo(10f, 42f)
            arcTo(2f, 2f, 0f, false, true, 8f, 40f)
            close()
        }
        // Top bar
        path(fill = SolidColor(Color(0xFF0284C7))) {
            moveTo(8f, 20f)
            lineTo(40f, 20f)
            lineTo(40f, 14f)
            arcTo(2f, 2f, 0f, false, false, 38f, 12f)
            lineTo(10f, 12f)
            arcTo(2f, 2f, 0f, false, false, 8f, 14f)
            close()
        }
        // Handle pins
        path(
            stroke = SolidColor(Color(0xFF0284C7)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(17f, 8f)
            lineTo(17f, 16f)
            moveTo(31f, 8f)
            lineTo(31f, 16f)
        }
        // Grid dots
        path(fill = SolidColor(Color(0xFF0284C7))) {
            moveTo(
                16f,
                27f
            )
            arcTo(2f, 2f, 0f, false, true, 16f, 31f)
            arcTo(2f, 2f, 0f, false, true, 16f, 27f)
            moveTo(
                24f,
                27f
            )
            arcTo(2f, 2f, 0f, false, true, 24f, 31f)
            arcTo(2f, 2f, 0f, false, true, 24f, 27f)
            moveTo(
                32f,
                27f
            )
            arcTo(2f, 2f, 0f, false, true, 32f, 31f)
            arcTo(2f, 2f, 0f, false, true, 32f, 27f)
        }
    }.build()

// ── 12. Projects ──────────────────────────────────────────────────────────────
val ProjectsIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Projects",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Bar 1
        path(
            fill = SolidColor(Color(0xFF6EE7B7)),
            stroke = SolidColor(Color(0xFF059669)),
            strokeLineWidth = 1.5f
        ) {
            moveTo(8f, 30f)
            lineTo(18f, 30f)
            lineTo(18f, 40f)
            lineTo(8f, 40f)
            close()
        }
        // Bar 2
        path(
            fill = SolidColor(Color(0xFF6EE7B7)),
            stroke = SolidColor(Color(0xFF059669)),
            strokeLineWidth = 1.5f
        ) {
            moveTo(20f, 20f)
            lineTo(28f, 20f)
            lineTo (28f, 40f)
            lineTo(20f, 40f)
            close()
        }
        // Bar 3
        path(
            fill = SolidColor(Color(0xFF059669)),
            stroke = SolidColor(Color(0xFF059669)),
            strokeLineWidth = 1.5f
        ) {
            moveTo(30f, 10f)
            lineTo (40f, 10f)
            lineTo(40f, 40f)
            lineTo(30f, 40f)
            close()
        }
        // Trend line
        path(
            stroke = SolidColor(Color(0xFFFBBF24)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(8f, 32f)
            lineTo (20f, 22f)
            lineTo(30f, 26f)
            lineTo(40f, 12f)
        }
    }.build()

// ── 13. Deadlines ─────────────────────────────────────────────────────────────
val DeadlinesIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Deadlines",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Clock circle
        path(
            fill = SolidColor(Color(0xFFFEE2E2)),
            stroke = SolidColor(Color(0xFFDC2626)),
            strokeLineWidth = 2.5f
        ) {
            moveTo(24f, 8f)
            arcTo(16f, 16f, 0f, false, true, 24f, 40f)
            arcTo(16f, 16f, 0f, false, true, 24f, 8f)
            close()
        }
        // Hour hand
        path(
            stroke = SolidColor(Color(0xFFDC2626)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(24f, 16f)
            lineTo (24f, 24f)
        }
        // Minute hand (urgent angle)
        path(
            stroke = SolidColor(Color(0xFF991B1B)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(24f, 24f)
            lineTo (32f, 28f)
        }
        // Exclamation
        path(
            stroke = SolidColor(Color(0xFFDC2626)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(24f, 43f)
            lineTo (24f, 45f)
            moveTo(24f, 42f)
            lineTo (24f, 42f)
        }
    }.build()

// ── 14. Goals ─────────────────────────────────────────────────────────────────
val GoalsIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Goals",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Outer ring
        path(
            fill = null,
            stroke = SolidColor(Color(0xFFF97316)),
            strokeLineWidth = 2.5f
        ) {
            moveTo(24f, 6f)
            arcTo(18f, 18f, 0f, false, true, 24f, 42f)
            arcTo(18f, 18f, 0f, false, true, 24f, 6f)
            close()
        }
        // Middle ring
        path(
            fill = null,
            stroke = SolidColor(Color(0xFFFB923C)),
            strokeLineWidth = 2.5f
        ) {
            moveTo(24f, 12f)
            arcTo(12f, 12f, 0f, false, true, 24f, 36f)
            arcTo(12f, 12f, 0f, false, true, 24f, 12f)
            close()
        }
        // Inner dot
        path(fill = SolidColor(Color(0xFFEA580C))) {
            moveTo(24f, 18f)
            arcTo(6f, 6f, 0f, false, true, 24f, 30f)
            arcTo(6f, 6f, 0f, false, true, 24f, 18f)
            close()
        }
        // Arrow
        path(
            stroke = SolidColor(Color(0xFFEA580C)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(36f, 6f)
            lineTo (42f, 6f)
            lineTo(42f, 12f)
            moveTo(42f, 6f)
            lineTo (30f, 18f)
        }
    }.build()

// ── 15. Ideas / Brainstorm ────────────────────────────────────────────────────
val IdeasIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Ideas",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Bulb
        path(
            fill = SolidColor(Color(0xFFFEF08A)),
            stroke = SolidColor(Color(0xFFCA8A04)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(24f, 6f)
            arcTo(12f, 12f, 0f, false, true, 32f, 27f)
            lineTo(30f, 32f)
            lineTo(18f, 32f)
            lineTo(16f, 27f)
            arcTo(12f, 12f, 0f, false, true, 24f, 6f)
            close()
        }
        // Base
        path(
            stroke = SolidColor(Color(0xFFCA8A04)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(19f, 36f)
            lineTo (29f, 36f)
            moveTo(20f, 40f)
            lineTo (28f, 40f)
        }
        // Shine lines
        path(
            stroke = SolidColor(Color(0xFFFBBF24)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(24f, 2f)
            lineTo (24f, 4f)
            moveTo(36f, 6f)
            lineTo (34f, 8f)
            moveTo(12f, 6f)
            lineTo (14f, 8f)
            moveTo(40f, 16f)
            lineTo (38f, 17f)
            moveTo(8f, 16f)
            lineTo (10f, 17f)
        }
    }.build()

// ── 16. Reading List ──────────────────────────────────────────────────────────
val ReadingListIcon: ImageVector
    get() = ImageVector.Builder(
        name = "ReadingList",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Stack book 1 (bottom)
        path(
            fill = SolidColor(Color(0xFFC4B5FD)),
            stroke = SolidColor(Color(0xFF7C3AED)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(8f, 34f)
            lineTo (40f, 34f)
            lineTo(40f, 40f)
            lineTo(8f, 40f)
            close()
        }
        // Stack book 2 (mid)
        path(
            fill = SolidColor(Color(0xFFA5B4FC)),
            stroke = SolidColor(Color(0xFF6366F1)),
            strokeLineWidth = 2f
        ) {
            moveTo(10f, 26f)
            lineTo (38f, 26f)
            lineTo(38f, 33f)
            lineTo(10f, 33f)
            close()
        }
        // Top open book
        path(
            fill = SolidColor(Color(0xFFEDE9FE)),
            stroke = SolidColor(Color(0xFF7C3AED)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(
                12f,
                10f
            )
            curveTo (12f, 10f, 18f, 12f, 24f, 12f)
            curveTo(30f, 12f, 36f, 10f, 36f, 10f)
            lineTo(
                36f,
                26f
            )
            curveTo (36f, 26f, 30f, 24f, 24f, 24f)
            curveTo(18f, 24f, 12f, 26f, 12f, 26f)
            close()
        }
        // Spine line
        path(stroke = SolidColor(Color(0xFF7C3AED)), strokeLineWidth = 2f) {
            moveTo(24f, 12f)
            lineTo (24f, 24f)
        }
    }.build()

// ── 17. Food & Cooking ────────────────────────────────────────────────────────
val FoodIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Food",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Plate
        path(
            fill = SolidColor(Color(0xFFFEF3C7)),
            stroke = SolidColor(Color(0xFFF59E0B)),
            strokeLineWidth = 2.5f
        ) {
            moveTo(8f, 30f)
            arcTo(16f, 10f, 0f, false, true, 40f, 30f)
            arcTo(16f, 10f, 0f, false, true, 8f, 30f)
            close()
        }
        // Fork
        path(
            stroke = SolidColor(Color(0xFFD97706)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(14f, 14f)
            lineTo (14f, 26f)
            moveTo(12f, 14f)
            lineTo (12f, 19f)
            moveTo(16f, 14f)
            lineTo (16f, 19f)
        }
        // Knife
        path(
            stroke = SolidColor(Color(0xFFD97706)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(34f, 14f)
            lineTo (34f, 26f)
            moveTo(
                34f,
                14f
            )
            curveTo (34f, 14f, 38f, 16f, 38f, 20f)
            curveTo(38f, 22f, 34f, 22f, 34f, 22f)
        }
    }.build()

// ── 18. Entertainment ─────────────────────────────────────────────────────────
val EntertainmentIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Entertainment",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Play circle
        path(
            fill = SolidColor(Color(0xFFFCE7F3)),
            stroke = SolidColor(Color(0xFFEC4899)),
            strokeLineWidth = 2.5f
        ) {
            moveTo(8f, 24f)
            arcTo(16f, 16f, 0f, false, true, 40f, 24f)
            arcTo(16f, 16f, 0f, false, true, 8f, 24f)
            close()
        }
        // Play triangle
        path(fill = SolidColor(Color(0xFFEC4899))) {
            moveTo(20f, 16f)
            lineTo (20f, 32f)
            lineTo(34f, 24f)
            close()
        }
    }.build()

// ── 19. Self Care ─────────────────────────────────────────────────────────────
val SelfCareIcon: ImageVector
    get() = ImageVector.Builder(
        name = "SelfCare",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Star/sparkle
        path(
            fill = SolidColor(Color(0xFFFCE7F3)),
            stroke = SolidColor(Color(0xFFDB2777)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(24f, 6f)
            lineTo (28f, 18f)
            lineTo(40f, 18f)
            lineTo(30f, 26f)
            lineTo(34f, 38f)
            lineTo (24f, 30f)
            lineTo(14f, 38f)
            lineTo(18f, 26f)
            lineTo(8f, 18f)
            lineTo (20f, 18f)
            close()
        }
        // Inner circle
        path(fill = SolidColor(Color(0xFFDB2777))) {
            moveTo(24f, 18f)
            arcTo(6f, 6f, 0f, false, true, 24f, 30f)
            arcTo(6f, 6f, 0f, false, true, 24f, 18f)
            close()
        }
    }.build()

// ── 20. Hobbies ───────────────────────────────────────────────────────────────
val HobbiesIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Hobbies",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Palette circle
        path(
            fill = SolidColor(Color(0xFFFFEDD5)),
            stroke = SolidColor(Color(0xFFF97316)),
            strokeLineWidth = 2.5f
        ) {
            moveTo(8f, 24f)
            arcTo(16f, 16f, 0f, false, true, 40f, 24f)
            arcTo(16f, 16f, 0f, false, true, 8f, 24f)
            close()
        }
        // Color dots
        path(fill = SolidColor(Color(0xFFDC2626))) {
            moveTo(
                16f,
                18f
            )
            arcTo (3f, 3f, 0f, false, true, 16f, 24f)
            arcTo(3f, 3f, 0f, false, true, 16f, 18f)
            close()
        }
        path(fill = SolidColor(Color(0xFF2563EB))) {
            moveTo(
                24f,
                14f
            )
            arcTo (3f, 3f, 0f, false, true, 24f, 20f)
            arcTo(3f, 3f, 0f, false, true, 24f, 14f)
            close()
        }
        path(fill = SolidColor(Color(0xFF16A34A))) {
            moveTo(
                32f,
                18f
            )
            arcTo (3f, 3f, 0f, false, true, 32f, 24f)
            arcTo(3f, 3f, 0f, false, true, 32f, 18f)
            close()
        }
        path(fill = SolidColor(Color(0xFF7C3AED))) {
            moveTo(
                28f,
                26f
            )
            arcTo (3f, 3f, 0f, false, true, 28f, 32f)
            arcTo(3f, 3f, 0f, false, true, 28f, 32f)
            close()
        }
        // Brush thumb hole
        path(
            fill = SolidColor(Color(0xFFF97316)),
            stroke = SolidColor(Color(0xFFEA580C)),
            strokeLineWidth = 1.5f
        ) {
            moveTo(
                30f,
                28f
            )
            arcTo (4f, 4f, 0f, false, true, 30f, 36f)
            arcTo(4f, 4f, 0f, false, true, 30f, 28f)
            close()
        }
    }.build()

// ── 21. Spiritual / Meditation ────────────────────────────────────────────────
val SpiritualIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Spiritual",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Lotus petals
        path(
            fill = SolidColor(Color(0xFFFCE7F3)),
            stroke = SolidColor(Color(0xFFEC4899)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(
                24f,
                30f
            )
            curveTo (24f, 30f, 14f, 26f, 14f, 16f)
            curveTo(14f, 10f, 24f, 10f, 24f, 10f)
            curveTo(24f, 10f, 34f, 10f, 34f, 16f)
            curveTo (34f, 26f, 24f, 30f, 24f, 30f)
            close()
        }
        path(
            fill = SolidColor(Color(0xFFFBBF24)),
            stroke = SolidColor(Color(0xFFF59E0B)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(
                24f,
                30f
            )
            curveTo (24f, 30f, 10f, 28f, 10f, 20f)
            curveTo(10f, 14f, 18f, 14f, 18f, 14f)
        }
        path(
            fill = SolidColor(Color(0xFFFBBF24)),
            stroke = SolidColor(Color(0xFFF59E0B)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(
                24f,
                30f
            )
            curveTo (24f, 30f, 38f, 28f, 38f, 20f)
            curveTo(38f, 14f, 30f, 14f, 30f, 14f)
        }
        // Stem
        path(
            stroke = SolidColor(Color(0xFF16A34A)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(24f, 30f)
            lineTo (24f, 42f)
            moveTo(18f, 38f)
            curveTo (18f, 38f, 20f, 34f, 24f, 34f)
        }
    }.build()

// ── 22. Urgent ────────────────────────────────────────────────────────────────
val UrgentIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Urgent",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Lightning bolt
        path(
            fill = SolidColor(Color(0xFFFEF08A)),
            stroke = SolidColor(Color(0xFFF59E0B)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(28f, 6f)
            lineTo (14f, 26f)
            lineTo(22f, 26f)
            lineTo(18f, 42f)
            lineTo(34f, 20f)
            lineTo (26f, 20f)
            close()
        }
    }.build()

// ── 23. Someday / Maybe ───────────────────────────────────────────────────────
val SomedayIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Someday",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Cloud
        path(
            fill = SolidColor(Color(0xFFE0F2FE)),
            stroke = SolidColor(Color(0xFF38BDF8)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(14f, 36f)
            arcTo(8f, 8f, 0f, false, true, 14f, 20f)
            arcTo(6f, 6f, 0f, false, true, 22f, 15f)
            arcTo(10f, 10f, 0f, false, true, 40f, 22f)
            arcTo(8f, 8f, 0f, false, true, 36f, 36f)
            close()
        }
        // Question mark
        path(
            stroke = SolidColor(Color(0xFF0284C7)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(
                20f,
                24f
            )
            curveTo (20f, 20f, 24f, 18f, 24f, 22f)
            curveTo(24f, 25f, 24f, 26f, 24f, 28f)
            moveTo(24f, 31f)
            lineTo (24f, 33f)
        }
    }.build()

// ── 24. Wishlist ──────────────────────────────────────────────────────────────
val WishlistIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Wishlist",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // List lines
        path(
            fill = SolidColor(Color(0xFFF3E8FF)),
            stroke = SolidColor(Color(0xFF9333EA)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(10f, 8f)
            lineTo (38f, 8f)
            lineTo(38f, 42f)
            lineTo(10f, 42f)
            close()
        }
        // Star item 1
        path(fill = SolidColor(Color(0xFFFBBF24))) {
            moveTo(18f, 16f)
            lineTo (19.4f, 20f)
            lineTo(23f, 20f)
            lineTo(20f, 22.5f)
            lineTo(21.4f, 26f)
            lineTo (18f, 23.5f)
            lineTo(14.6f, 26f)
            lineTo(16f, 22.5f)
            lineTo(13f, 20f)
            lineTo (16.6f, 20f)
            close()
        }
        // Line items
        path(
            stroke = SolidColor(Color(0xFF9333EA)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(26f, 21f)
            lineTo (34f, 21f)
            moveTo(16f, 30f)
            lineTo (34f, 30f)
            moveTo(16f, 36f)
            lineTo (28f, 36f)
        }
    }.build()

// ── 25. Birthdays & Events ────────────────────────────────────────────────────
val BirthdaysIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Birthdays",
        defaultWidth = 48.dp, defaultHeight = 48.dp,
        viewportWidth = 48f, viewportHeight = 48f
    ).apply {
        // Cake body
        path(
            fill = SolidColor(Color(0xFFFEE2E2)),
            stroke = SolidColor(Color(0xFFE11D48)),
            strokeLineWidth = 2.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(8f, 28f)
            lineTo (40f, 28f)
            lineTo(40f, 42f)
            lineTo(8f, 42f)
            close()
        }
        // Frosting wave
        path(
            fill = SolidColor(Color(0xFFFCE7F3)),
            stroke = SolidColor(Color(0xFFEC4899)),
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(8f, 28f)
            curveTo (12f, 24f, 16f, 28f, 20f, 24f)
            curveTo(24f, 20f, 28f, 24f, 32f, 20f)
            curveTo (36f, 16f, 40f, 20f, 40f, 28f)
        }
        // Candles
        path(fill = SolidColor(Color(0xFF6366F1))) {
            moveTo(16f, 20f)
            lineTo (18f, 20f)
            lineTo(18f, 28f)
            lineTo(16f, 28f)
            close()
            moveTo(22f, 16f)
            lineTo (24f, 16f)
            lineTo(24f, 28f)
            lineTo(22f, 28f)
            close()
            moveTo(28f, 20f)
            lineTo (30f, 20f)
            lineTo(30f, 28f)
            lineTo(28f, 28f)
            close()
        }
        // Flames
        path(fill = SolidColor(Color(0xFFFBBF24))) {
            moveTo(
                17f,
                16f
            )
            curveTo (17f, 16f, 14f, 18f, 17f, 20f)
            curveTo(20f, 18f, 17f, 16f, 17f, 16f)
            moveTo(
                23f,
                12f
            )
            curveTo (23f, 12f, 20f, 14f, 23f, 16f)
            curveTo(26f, 14f, 23f, 12f, 23f, 12f)
            moveTo(
                29f,
                16f
            )
            curveTo (29f, 16f, 26f, 18f, 29f, 20f)
            curveTo(32f, 18f, 29f, 16f, 29f, 16f)
        }
    }.build()
