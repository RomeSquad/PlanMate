package org.example.logic.request

import java.util.*

data class TaskDeletionRequest(
    val projectId: UUID,
    val taskId: UUID
)
