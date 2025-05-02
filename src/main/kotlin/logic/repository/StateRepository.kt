package org.example.logic.repository

import org.example.logic.entity.State

interface StateRepository {
    fun getAllStates(): List<State>
    fun addState(state: State)
    fun editState(stateId:String , newStateName:String)
    fun deleteState(id: String)
}
