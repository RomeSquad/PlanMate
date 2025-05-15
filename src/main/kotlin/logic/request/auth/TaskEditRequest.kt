package org.example.logic.request.auth

import java.util.*

data class TaskEditRequest (
    val taskId: UUID,
    val title: String,
    val description: String,
    val updatedAt: Long
)