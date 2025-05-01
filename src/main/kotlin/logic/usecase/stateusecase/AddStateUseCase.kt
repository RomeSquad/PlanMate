package org.example.logic.usecase.stateusecase

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class AddStateUseCase(repository: StateRepository) {

    fun addStates (name: String, projectId:String): State {
        return State(name = "test", projectId = "1")
    }
}