package data.datasource.projectState

import org.example.logic.entity.ProjectState

interface ProjectStateDataSource {
    suspend fun getAllProjectStates(): List<ProjectState>
    suspend fun addProjectState(state: ProjectState)
    suspend fun editProjectState(projectId: Int, newStateName: String)
    suspend fun deleteProjectState(projectId: Int)
    suspend fun getStateById(projectId: Int): ProjectState
}