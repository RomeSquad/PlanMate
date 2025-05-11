package org.example.logic.usecase.state

import org.example.logic.repository.ProjectStateRepository
import java.util.UUID

class DeleteProjectStatesUseCase(
    private val stateRepository: ProjectStateRepository
) {
    suspend fun execute(stateId: UUID) {
        return stateRepository.deleteProjectState(stateId)
    }
}

