package org.example.logic.repository

import org.example.logic.entity.ProjectState
import java.util.UUID

interface ProjectStateRepository {
    suspend fun addProjectState(state: ProjectState)
    suspend fun editProjectState(projectId: UUID, newStateName: String)
    suspend fun deleteProjectState(projectId: UUID): Boolean

    suspend fun getProjectStateByTaskId(taskId: UUID): ProjectState

    suspend fun getAllProjectStates(): List<ProjectState>
}
