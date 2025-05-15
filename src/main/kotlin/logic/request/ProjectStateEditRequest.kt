package org.example.logic.request

import java.util.*

data class ProjectStateEditRequest(
    val projectId: UUID,
    val newStateName: String
)
