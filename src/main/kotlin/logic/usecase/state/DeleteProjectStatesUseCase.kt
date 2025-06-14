package org.example.logic.usecase.state

import org.example.logic.repository.ProjectStateRepository
import java.util.*

class DeleteProjectStatesUseCase(
    private val stateRepository: ProjectStateRepository
) {
    suspend fun execute(stateId: UUID): Boolean {
        return stateRepository.deleteProjectState(stateId)
    }
}

