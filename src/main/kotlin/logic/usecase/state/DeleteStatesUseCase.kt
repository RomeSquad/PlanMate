package org.example.logic.usecase.state

import org.example.logic.repository.StateRepository

class DeleteStatesUseCase(
    private val stateRepository: StateRepository
) {
    fun execute(stateId: Int) {
        return stateRepository.deleteState(stateId)
    }
}

