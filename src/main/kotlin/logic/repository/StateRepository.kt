package org.example.logic.repository

import org.example.logic.entity.State

interface StateRepository {
    fun addState(state: State)
    fun editState(projectId: Int , newStateName:String)
    fun deleteState(projectId: Int)

    fun getStateByTaskId (taskId: Int): State

    fun getAllStatesProject(): List<State>
}
