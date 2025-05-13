package org.example.logic.usecase.state

import org.example.logic.repository.ProjectStateRepository
import java.util.*

class EditProjectStateUseCase(
    private val stateRepository: ProjectStateRepository
) {
    suspend fun execute(projectId: UUID, newStateName: String) {
        require(newStateName.isNotBlank()) { "should new state name must not be blank" }

        stateRepository.editProjectState(projectId, newStateName)
    }
}

