package org.example.logic.usecase.state

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class GetAllStatesUseCase(private val stateRepository: StateRepository) {
    fun executeGetAllStates(): List<State> {
        return stateRepository.getAllStates()
    }
}