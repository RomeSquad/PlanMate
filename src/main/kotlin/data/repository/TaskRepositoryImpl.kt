package org.example.data.repository

import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class TaskRepositoryImpl : TaskRepository {
    override fun createTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: String): Task {
        TODO("Not yet implemented")
    }
}
