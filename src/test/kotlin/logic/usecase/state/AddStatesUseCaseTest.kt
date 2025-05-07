package logic.usecase.state

import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.AddStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

class AddStatesUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var addStatesUseCase: AddStatesUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        addStatesUseCase = AddStatesUseCase(stateRepository)
    }

    @Test
    fun ` should throw exception when state name is blank`() {
        assertThrows<IllegalArgumentException> {
            addStatesUseCase.executeAddState("", "1")
        }
    }

    @Test
    fun ` should throw exception when project id is blank`() {
        assertThrows<IllegalArgumentException> {
            addStatesUseCase.executeAddState("pending", "")
        }
    }

    @Test
    fun ` should add state to project successfully`() {
        every { stateRepository.addState(any()) } returns true
        val result = addStatesUseCase.executeAddState("todo", "2")

        assertTrue { result }
    }


}