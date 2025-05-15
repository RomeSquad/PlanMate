package org.example.logic.request.project

import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import java.util.*

data class CreateProjectRequest(
    val name: String,
    val userId: UUID,
    val userName: String,
    val description: String
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
