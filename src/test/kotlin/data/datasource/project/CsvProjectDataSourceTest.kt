package data.datasource.project


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

    private val firstProjectID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
    private val secondProjectID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")

    @BeforeEach
    fun setup() {
        stateProjectDataSource = mockk(relaxed = true)
        stateRepository = ProjectStateRepositoryImpl(stateProjectDataSource)
    }

    @Test
    fun `getAllStates should return list of states`() = runTest {
        // Given
        val mockStates = listOf(
            ProjectState(projectId = firstProjectID, stateName = "Cairo"),
            ProjectState(projectId = secondProjectID, stateName = "Alex")
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
        val state = ProjectState(projectId = firstProjectID, stateName = "inProgress")

        // When
        stateRepository.addProjectState(state)

        // Then
        coVerify { stateProjectDataSource.addProjectState(state) }
    }

    @Test
    fun `deleteState should call dataSource deleteState`() = runTest {
        // Given
        val id = firstProjectID

        // When
        stateRepository.deleteProjectState(id)

        // Then
        coVerify { stateProjectDataSource.deleteProjectState(id) }
    }

    @Test
    fun `editState should call dataSource editState`() = runTest {
        // Given
        val projectId = firstProjectID
        val newStateName = "UpdatedState"

        // When
        stateRepository.editProjectState(projectId, newStateName)

        // Then
        coVerify { stateProjectDataSource.editProjectState(projectId, newStateName) }
    }

    @Test
    fun `getStateById should return state from dataSource`() = runTest {
        // Given
        val projectId = firstProjectID
        val mockState = ProjectState(projectId = projectId, stateName = "Cairo")
        coEvery { stateProjectDataSource.getStateById(projectId) } returns mockState

        // When
        val result = stateRepository.getProjectStateByTaskId(projectId)

        // Then
        assertEquals(mockState, result)
        coVerify { stateProjectDataSource.getStateById(projectId) }
    }
}