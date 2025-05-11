package org.example.logic.usecase.state

import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import java.util.UUID

class GetStateByTaskIdUseCase(
    private val stateRepository: ProjectStateRepository
) {
    suspend fun execute(taskId: UUID): ProjectState {
        return stateRepository.getProjectStateByTaskId(taskId)
    }
}
