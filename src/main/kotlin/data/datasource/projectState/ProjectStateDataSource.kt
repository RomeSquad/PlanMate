package org.example.data.datasource.state

import org.example.logic.entity.ProjectState

interface ProjectStateDataSource {
    suspend fun getAllProjectStates(): List<ProjectState>// for project
    suspend fun addProjectState(state: ProjectState)
    suspend fun editProjectState(projectId : Int, newStateName: String)
    suspend fun deleteProjectState(projectId: Int)
    suspend fun getStateById (projectId: Int): ProjectState
}
