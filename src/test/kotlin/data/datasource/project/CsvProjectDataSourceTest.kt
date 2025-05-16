package data.datasource.project


import ProjectStateRepositoryImpl
import data.datasource.projectState.ProjectStateDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.request.ProjectStateEditRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class ProjectStateRepositoryImplTest {
    private lateinit var stateProjectDataSource: ProjectStateDataSource
    private lateinit var stateRepository: ProjectStateRepository

    private val firstProjectID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    private val secondProjectID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")

    @BeforeEach
    fun setup() {
        stateProjectDataSource = mockk(relaxed = true)
        stateRepository = ProjectStateRepositoryImpl(stateProjectDataSource)
    }

    @Test
    fun `getAllStates should return list of states`() = runTest {
        val mockStates = listOf(
            ProjectState(projectId = firstProjectID, stateName = "Cairo"),
            ProjectState(projectId = secondProjectID, stateName = "Alex")
        )
        coEvery { stateProjectDataSource.getAllProjectStates() } returns mockStates

        val result = stateRepository.getAllProjectStates()

        assertEquals(mockStates, result)
        coVerify { stateProjectDataSource.getAllProjectStates() }
    }

    @Test
    fun `addState should call dataSource addState`() = runTest {
        val state = ProjectState(projectId = firstProjectID, stateName = "inProgress")

        stateRepository.addProjectState(state)

        coVerify { stateProjectDataSource.addProjectState(state) }
    }

    @Test
    fun `deleteState should call dataSource deleteState`() = runTest {
        val id = firstProjectID

        stateRepository.deleteProjectState(id)

        coVerify { stateProjectDataSource.deleteProjectState(id) }
    }

    @Test
    fun `editState should call dataSource editState`() = runTest {
        val projectId = firstProjectID
        val newStateName = "UpdatedState"

        stateRepository.editProjectState(ProjectStateEditRequest(projectId, newStateName))

        coVerify { stateProjectDataSource.editProjectState(ProjectStateEditRequest(projectId, newStateName)) }
    }

    @Test
    fun `getStateById should return state from dataSource`() = runTest {
        val projectId = firstProjectID
        val mockState = ProjectState(projectId = projectId, stateName = "Cairo")
        coEvery { stateProjectDataSource.getStateById(projectId) } returns mockState

        val result = stateRepository.getProjectStateByTaskId(projectId)

        assertEquals(mockState, result)
        coVerify { stateProjectDataSource.getStateById(projectId) }
    }
}