package com.jrprofessor.mindolist.model

import kotlinx.serialization.Serializable

@Serializable
data class ParsedTask(
    val title: String,
    val description: String,
    val date: String?,     // "DD-MM-YYYY" or null
    val time: String?  ,    // "hh:mm AM/PM" or null
    val importance: String,   // "High" | "Medium" | "Low"
    val category: String // "Work" | "Personal" | "Shopping"
)