package org.example.logic.usecase.state

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class GetAllProjectStatesUseCase(
    private val stateRepository: StateRepository
) {
    fun execute(): List<State> {
        return stateRepository.getAllStatesProject()
    }
}