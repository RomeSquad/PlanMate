package data.datasource.projectState

import org.example.logic.entity.ProjectState
import java.util.*

interface ProjectStateDataSource {
    suspend fun getAllProjectStates(): List<ProjectState>
    suspend fun addProjectState(state: ProjectState)
    suspend fun editProjectState(projectId: UUID, newStateName: String)
    suspend fun deleteProjectState(projectId: UUID): Boolean
    suspend fun getStateById(projectId: UUID): ProjectState
}