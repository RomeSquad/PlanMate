package org.example.data.repository

import org.example.data.datasource.task.TaskDataSource
import org.example.logic.entity.Task
import org.example.logic.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {

    override fun createTask(task: Task) = taskDataSource.createTask(task)

    override fun editTask(
        taskId: String,
        title: String,
        description: String,
        updatedAt: Long
    ) {
        taskDataSource.editTask(taskId, title, description, updatedAt)
    }

    override fun deleteTask(projectId: Int, taskId: String) {
        taskDataSource.deleteTask(projectId, taskId)
    }

    override fun getTaskById(taskId: String): Task {
        return taskDataSource.getTaskByIdFromFile(taskId)
    }

    override fun getTasksByProject(projectId: Int): List<Task> {
        return taskDataSource.getAllTasks().filter { it.projectId == projectId }
    }

    override fun getAllTasks(): List<Task> = taskDataSource.getAllTasks()
}