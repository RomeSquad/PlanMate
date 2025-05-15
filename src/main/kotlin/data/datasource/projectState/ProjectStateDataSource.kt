package data.datasource.projectState

import org.example.logic.entity.ProjectState
import org.example.logic.request.auth.ProjectStateEditRequest
import java.util.*

interface ProjectStateDataSource {
    suspend fun getAllProjectStates(): List<ProjectState>
    suspend fun addProjectState(state: ProjectState)
    suspend fun editProjectState(request : ProjectStateEditRequest)
    suspend fun deleteProjectState(projectId: UUID): Boolean
    suspend fun getStateById(projectId: UUID): ProjectState
}