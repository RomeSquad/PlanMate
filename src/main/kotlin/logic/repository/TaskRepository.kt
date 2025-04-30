package org.example.logic.repository

import org.example.logic.entity.Task

interface TaskRepository {
    fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    )
    fun createTask(task: Task)
}