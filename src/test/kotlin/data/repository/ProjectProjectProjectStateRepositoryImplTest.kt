import io.mockk.*
import org.example.data.datasource.state.ProjectStateDataSource
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectStateRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProjectProjectProjectStateRepositoryImplTest {

    private lateinit var stateProjectDataSource: ProjectStateDataSource
    private lateinit var stateRepository: ProjectStateRepository

    @BeforeEach
    fun setup() {
        stateProjectDataSource = mockk(relaxed = true)
        stateRepository = ProjectStateRepositoryImpl(stateProjectDataSource)
    }

    @Test
    fun `getAllStates should return list of states`() {
        // Given
        val mockStates = listOf(
            ProjectState(projectId = 1, stateName = "Cairo"),
            ProjectState(projectId = 2, stateName = "Alex")
        )
        every { stateProjectDataSource.getAllProjectStates() } returns mockStates

        // When
        val result = stateRepository.getAllProjectStates()

        // Then
        assertEquals(mockStates, result)
        verify { stateProjectDataSource.getAllProjectStates() }
    }

    @Test
    fun `addState should call dataSource addState`() {
        // Given
        val state = ProjectState(projectId = 5, stateName = "inPrograss")

        // When
        stateRepository.addProjectState(state)

        // Then
        verify { stateProjectDataSource.addProjectState(state) }
    }


    @Test
    fun `deleteState should call dataSource deleteState`() {
        // Given
        val id = 2

        // When
        stateRepository.deleteProjectState(id)

        // Then
        verify { stateProjectDataSource.deleteProjectState(id) }
    }
}
