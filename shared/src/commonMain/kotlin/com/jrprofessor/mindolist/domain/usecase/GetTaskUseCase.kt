package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.model.TaskModel
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(
    private val repository: TaskRepository,
) {
    operator fun invoke(): Flow<Result<List<TaskModel>>> = repository.getTasks()
}