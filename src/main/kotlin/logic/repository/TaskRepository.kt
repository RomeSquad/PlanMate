package org.example.logic.repository

import org.example.logic.entity.Task

interface TaskRepository {
    fun createTask(task: Task)
    fun editTask(taskId: String, title: String, description: String, updatedAt: Long)
    fun deleteTask(projectId: Int, taskId: String)

    fun getTaskById (taskId: String): Task
    fun getTasksByProject(projectId: Int): List<Task>

    fun getAllTasks(): List<Task>
}