package org.example.logic.usecase.state

import org.example.logic.repository.StateRepository

class EditStateUseCase(private val stateRepository: StateRepository) {
    fun editState(stateId:String , newStateName:String){
        stateRepository.editState(stateId,newStateName)
    }
}

