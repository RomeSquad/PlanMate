package org.example.data.datasource.task

import org.example.logic.entity.Task

interface TaskDataSource {
    fun createTask(task: Task)
    fun editTask(taskId: String, title: String, description: String, updatedAt: Long)
    fun deleteTask(projectId: Int, taskId: String)

    fun getTaskByIdFromFile(taskId: String): Task
    fun getTasksByProjectId(projectId: Int): List<Task>

    fun getAllTasks(): List<Task>
    fun saveAllTasks(tasks: List<Task>)
}