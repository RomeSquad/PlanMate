package org.example.logic.repository

interface TaskRepository {
    fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    )
}