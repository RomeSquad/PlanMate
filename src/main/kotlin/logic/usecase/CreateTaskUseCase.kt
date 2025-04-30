package org.example.logic.usecase

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class CreateTaskUseCase (
    private val taskRepository: TaskRepository
) {
    fun createTask(task: Task) {}
}