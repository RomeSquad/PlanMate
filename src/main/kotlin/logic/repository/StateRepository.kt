package org.example.logic.repository

import org.example.logic.entity.State

interface StateRepository {
    fun getAllStates(): List<State>
    fun addState(state: State): Boolean
    fun editState(stateId:String , newStateName:String) : Boolean
    fun deleteState(id: String): Boolean
}
