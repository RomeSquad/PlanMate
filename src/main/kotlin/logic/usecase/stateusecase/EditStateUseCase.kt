package org.example.logic.usecase.stateusecase

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class EditStateUseCase (repository: StateRepository){

    fun editState(state: State):State{
        return State(name = "edit", projectId = "2")
    }
}