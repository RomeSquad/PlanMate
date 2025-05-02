package org.example.logic.usecase.state

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class DeleteStatesUseCase(private val stateRepository: StateRepository) {
    fun executeDeleteState(stateId: String) {
        stateRepository.deleteState(stateId)
    }
}

