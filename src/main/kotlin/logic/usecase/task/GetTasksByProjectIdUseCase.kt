package org.example.logic.usecase.task

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import java.util.UUID

class GetTasksByProjectIdUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun getTasksByProjectId(projectId: UUID): List<Task> {
        return taskRepository.getTasksByProject(projectId)
    }
}
