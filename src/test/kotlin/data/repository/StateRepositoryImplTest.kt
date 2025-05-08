import io.mockk.*
import org.example.data.datasource.state.StateProjectDataSource
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StateRepositoryImplTest {

    private lateinit var stateProjectDataSource: StateProjectDataSource
    private lateinit var stateRepository: StateRepository

    @BeforeEach
    fun setup() {
        stateProjectDataSource = mockk(relaxed = true)
        stateRepository = StateRepositoryImpl(stateProjectDataSource)
    }

    @Test
    fun `getAllStates should return list of states`() {
        // Given
        val mockStates = listOf(
            State(projectId = 1, stateName = "Cairo"),
            State(projectId = 2, stateName = "Alex")
        )
        every { stateProjectDataSource.getAllStatesProject() } returns mockStates

        // When
        val result = stateRepository.getAllStatesProject()

        // Then
        assertEquals(mockStates, result)
        verify { stateProjectDataSource.getAllStatesProject() }
    }

    @Test
    fun `addState should call dataSource addState`() {
        // Given
        val state = State(projectId = 5, stateName = "inPrograss")

        // When
        stateRepository.addState(state)

        // Then
        verify { stateProjectDataSource.addState(state) }
    }


    @Test
    fun `deleteState should call dataSource deleteState`() {
        // Given
        val id = 2

        // When
        stateRepository.deleteState(id)

        // Then
        verify { stateProjectDataSource.deleteState(id) }
    }
}
