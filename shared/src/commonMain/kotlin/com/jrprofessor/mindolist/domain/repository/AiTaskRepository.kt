package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.model.ParsedTask

interface AiTaskRepository {
    suspend fun parseReminderText(userInput: String): ParsedTask
}
