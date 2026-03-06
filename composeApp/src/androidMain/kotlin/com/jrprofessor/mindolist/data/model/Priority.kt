package com.jrprofessor.mindolist.data.model

import androidx.compose.ui.graphics.Color

enum class Priority(
    val label: String,
    val color: Color,
    val backgroundColor: Color,
    val stripColor: Color,
) {
    HIGH(
        label = "HIGH",
        color = Color(0xFFDC2626),
        backgroundColor = Color(0xFFFEE2E2),
        stripColor = Color(0xFFEF4444),
    ),
    MEDIUM(
        label = "MEDIUM",
        color = Color(0xFFD97706),
        backgroundColor = Color(0xFFFEF3C7),
        stripColor = Color(0xFFF59E0B),
    ),
    LOW(
        label = "LOW",
        color = Color(0xFF059669),
        backgroundColor = Color(0xFFD1FAE5),
        stripColor = Color(0xFF10B981),
    ),
}