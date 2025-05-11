import data.datasource.projectState.ProjectStateDataSource
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository

class ProjectStateRepositoryImpl(
    private val projectStateDataSource: ProjectStateDataSource
) : ProjectStateRepository {

    override suspend fun getAllProjectStates(): List<ProjectState> {
        return projectStateDataSource.getAllProjectStates()
    }

    override suspend fun addProjectState(state: ProjectState) {
        return projectStateDataSource.addProjectState(state)
    }

    override suspend fun editProjectState(projectId: Int, newStateName: String) {
        return projectStateDataSource.editProjectState(projectId, newStateName)
    }

    override suspend fun deleteProjectState(projectId: Int) {
        return projectStateDataSource.deleteProjectState(projectId)
    }

    override suspend fun getProjectStateByTaskId(taskId: Int): ProjectState {
        return projectStateDataSource.getStateById(taskId)
    }
}