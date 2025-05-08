package org.example.logic.repository

import org.example.logic.entity.Task

interface TaskRepository {
    suspend fun createTask(task: Task)
    suspend fun editTask(taskId: String, title: String, description: String, updatedAt: Long)
    suspend fun deleteTask(projectId: Int, taskId: String)

    suspend fun getTaskById (taskId: String): Task
    suspend fun getTasksByProject(projectId: Int): List<Task>

    suspend fun getAllTasks(): List<Task>
}