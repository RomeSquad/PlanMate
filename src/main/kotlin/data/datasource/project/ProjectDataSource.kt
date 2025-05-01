package org.example.data.datasource.project

import org.example.logic.entity.State

interface ProjectDataSource  {
    fun getAllStates(): List<State>
    fun addState(state: State)
    fun editState(id: String)
    fun deleteState(id: String)
}