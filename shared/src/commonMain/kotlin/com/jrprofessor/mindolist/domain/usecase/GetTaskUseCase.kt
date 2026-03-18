package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class GetTasksUseCase(
    private val repository: TaskRepository,
) {
    operator fun invoke(date: LocalDate? = null): Flow<Result<List<TaskModel>>> =
        if (date == null) repository.getTasks() else repository.getTasksByDate(date)
}