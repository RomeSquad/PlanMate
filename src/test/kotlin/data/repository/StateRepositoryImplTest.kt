import io.mockk.*
import org.example.data.datasource.state.StateDataSource
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StateRepositoryImplTest {

    private lateinit var stateDataSource: StateDataSource
    private lateinit var stateRepository: StateRepository

    @BeforeEach
    fun setup() {
        stateDataSource = mockk(relaxed = true)
        stateRepository = StateRepositoryImpl(stateDataSource)
    }

    @Test
    fun `getAllStates should return list of states`() {
        // Gevin
        val mockStates = listOf(
            State(projectId = "1", name = "Cairo"),
            State(projectId = "2", name = "Alex")
        )
        every { stateDataSource.getAllStates() } returns mockStates

        // When
        val result = stateRepository.getAllStates()

        // Then
        assertEquals(mockStates, result)
        verify { stateDataSource.getAllStates() }
    }

    @Test
    fun `addState should call dataSource addState`() {
        // Gevin
        val state = State(projectId = "5", name = "inPrograss")

        // When
        stateRepository.addState(state)

        // Then
        verify { stateDataSource.addState(state) }
    }

    @Test
    fun `editState should call dataSource editState`() {
        // GEvin
        val id = "1"

        // When
        stateRepository.editState(id)

        // Then
        verify { stateDataSource.editState(id) }
    }

    @Test
    fun `deleteState should call dataSource deleteState`() {
        // GEven
        val id = "2"

        // When
        stateRepository.deleteState(id)

        // Then
        verify { stateDataSource.deleteState(id) }
    }
}
