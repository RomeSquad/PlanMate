package org.example.logic.repository

import org.example.logic.entity.Task

interface TaskRepository {
    fun createTask(task: Task)

    fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    )
}