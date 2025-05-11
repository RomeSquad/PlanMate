import data.datasource.projectState.ProjectStateDataSource
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import java.util.UUID

class ProjectStateRepositoryImpl(
    private val projectStateDataSource: ProjectStateDataSource
) : ProjectStateRepository {

    override suspend fun getAllProjectStates(): List<ProjectState> {
        return projectStateDataSource.getAllProjectStates()
    }

    override suspend fun addProjectState(state: ProjectState) {
        return projectStateDataSource.addProjectState(state)
    }

    override suspend fun editProjectState(projectId: UUID, newStateName: String) {
        return projectStateDataSource.editProjectState(projectId, newStateName)
    }

    override suspend fun deleteProjectState(projectId: UUID) {
        return projectStateDataSource.deleteProjectState(projectId)
    }

    override suspend fun getProjectStateByTaskId(taskId: UUID): ProjectState {
        return projectStateDataSource.getStateById(taskId)
    }
}