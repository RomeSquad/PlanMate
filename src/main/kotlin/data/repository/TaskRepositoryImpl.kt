package org.example.data.repository

import org.example.data.datasource.task.TaskDataSource
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {
    override suspend fun createTask(task: Task) {
        taskDataSource.createTask(task)
    }

    override suspend fun editTask(
        taskId: UUID,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        taskDataSource.editTask(taskId, title, description, updatedAt)
    }

    override suspend fun deleteTask(projectId: UUID, taskId: UUID) {
        taskDataSource.deleteTask(projectId, taskId)
    }

    override suspend fun getTaskById(taskId: UUID): Task {
        return taskDataSource.getTaskByIdFromFile(taskId)
    }

    override suspend fun getTasksByProject(projectId: UUID): List<Task> {
        return taskDataSource.getAllTasks().filter { it.projectId == projectId }
    }

    override suspend fun getAllTasks(): List<Task> {
        return taskDataSource.getAllTasks()
    }
}