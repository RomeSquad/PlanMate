package org.example.logic.usecase.state

import org.example.logic.repository.StateRepository

class EditStateUseCase(
    private val stateRepository: StateRepository
) {
    fun execute(projectId: Int, newStateName: String) {
        require(newStateName.isNotBlank()) { "should new state name must not be blank" }

        stateRepository.editState(projectId, newStateName)
    }
}

