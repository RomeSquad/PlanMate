package org.example.logic.usecase.task

import org.example.logic.repository.TaskRepository

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend fun getAllTasks() = taskRepository.getAllTasks()
}