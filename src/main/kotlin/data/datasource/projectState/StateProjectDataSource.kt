package org.example.data.datasource.state

import org.example.logic.entity.State

interface StateProjectDataSource {
    fun getAllStatesProject(): List<State>// for project
    fun addState(state: State)
    fun editState(projectId : Int, newStateName: String)
    fun deleteState(projectId: Int)
    fun getStateById (projectId: Int): State
}
