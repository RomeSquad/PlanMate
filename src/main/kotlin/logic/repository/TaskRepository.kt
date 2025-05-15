package org.example.logic.repository

import org.example.logic.entity.Task
import org.example.logic.request.auth.TaskDeletionRequest
import org.example.logic.request.auth.TaskEditRequest
import java.util.*

interface TaskRepository {
    suspend fun createTask(task: Task)
    suspend fun editTask(request: TaskEditRequest)
    suspend fun deleteTask(request: TaskDeletionRequest)

    suspend fun getTaskById(taskId: UUID): Task
    suspend fun getTasksByProject(projectId: UUID): List<Task>

    suspend fun getAllTasks(): List<Task>
}