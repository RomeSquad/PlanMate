package org.example.logic.usecase

import org.example.logic.repository.TaskRepository

class EditTaskUseCase (
    private val taskRepository: TaskRepository
) {
    fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    ) {

    }
}