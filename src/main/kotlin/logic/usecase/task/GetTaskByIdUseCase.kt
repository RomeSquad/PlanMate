package org.example.logic.usecase.task

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class GetTaskByIdUseCase(
    private val taskRepository: TaskRepository
) {
    fun getTaskById(taskId: String): Task {
        require(taskId.isNotBlank()) { "taskId must not be blank" }
        return taskRepository.getTaskById(taskId)
    }
}
