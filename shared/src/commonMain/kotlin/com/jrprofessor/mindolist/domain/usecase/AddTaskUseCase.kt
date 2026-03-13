package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.model.TaskModel

class AddTaskUseCase(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(task: TaskModel): com.jrprofessor.mindolist.domain.model.Result<Unit> {
        if (task.title.isBlank())
            return com.jrprofessor.mindolist.domain.model.Result.Error(Exception("Validation"), "Title cannot be empty")
        if (task.dueDate == null)
            return com.jrprofessor.mindolist.domain.model.Result.Error(Exception("Validation"), "Please select a due date")
        return repository.addTask(task)
    }
}