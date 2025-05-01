package org.example.logic.repository

import org.example.logic.entity.State

interface StateRepository {
    fun getAllStates(): List<State>
    fun addState(state: State)
    fun editState(id: String)
    fun deleteState(id: String)
}
