package org.example.logic.usecase.state

import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import java.util.*

class AddProjectStatesUseCase(
    private val stateRepository: ProjectStateRepository
) {
    suspend fun execute(stateName: String, projectId: UUID) {
        require(stateName.isNotBlank()) { "State Name must not be blank" }
        val projectState = ProjectState(
            stateName = stateName,
            projectId = projectId
        )
        stateRepository.addProjectState(projectState)
    }
}