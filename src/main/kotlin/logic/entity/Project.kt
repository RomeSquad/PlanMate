package org.example.logic.entity

import java.util.*

data class Project(
    val projectId: UUID,
    val name: String,
    val description: String,
    val state: ProjectState
)

