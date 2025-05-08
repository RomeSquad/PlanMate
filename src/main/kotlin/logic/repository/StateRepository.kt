package org.example.logic.repository

import org.example.logic.entity.State

interface StateRepository {
    fun getAllStatesProject(): List<State>
    fun addState(state: State)
    fun editState(projectId: Int , newStateName:String)
    fun deleteState(projectId: Int)
    fun getStateById (projectId: Int): State
}
