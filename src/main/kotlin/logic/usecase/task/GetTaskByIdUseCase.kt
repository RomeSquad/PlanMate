package org.example.logic.usecase.task

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class GetTaskByIdUseCase (
    private val taskRepository: TaskRepository
) {
    fun getTaskById(taskId: String): Result<Task> {
        TODO()
    }
}