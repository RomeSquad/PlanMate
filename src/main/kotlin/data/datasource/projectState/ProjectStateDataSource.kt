package org.example.data.datasource.state

import org.example.logic.entity.ProjectState

interface ProjectStateDataSource {
    fun getAllProjectStates(): List<ProjectState>// for project
    fun addProjectState(state: ProjectState)
    fun editProjectState(projectId : Int, newStateName: String)
    fun deleteProjectState(projectId: Int)
    fun getStateById (projectId: Int): ProjectState
}
