package org.example.logic.repository

import org.example.logic.entity.ProjectState

interface ProjectStateRepository {
    suspend fun addProjectState(state: ProjectState)
    suspend fun editProjectState(projectId: Int, newStateName:String)
    suspend fun deleteProjectState(projectId: Int)

    suspend fun getProjectStateByTaskId (taskId: Int): ProjectState

    suspend fun getAllProjectStates(projectId: Int): List<ProjectState>
}
