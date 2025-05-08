package org.example.logic.usecase.state

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class AddStatesUseCase(
    private val stateRepository: StateRepository
) {
    fun execute(stateName: String, projectId: Int) {
        require(stateName.isNotBlank()) { "State Name must not be blank" }

        val state = State(
            stateName = stateName,
            projectId = projectId
        )

        stateRepository.addState(state)
    }
}