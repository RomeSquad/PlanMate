package org.example.logic.usecase.state

import org.example.logic.repository.StateRepository

class DeleteStatesUseCase(private val stateRepository: StateRepository) {
    fun executeDeleteState(stateId: String): Boolean {
        require(stateId.isNotBlank()) { "State Id must not be blank" }
        return stateRepository.deleteState(stateId)
    }
}

