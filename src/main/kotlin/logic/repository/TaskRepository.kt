package org.example.logic.repository

import org.example.logic.entity.Task
import java.util.UUID

interface TaskRepository {
    suspend fun createTask(task: Task)
    suspend fun editTask(taskId: UUID, title: String, description: String, updatedAt: Long)
    suspend fun deleteTask(projectId: UUID, taskId: UUID)

    suspend fun getTaskById (taskId: UUID): Task
    suspend fun getTasksByProject(projectId: UUID): List<Task>

    suspend fun getAllTasks(): List<Task>
}