package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<com.jrprofessor.mindolist.domain.model.Result<List<TaskModel>>>
    suspend fun addTask(task: TaskModel): com.jrprofessor.mindolist.domain.model.Result<Unit>
    suspend fun updateTask(task: TaskModel): com.jrprofessor.mindolist.domain.model.Result<Unit>
    suspend fun markComplete(taskId: String, isCompleted: Boolean): com.jrprofessor.mindolist.domain.model.Result<Unit>
    suspend fun deleteTask(taskId: String): com.jrprofessor.mindolist.domain.model.Result<Unit>
}