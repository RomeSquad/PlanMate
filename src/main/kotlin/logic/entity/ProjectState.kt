package org.example.logic.entity

import java.util.UUID

data class ProjectState(
    val projectId: UUID,
    val stateName: String,
)