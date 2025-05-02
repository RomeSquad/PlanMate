package org.example.data.repository

import org.example.data.datasource.task.TaskDataSource
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {
    override fun createTask(task: Task): Result<Unit> {
        return taskDataSource.createTask(task)
    }

    override fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        TODO("Not yet implemented")
    }

    override fun getTaskById(taskId: String): Result<Task> {
        TODO("Not yet implemented")
    }

    override fun deleteTask(projectId: Int, taskId: String) {
        taskDataSource.deleteTask(projectId, taskId)
    }
}
