package org.example.logic.repository

import org.example.logic.entity.State

interface StateRepository {
    fun getAllStates(): List<State>
    fun getAddState(state: State)
    fun getUpdateState(stateId: String, newName: String)
    fun getDeleteState(stateId: String)

}