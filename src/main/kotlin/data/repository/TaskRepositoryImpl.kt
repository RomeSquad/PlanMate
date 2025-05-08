package org.example.data.repository

import org.example.data.datasource.task.TaskDataSource
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {

    override suspend fun createTask(task: Task) = taskDataSource.createTask(task)

    override suspend fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        taskDataSource.editTask(taskId, title, description, updatedAt)
    }

    override suspend fun deleteTask(projectId: Int, taskId: String) {
        taskDataSource.deleteTask(projectId, taskId)
    }

    override suspend fun getTaskById(taskId: String): Task {
        return taskDataSource.getTaskByIdFromFile(taskId)
    }

    override suspend fun getTasksByProject(projectId: Int): List<Task> {
        return taskDataSource.getAllTasks().filter { it.projectId == projectId }
    }

    override suspend fun getAllTasks(): List<Task> = taskDataSource.getAllTasks()
}