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
        val result = taskDataSource.getAllTasks()
        if (result.isFailure) throw result.exceptionOrNull()!!

        val tasks = result.getOrThrow().toMutableList()
        val index = tasks.indexOfFirst { it.id == taskId }

        if (index == -1) {
            throw NoSuchElementException("Task with id $taskId not found")
        }

        val task = tasks[index]
        tasks[index] = task.copy(
            title = title,
            description = description,
            updatedAt = updatedAt
        )

        val writeResult = taskDataSource.setAllTasks(tasks)
        if (writeResult.isFailure) throw writeResult.exceptionOrNull()!!
    }

    override fun getAllTasks(): Result<List<Task>> {
        return taskDataSource.getAllTasks()
    }

    override fun getTaskById(taskId: String): Result<Task> {
        return taskDataSource.getTaskByIdFromFile(taskId)
    }

    override fun getTasksByProject(projectId: Int): List<Task> {
        val result = taskDataSource.getAllTasks()
        if (result.isFailure) throw result.exceptionOrNull()!!
        return result.getOrThrow().filter { it.projectId == projectId }
    }

    override fun deleteTask(projectId: Int, taskId: String) {
        taskDataSource.deleteTask(projectId, taskId)
    }
}