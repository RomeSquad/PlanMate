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

class AddProjectStatesUseCase(
    private val stateRepository: ProjectStateRepository,
    private val authRepository: AuthRepository,
    private val changeHistoryRepository: ChangeHistoryRepository,
    ) {
    suspend fun execute(projectId: UUID, stateName: String) {
        if (stateName.isBlank()) throw CantAddStateWithNoNameException("state name can't be empty")

        val user = authRepository.getCurrentUser()
        if (user?.userRole != UserRole.ADMIN) throw NotAccessException("Only Admin can add states")

        val defaultStateNames = listOf("todo", "in progress", "done")
        defaultStateNames.forEach { name ->
            val projectState = ProjectState(
                stateName = name,
                projectId = projectId
            )
            stateRepository.addProjectState(projectState)
        }
        val audit = createAudit(stateName, user,projectId,)
        changeHistoryRepository.addChangeHistory(audit)
    }

    private fun createAudit(stateName: String, user: User, projectId: UUID): ChangeHistory {
        val description = "create $stateName state is created successfully"
        return ChangeHistory(
            projectID = projectId,
            taskID = projectId,
            authorID = user.userId,
            changeDate = Date(),
            changeDescription = description,
        )
    }
}