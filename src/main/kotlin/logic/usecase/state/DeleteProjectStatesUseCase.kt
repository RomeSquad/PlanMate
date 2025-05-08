package org.example.logic.usecase.state

import org.example.logic.repository.ProjectStateRepository

class DeleteProjectStatesUseCase(
    private val stateRepository: ProjectStateRepository
) {
    fun execute(stateId: Int) {
        return stateRepository.deleteProjectState(stateId)
    }
}

