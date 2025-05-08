package org.example.logic.usecase.task

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class GetTasksByProjectIdUseCase(
    private val taskRepository: TaskRepository
) {
    fun getTasksByProjectId(projectId: Int): List<Task> {
        require(projectId > 0) { "Project ID must be greater than zero" }
        return taskRepository.getTasksByProject(projectId)
    }
}
