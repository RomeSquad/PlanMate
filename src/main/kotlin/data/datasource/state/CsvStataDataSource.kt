package org.example.data.datasource.state

import org.example.logic.entity.State
import java.io.File

class CsvStateDataSource(val statefile :File) : StateDataSource {
    override fun getAllStates(): List<State> {
        TODO("Not yet implemented")
    }

    override fun addState(state: State) {
        TODO("Not yet implemented")
    }

    override fun editState(stateId: String, newStateName: String) {
        TODO("Not yet implemented")
    }

    override fun deleteState(id: String) {
        TODO("Not yet implemented")
    }

}
