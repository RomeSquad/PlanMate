package logic.usecase.task

import org.example.logic.entity.State
import org.example.logic.entity.Task

fun createTask (
    id: String? = "",
    title: String,
    description: String,
    projectId: Int = 1
): Task {
    return Task (
        id = id.toString(),
        title = title,
        description = description,
        state = State(projectId = projectId, stateName = ""),
        projectId = projectId,
        createdBy = "",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}