package com.jrprofessor.mindolist.model

import androidx.compose.ui.graphics.Color

enum class Priority(
    val label: String,
    val color: Color,
    val dotColor: Color,
    val backgroundColor: Color,
    val stripColor: Color,
) {
    HIGH(
        label = "HIGH",
        color = Color(0xFFE53E3E),
        dotColor = Color(0xFFE53E3E),
        backgroundColor = Color(0xFFFFF5F5),
        stripColor = Color(0xFFEF4444),
    ),
    MEDIUM(
        label = "MEDIUM",
        color = Color(0xFFDD6B20),
        dotColor = Color(0xFFDD6B20),   // orange
        backgroundColor = Color(0xFFFFFAF0),
        stripColor = Color(0xFFF59E0B),
    ),
    LOW(
        label = "LOW",
        color = Color(0xFF718096),
        dotColor = Color(0xFF718096),   // gray
        backgroundColor = Color(0xFFF7FAFC),
        stripColor = Color(0xFF10B981),
    ),
}