package org.example.logic.usecase.state

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class AddStatesUseCase(private val stateRepository: StateRepository) {
    fun executeAddState(stateName:String, projectId : String){
        val state = State(stateName,projectId)
        stateRepository.addState(state)
    }
}