package org.example.logic.usecase.task

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class CreateTaskUseCase(
    private val taskRepository: TaskRepository
) {
    fun createTask(task: Task) {
        if (task.title.isEmpty()) {
            throw IllegalArgumentException("Title must not be empty")
        }

        if (task.description.isEmpty()) {
            throw IllegalArgumentException("Description must not be empty")
        }

        if (task.projectId == 0) {
            throw IllegalArgumentException("Project ID cannot be zero")
        }

        taskRepository.createTask(task)
    }
}