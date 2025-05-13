package org.example.logic.usecase.state

import org.example.logic.entity.ChangeHistory
import org.example.logic.entity.ProjectState
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.CantAddStateWithNoNameException
import org.example.logic.exception.NotAccessException
import org.example.logic.repository.AuthRepository
import org.example.logic.repository.ChangeHistoryRepository
import org.example.logic.repository.ProjectStateRepository
import java.util.*

class AddTaskStateToProjectUseCase(
    private val projectStatesRepository: ProjectStateRepository,
    private val changeHistoryRepository: ChangeHistoryRepository,
    private val authRepository: AuthRepository
) {
    suspend fun execute(state: ProjectState, userId: UUID) {
        val user = authRepository.getUserById(userId)
        if (user?.userRole != UserRole.ADMIN) throw NotAccessException("Only Admin can add states")

        if (state.stateName.isBlank()) throw CantAddStateWithNoNameException("state name can't be empty")

        projectStatesRepository.addProjectState(
            state
        )
        val audit = createAudit(state, user)
        changeHistoryRepository.addChangeHistory(audit)
    }

    private fun createAudit(state: ProjectState, user: User): ChangeHistory {
        val description = "create ${state.stateName} state is created successfully"
        return ChangeHistory(
            projectID = state.projectId,
            taskID = state.projectId,
            authorID = user.userId,
            changeDate = Date(),
            changeDescription = description,
        )
    }
}