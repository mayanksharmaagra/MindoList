package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface TaskRepository {
    fun getTasks(): Flow<com.jrprofessor.mindolist.domain.model.Result<List<TaskModel>>>
    fun getTasksByDate(date: LocalDate): Flow<com.jrprofessor.mindolist.domain.model.Result<List<TaskModel>>>
    suspend fun addTask(task: TaskModel): com.jrprofessor.mindolist.domain.model.Result<Unit>
    suspend fun updateTask(task: TaskModel): com.jrprofessor.mindolist.domain.model.Result<Unit>
    suspend fun markComplete(taskId: String, isCompleted: Boolean): com.jrprofessor.mindolist.domain.model.Result<Unit>
    suspend fun deleteTask(taskId: String): com.jrprofessor.mindolist.domain.model.Result<Unit>
    suspend fun togglePin(taskId: String, isPinned: Boolean): com.jrprofessor.mindolist.domain.model.Result<Unit>
    fun getTasksInRange(startMillis: Long, endMillis: Long): Flow<com.jrprofessor.mindolist.domain.model.Result<List<TaskModel>>>
}