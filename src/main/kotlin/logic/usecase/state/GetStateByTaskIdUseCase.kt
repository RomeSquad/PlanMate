package org.example.logic.usecase.state

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class GetStateByTaskIdUseCase(
    private val stateRepository: StateRepository
) {
    fun execute(taskId:Int):State{
        require(taskId > 0) { "task id must be greater than 0" }
        return stateRepository.getStateByTaskId(taskId)
    }
}