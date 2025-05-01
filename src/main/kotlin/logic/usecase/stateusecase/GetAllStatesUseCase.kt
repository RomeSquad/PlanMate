package org.example.logic.usecase.stateusecase

import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class GetAllStatesUseCase (repository: StateRepository) {

    fun getAllStates (state: String): List<State> {
        return listOf( State(name = "todo", projectId = "1"),State(name = "done", projectId = "1"))
    }
}