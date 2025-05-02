package org.example.logic.repository

import org.example.logic.entity.Task

interface TaskRepository {
    fun createTask(task: Task): Result<Unit>

    fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    )

    fun deleteTask(projectId: Int, taskId: String)

    fun getTaskById (taskId: String): Result<Task>
}