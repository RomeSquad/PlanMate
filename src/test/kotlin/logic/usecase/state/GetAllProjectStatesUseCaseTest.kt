package logic.usecase.state

import io.mockk.every
import io.mockk.mockk
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.GetAllProjectStatesUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllProjectStatesUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var getAllStatesUseCase: GetAllProjectStatesUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        getAllStatesUseCase = GetAllProjectStatesUseCase(stateRepository)
    }

    @Test
    fun `should return all states of the project from repository`() {
        val allStates = listOf(
            State(projectId = 1, stateName = "todo"),
            State(projectId = 2, stateName = "pending")
        )
        every { stateRepository.getAllStatesProject() } returns allStates

        val result = getAllStatesUseCase.execute()

        assertEquals("todo", result[0].stateName)
    }

}

