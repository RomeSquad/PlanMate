package org.example.logic.usecase.state

import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import java.util.*

class DefaultProjectStateUseCase(
    private val stateRepository: ProjectStateRepository
) {
    private val defaultStateNames = listOf("todo", "in progress", "done")
    suspend fun initializeProjectState(projectId: UUID) {
        defaultStateNames.forEach { name ->
            val state = ProjectState(
                stateName = name,
                projectId = projectId
            )
            stateRepository.addProjectState(state)
        }
    }
}
