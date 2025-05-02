package org.example.logic.usecase.task

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class CreateTaskUseCase (
    private val taskRepository: TaskRepository
) {
    fun createTask(task: Task): Result<Unit> {
        return try {
            if (task.title.isEmpty()) {
                failedException("Title must not be empty")
            }

            if (task.description.isEmpty()) {
                failedException("Description must not be empty")
            }

            if (task.projectId == 0) {
                failedException("Project ID cannot be zero")
            }

            taskRepository.createTask(task)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private fun failedException(message: String): Result<Unit> {
        return Result.failure(IllegalArgumentException(message))
    }
}