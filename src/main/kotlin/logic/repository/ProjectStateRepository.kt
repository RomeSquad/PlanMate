package org.example.logic.repository

import org.example.logic.entity.ProjectState

interface ProjectStateRepository {
    fun addProjectState(state: ProjectState)
    fun editProjectState(projectId: Int, newStateName:String)
    fun deleteProjectState(projectId: Int)

    fun getProjectStateByTaskId (taskId: Int): ProjectState

    fun getAllProjectStates(): List<ProjectState>
}
