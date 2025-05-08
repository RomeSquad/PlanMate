package org.example.logic.usecase.state

import org.example.logic.entity.ProjectState
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.ProjectStateRepository

class AddCustomProjectStateUseCase(
    private val stateRepository: ProjectStateRepository
) {

    fun execute(currentUser: User, stateName: String, projectId: Int) {
        require(stateName.isNotBlank()) { "state name must not be blank" }

        if (currentUser.userRole != UserRole.ADMIN) {
            throw IllegalAccessException("Only admins can add new states.")
        }

        val projectState = ProjectState(
            stateName = stateName,
            projectId = projectId
        )
        return stateRepository.addProjectState(projectState)
    }
}
