package org.example.logic.usecase.task

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository
) {
    fun getAllTasks(): List<Task> = taskRepository.getAllTasks()
}
