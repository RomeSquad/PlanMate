package org.example.logic.usecase.task

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import java.util.UUID

class GetTaskByIdUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun getTaskById(taskId: UUID): Task {
        return taskRepository.getTaskById(taskId)
    }
}
