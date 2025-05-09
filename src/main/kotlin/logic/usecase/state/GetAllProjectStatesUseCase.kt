package org.example.logic.usecase.state

import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository

class GetAllProjectStatesUseCase(
    private val stateRepository: ProjectStateRepository
) {
    suspend fun execute(projectId: Int): List<ProjectState> {
        return stateRepository.getAllProjectStates(projectId )
    }
}