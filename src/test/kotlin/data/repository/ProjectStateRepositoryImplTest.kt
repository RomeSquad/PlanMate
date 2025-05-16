package data.repository

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
import java.util.UUID
import kotlin.test.assertEquals

class ProjectStateRepositoryImplTest {
    private lateinit var stateProjectDataSource: ProjectStateDataSource
    private lateinit var stateRepository: ProjectStateRepository

    private val projectId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    private val projectId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")

    @BeforeEach
    fun setup() {
        stateProjectDataSource = mockk(relaxed = true)
        stateRepository = ProjectStateRepositoryImpl(stateProjectDataSource)
    }

    @Test
    fun `getAllStates should return list of states`() = runTest {
        val mockStates = listOf(
            ProjectState(projectId = projectId1, stateName = "Cairo"),
            ProjectState(projectId = projectId2, stateName = "Alex")
        )
        coEvery { stateProjectDataSource.getAllProjectStates() } returns mockStates

        val result = stateRepository.getAllProjectStates()

        assertEquals(mockStates, result)
        coVerify { stateProjectDataSource.getAllProjectStates() }
    }

    @Test
    fun `addState should call dataSource addState`() = runTest {
        val state = ProjectState(projectId = projectId1, stateName = "inProgress")

        stateRepository.addProjectState(state)

        coVerify { stateProjectDataSource.addProjectState(state) }
    }

    @Test
    fun `deleteState should call dataSource deleteState`() = runTest {
        val id = projectId1

        stateRepository.deleteProjectState(id)

        coVerify { stateProjectDataSource.deleteProjectState(id) }
    }

    @Test
    fun `editState should call dataSource editState`() = runTest {
        val projectId = projectId1
        val newStateName = "UpdatedState"

        stateRepository.editProjectState(ProjectStateEditRequest(projectId, newStateName))

        coVerify { stateProjectDataSource.editProjectState(ProjectStateEditRequest(projectId, newStateName)) }
    }

    @Test
    fun `getStateById should return state from dataSource`() = runTest {
        val projectId = projectId1
        val mockState = ProjectState(projectId = projectId, stateName = "Cairo")
        coEvery { stateProjectDataSource.getStateById(projectId) } returns mockState

        val result = stateRepository.getProjectStateByTaskId(projectId)

        assertEquals(mockState, result)
        coVerify { stateProjectDataSource.getStateById(projectId) }
    }
}
