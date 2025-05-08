import org.example.data.datasource.state.ProjectStateDataSource
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository

class ProjectStateRepositoryImpl(private val stateProjectDataSource: ProjectStateDataSource) : StateRepository {

    override fun getAllStatesProject(): List<State> {
        return stateProjectDataSource.getAllStatesProject()
    }

    override fun addState(state: State) {
        return stateProjectDataSource.addState(state)
    }

    override fun editState(projectId: Int, newStateName: String) {
        return stateProjectDataSource.editState(projectId, newStateName)
    }

    override fun deleteState(projectId: Int) {
        return stateProjectDataSource.deleteState(projectId)
    }

    override fun getStateByTaskId(taskId: Int): State {
        return stateProjectDataSource.getStateById(taskId)
    }
}