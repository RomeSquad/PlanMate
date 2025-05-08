package org.example.data.datasource.task

import org.example.logic.entity.Task

interface TaskDataSource {
    suspend fun createTask(task: Task)
    suspend fun editTask(taskId: String, title: String, description: String, updatedAt: Long)
    suspend fun deleteTask(projectId: Int, taskId: String)

    suspend fun getTaskByIdFromFile(taskId: String): Task
    suspend fun getTasksByProjectId(projectId: Int): List<Task>

    suspend fun getAllTasks(): List<Task>
    suspend fun saveAllTasks(tasks: List<Task>)
}