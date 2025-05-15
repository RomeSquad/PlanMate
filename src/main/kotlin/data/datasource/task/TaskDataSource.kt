package org.example.data.datasource.task

import org.example.logic.entity.Task
import java.util.*

interface TaskDataSource {
    suspend fun createTask(task: Task)
    suspend fun editTask(taskId: UUID, title: String, description: String, updatedAt: Long)
    suspend fun deleteTask(projectId: UUID, taskId: UUID)
    suspend fun getTaskByIdFromFile(taskId: UUID): Task
    suspend fun getTasksByProjectId(projectId: UUID): List<Task>
    suspend fun getAllTasks(): List<Task>
    suspend fun saveAllTasks(tasks: List<Task>)
}