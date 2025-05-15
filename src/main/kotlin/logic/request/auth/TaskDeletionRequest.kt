package org.example.logic.request.auth

import java.util.*

data class TaskDeletionRequest(
    val projectId: UUID,
    val taskId: UUID
)
