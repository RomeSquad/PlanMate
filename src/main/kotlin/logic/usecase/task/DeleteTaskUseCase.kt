package org.example.logic.usecase.task

import org.example.logic.repository.TaskRepository

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun deleteTask(projectId: Int, taskId: String) {
        require(projectId > 0) { "projectId must not be blank" }
        require(taskId.isNotBlank()) { "taskId must not be blank" }

        taskRepository.deleteTask(projectId, taskId)
    }
}