import data.datasource.projectState.ProjectStateDataSource
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.request.ProjectStateEditRequest
import java.util.*

class ProjectStateRepositoryImpl(
    private val projectStateDataSource: ProjectStateDataSource
) : ProjectStateRepository {

    override suspend fun getAllProjectStates(): List<ProjectState> {
        return projectStateDataSource.getAllProjectStates()
    }

    override suspend fun addProjectState(state: ProjectState) {
        return projectStateDataSource.addProjectState(state)
    }

    override suspend fun editProjectState(request: ProjectStateEditRequest) {
        return projectStateDataSource.editProjectState(request)
    }

    override suspend fun deleteProjectState(projectId: UUID): Boolean {
        return projectStateDataSource.deleteProjectState(projectId)
    }

    override suspend fun getProjectStateByTaskId(taskId: UUID): ProjectState {
        return projectStateDataSource.getStateById(taskId)
    }
}