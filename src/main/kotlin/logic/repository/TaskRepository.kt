package org.example.logic.repository

import org.example.logic.entity.Task

interface TaskRepository {
    fun createTask(task: Task)
}