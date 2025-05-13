package org.example.logic.entity

import java.util.*

data class TaskState(
    val projectId: UUID,
    val stateName: String,
)