package org.example.logic.entity

import java.util.*

data class Project(
    val projectId: UUID,
    val name: String,
    val description: String,
    val state: ProjectState
)

data class CreateProjectRequest(
    val name: String,
    val userId: UUID,
    val userName: String,
    val description: String
)

data class CreateProjectResponse(
    val id: UUID,
)

fun CreateProjectRequest.toProject(): Project {
    val id = UUID.randomUUID()
    return Project(
        projectId = id,
        name = name,
        description = description,
        state = ProjectState(
            projectId = id,
            stateName = "InProgress"
        )
    )
}
