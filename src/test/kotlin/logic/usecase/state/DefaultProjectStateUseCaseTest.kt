package logic.usecase.state

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.AddStatesUseCase
import org.example.logic.usecase.state.DefaultProjectStateUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultProjectStateUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var addStatesUseCase: AddStatesUseCase
    private lateinit var defaultProjectStateUseCase: DefaultProjectStateUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        addStatesUseCase = AddStatesUseCase(stateRepository)
        defaultProjectStateUseCase = DefaultProjectStateUseCase(stateRepository)
    }

    @Test
    fun `should initialize default states for a new project`() {
        val getStates = mutableListOf<State>()

        every { stateRepository.addState(capture(getStates)) } just Runs

        defaultProjectStateUseCase.initializeProjectState(2)
        assertTrue(getStates.any { it.stateName == "todo" || it.stateName == "in progress" || it.stateName == "done" })
    }

    @Test
    fun `should have 3 default state for a new project`() {
        val getStates = mutableListOf<State>()

        every { stateRepository.addState(capture(getStates)) } just Runs

        defaultProjectStateUseCase.initializeProjectState(3)

        assertEquals(3, getStates.size)
    }

}
