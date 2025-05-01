package org.example.logic.usecase.task

import org.example.logic.repository.TaskRepository

class DeleteTaskUseCase(private val taskRepository: TaskRepository) {
    fun deleteTask(projectId: String, taskId: String){
        require(projectId.isNotBlank()) { "projectId must not be blank" }
        require(taskId.isNotBlank()) { "taskId must not be blank" }
        taskRepository.deleteTask(projectId, taskId)
    }
}