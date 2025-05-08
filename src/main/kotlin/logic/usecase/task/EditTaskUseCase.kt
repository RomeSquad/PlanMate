package org.example.logic.usecase.task

import org.example.logic.repository.TaskRepository

class EditTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        if (taskId.isBlank()) {
            throw IllegalArgumentException("taskId must not be blank")
        }

        if (title.isBlank()) {
            throw IllegalArgumentException("title must not be blank")
        }

        if (description.isBlank()) {
            throw IllegalArgumentException("description must not be blank")
        }

        taskRepository.editTask(taskId, title, description, updatedAt)
    }
}
