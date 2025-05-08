package logic.usecase.state

import io.mockk.every
import io.mockk.mockk
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.GetStateByTaskIdUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetStateByTaskIdUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var getStateByTaskIdUseCase: GetStateByTaskIdUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        getStateByTaskIdUseCase = GetStateByTaskIdUseCase(stateRepository)
    }

    @Test
    fun `should return state name of the task`() {
        val mockState = State(projectId = 1, stateName = "todo")
        val taskId = 1
        every { stateRepository.getStateByTaskId(taskId) } returns mockState

        val result = getStateByTaskIdUseCase.execute(taskId)

        assertEquals("todo", result.stateName)
    }

    @Test
    fun ` should throw exception when task id equal to zero`() {
        assertThrows<IllegalArgumentException> {
            getStateByTaskIdUseCase.execute(0)
        }
    }

}