package org.example.logic.usecase.state

import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository

class GetStateByTaskIdUseCase(
    private val stateRepository: ProjectStateRepository
) {
    fun execute(taskId:Int):ProjectState{
        require(taskId > 0) { "task id must be greater than 0" }
        return stateRepository.getProjectStateByTaskId(taskId)
    }
}