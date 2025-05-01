package org.example.logic.usecase.stateusecase

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class DeleteStateUseCase(repository: StateRepository){

    fun deleteState(state: State):Boolean{
        return false
    }
}