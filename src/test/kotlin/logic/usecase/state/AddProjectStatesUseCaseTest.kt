package logic.usecase.state

import io.mockk.*
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.AddProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AddProjectStatesUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var addStatesUseCase: AddProjectStatesUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        addStatesUseCase = AddProjectStatesUseCase(stateRepository)
    }

    @Test
    fun ` should throw exception when state name is blank`() {
        assertThrows<IllegalArgumentException> {
            addStatesUseCase.execute("", 1)
        }
    }

    @Test
    fun ` should add state to project successfully`() {
        every { stateRepository.addState(any()) } just Runs

        addStatesUseCase.execute("todo", 2)

        verify(exactly = 1) { stateRepository.addState(any()) }
    }

}

