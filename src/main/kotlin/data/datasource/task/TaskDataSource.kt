package org.example.data.datasource.task

import org.example.logic.entity.Task

interface TaskDataSource {
    fun getTaskById(taskId: String): Task
}