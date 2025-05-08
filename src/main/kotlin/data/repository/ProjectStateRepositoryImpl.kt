import org.example.data.datasource.state.ProjectStateDataSource
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository

class ProjectStateRepositoryImpl(
    private val projectStateDataSource: ProjectStateDataSource
) : ProjectStateRepository {

    override fun getAllProjectStates(): List<ProjectState> {
        return projectStateDataSource.getAllProjectStates()
    }

    override fun addProjectState(state: ProjectState) {
        return projectStateDataSource.addProjectState(state)
    }

    override fun editProjectState(projectId: Int, newStateName: String) {
        return projectStateDataSource.editProjectState(projectId, newStateName)
    }

    override fun deleteProjectState(projectId: Int) {
        return projectStateDataSource.deleteProjectState(projectId)
    }

    override fun getProjectStateByTaskId(taskId: Int): ProjectState {
        return projectStateDataSource.getStateById(taskId)
    }
}