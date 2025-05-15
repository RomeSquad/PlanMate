package org.example.logic.request.auth

import java.util.*

data class ProjectStateEditRequest(
    val projectId: UUID,
    val newStateName: String
)
