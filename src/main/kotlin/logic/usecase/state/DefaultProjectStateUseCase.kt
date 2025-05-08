package org.example.logic.usecase.state

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class DefaultProjectStateUseCase(
    private val stateRepository: StateRepository
) {
    private val defaultStateNames = listOf("todo", "in progress", "done")
    fun initializeProjectState(projectId: Int) {

        defaultStateNames.forEach { name ->
            val state = State(
                stateName = name,
                projectId = projectId
            )
            stateRepository.addState(state)
        }
    }
}
