package com.jrprofessor.mindolist.model

import androidx.compose.ui.graphics.Color

// Data class to hold card configuration
data class TaskCardData(
    val title: String,
    var taskCount: Int,
    val taskMsg: String,
    val cardBackgroundColor: Color,
    val titleColor: Color,
    val taskCountColor: Color,
    val taskMsgColor: Color,
)