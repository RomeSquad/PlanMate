package logic.usecase.task

import org.example.logic.entity.ProjectState
import org.example.logic.entity.Task
import java.util.*

fun createTask(
    id: UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
    title: String,
    description: String,
    projectId: UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
    userId: UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174002")
): Task {
    return Task(
        taskId = id,
        title = title,
        description = description,
        state = ProjectState(projectId = projectId, stateName = ""),
        projectId = projectId,
        createdBy = userId,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}