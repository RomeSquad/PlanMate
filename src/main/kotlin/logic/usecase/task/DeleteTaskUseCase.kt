package org.example.logic.usecase.task

import org.example.logic.repository.TaskRepository
import org.example.logic.request.TaskDeletionRequest
import java.util.*

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun deleteTask(projectId: UUID, taskId: UUID) {
        val request = TaskDeletionRequest(projectId, taskId)
        taskRepository.deleteTask(request)
    }
}