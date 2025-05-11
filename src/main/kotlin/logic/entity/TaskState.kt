package org.example.logic.entity

import java.util.UUID

data class TaskState(
    val projectId: UUID,
    val stateName: String,
)