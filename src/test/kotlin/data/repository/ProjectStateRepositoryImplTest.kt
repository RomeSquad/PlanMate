package data.repository

import ProjectStateRepositoryImpl
import data.datasource.projectState.ProjectStateDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
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
        // Given
        val mockStates = listOf(
            ProjectState(projectId = projectId1, stateName = "Cairo"),
            ProjectState(projectId = projectId2, stateName = "Alex")
        )
        coEvery { stateProjectDataSource.getAllProjectStates() } returns mockStates

        // When
        val result = stateRepository.getAllProjectStates()

        // Then
        assertEquals(mockStates, result)
        coVerify { stateProjectDataSource.getAllProjectStates() }
    }

    @Test
    fun `addState should call dataSource addState`() = runTest {
        // Given
        val state = ProjectState(projectId = projectId1, stateName = "inProgress")

        // When
        stateRepository.addProjectState(state)

        // Then
        coVerify { stateProjectDataSource.addProjectState(state) }
    }

    @Test
    fun `deleteState should call dataSource deleteState`() = runTest {
        // Given
        val id = projectId1

        // When
        stateRepository.deleteProjectState(id)

        // Then
        coVerify { stateProjectDataSource.deleteProjectState(id) }
    }

    @Test
    fun `editState should call dataSource editState`() = runTest {
        // Given
        val projectId = projectId1
        val newStateName = "UpdatedState"

        // When
        stateRepository.editProjectState(projectId, newStateName)

        // Then
        coVerify { stateProjectDataSource.editProjectState(projectId, newStateName) }
    }

    @Test
    fun `getStateById should return state from dataSource`() = runTest {
        // Given
        val projectId = projectId1
        val mockState = ProjectState(projectId = projectId, stateName = "Cairo")
        coEvery { stateProjectDataSource.getStateById(projectId) } returns mockState

        // When
        val result = stateRepository.getProjectStateByTaskId(projectId)

        // Then
        assertEquals(mockState, result)
        coVerify { stateProjectDataSource.getStateById(projectId) }
    }
}
