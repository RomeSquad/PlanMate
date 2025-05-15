package org.example.logic.usecase.task

import org.example.logic.repository.TaskRepository
import org.example.logic.request.auth.TaskEditRequest
import java.util.*

class EditTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend fun editTask(
        taskId: UUID,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        if (title.isBlank()) {
            throw IllegalArgumentException("title must not be blank")
        }

        if (description.isBlank()) {
            throw IllegalArgumentException("description must not be blank")
        }
        val request = TaskEditRequest(taskId, title, description, updatedAt)
        taskRepository.editTask(request)
    }
}
