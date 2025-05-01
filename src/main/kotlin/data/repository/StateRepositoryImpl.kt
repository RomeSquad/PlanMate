import org.example.data.datasource.state.StateDataSource
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class StateRepositoryImpl(private val stateDataSource: StateDataSource) : StateRepository {
    override fun getAllStates(): List<State> {
        return stateDataSource.getAllStates()
    }

    override fun addState(state: State) {
        return stateDataSource.addState(state)
    }

    override fun editState(id: String) {
        return stateDataSource.editState(id)
    }

    override fun deleteState(id: String) {
        stateDataSource.deleteState(id)
    }
}