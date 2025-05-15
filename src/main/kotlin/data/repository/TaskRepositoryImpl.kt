package org.example.data.repository

import org.example.data.datasource.task.TaskDataSource
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository
import org.example.logic.request.auth.TaskDeletionRequest
import org.example.logic.request.auth.TaskEditRequest
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {

    override suspend fun createTask(task: Task) = taskDataSource.createTask(task)

    override suspend fun editTask(request: TaskEditRequest) {
        taskDataSource.editTask(request)
    }

    override suspend fun deleteTask(request: TaskDeletionRequest) {
        taskDataSource.deleteTask(request)
    }

    override suspend fun getTaskById(taskId: UUID): Task {
        return taskDataSource.getTaskByIdFromFile(taskId)
    }

    override suspend fun getTasksByProject(projectId: UUID): List<Task> {
        return taskDataSource.getAllTasks().filter { it.projectId == projectId }
    }

    override suspend fun getAllTasks(): List<Task> = taskDataSource.getAllTasks()
}