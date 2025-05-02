package org.example.data.datasource.state

import org.example.logic.entity.State

interface StateDataSource {
    fun getAllStates(): List<State>
    fun addState(state: State)
    fun editState(stateId : String, newStateName: String)
    fun deleteState(id: String)
}