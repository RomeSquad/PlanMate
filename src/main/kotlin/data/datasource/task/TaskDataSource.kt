package org.example.data.datasource.task

import org.example.logic.entity.Task

interface TaskDataSource {
    fun getTaskByIdFromFile(taskId: String): Task
    fun getTask(): Task
}