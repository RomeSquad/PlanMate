package org.example.data.datasource.task

import org.example.logic.entity.Task
import org.example.logic.request.TaskDeletionRequest
import org.example.logic.request.TaskEditRequest
import java.util.*

interface TaskDataSource {
    suspend fun createTask(task: Task)
    suspend fun editTask(request : TaskEditRequest)
    suspend fun deleteTask(request: TaskDeletionRequest)

    suspend fun getTaskByIdFromFile(taskId: UUID): Task
    suspend fun getTasksByProjectId(projectId: UUID): List<Task>

    suspend fun getAllTasks(): List<Task>
    suspend fun saveAllTasks(tasks: List<Task>)
}