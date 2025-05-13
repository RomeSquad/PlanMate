package org.example.logic.usecase.task

import org.example.logic.repository.TaskRepository
import java.util.*

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun deleteTask(projectId: UUID, taskId: UUID) {
        taskRepository.deleteTask(projectId, taskId)
    }
}