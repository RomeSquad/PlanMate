package org.example.logic.usecase.state

import org.example.logic.repository.StateRepository

class EditStateUseCase(private val stateRepository: StateRepository) {
    fun executeEditState(stateId:String , newStateName:String):Boolean{
        require(stateId.isNotBlank()){"should state id must not be blank"}
        require(newStateName.isNotBlank()){"should new state name must not be blank"}

        return stateRepository.editState(stateId,newStateName)
    }
}

