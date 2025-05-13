package org.example.logic.entity

import java.util.*

data class Task(
    val taskId: UUID,
    var title: String,
    var description: String,
    var state: ProjectState,
    val projectId: UUID,
    val createdBy: UUID,
    val createdAt: Long,
    var updatedAt: Long
)