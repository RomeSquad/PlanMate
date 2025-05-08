package org.example.logic.usecase.state

import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository

class GetAllProjectStatesUseCase(
    private val stateRepository: ProjectStateRepository
) {
    fun execute(): List<ProjectState> {
        return stateRepository.getAllProjectStates()
    }
}