package org.example.logic.repository

import org.example.logic.entity.ProjectState
import org.example.logic.request.ProjectStateEditRequest
import java.util.*

interface ProjectStateRepository {
    suspend fun addProjectState(state: ProjectState)
    suspend fun editProjectState(request: ProjectStateEditRequest)
    suspend fun deleteProjectState(projectId: UUID): Boolean

    suspend fun getProjectStateByTaskId(taskId: UUID): ProjectState

    suspend fun getAllProjectStates(): List<ProjectState>
}
