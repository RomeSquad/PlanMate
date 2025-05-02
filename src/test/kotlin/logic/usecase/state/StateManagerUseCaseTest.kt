package logic.usecase.state

import io.mockk.*
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.EditStateUseCase
import org.example.logic.usecase.state.GetAllStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetAllUsersUseCaseTest {

    private lateinit var stateRepository: StateRepository
    private lateinit var useCase: GetAllStatesUseCase
    private lateinit var editStateUseCase: EditStateUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        useCase = GetAllStatesUseCase(stateRepository)
    }

    @Test
    fun `should delete state successfully`() {
        val state = "done"

        every { stateRepository.deleteState(state) } just Runs

        stateRepository.deleteState(state)

        verify { stateRepository.deleteState("done") }
    }

    @Test
    fun `should edit state when enter state  editing information`() {
        val oldState = State(projectId = "2", stateName = "edit")

        every { stateRepository.editState("1", "new state name") } just Runs

        val result = editStateUseCase.editState("2", "todo")

        verify { stateRepository.editState("2", "todo") }
    }

}