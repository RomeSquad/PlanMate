package org.example.data.datasource.task

import org.example.logic.entity.Task

interface TaskDataSource {
    fun getTaskByIdFromFile(taskId: String): Result<Task>
    fun getAllTasks(): Result<List<Task>>
    fun setAllTasks(tasks: List<Task>): Result<Unit>

    fun createTask(task: Task): Result<Unit>
    fun deleteTask(projectId: Int, taskId: String)
}