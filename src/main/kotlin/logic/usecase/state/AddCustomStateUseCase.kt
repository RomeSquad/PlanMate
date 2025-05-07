package org.example.logic.usecase.state

import org.example.logic.entity.State
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.StateRepository

class AddCustomStateUseCase(

    private val stateRepository: StateRepository
) {

    fun executeAddCustomStateUseCase(currentUser: User, stateName: String, projectId: String): Boolean {
        require(stateName.isNotBlank()) { "state name must not be blank" }
        require(projectId.isNotBlank()) { "project id must not be blank" }

        if (currentUser.userRole != UserRole.ADMIN) {
            throw IllegalAccessException("Only admins can add new states.")
        }

        val state = State(
            stateName = stateName,
            projectId = projectId
        )
        return stateRepository.addState(state)
    }
}
