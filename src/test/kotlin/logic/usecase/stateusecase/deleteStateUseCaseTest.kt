package logic.usecase.stateusecase

import io.mockk.*
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.stateusecase.DeleteStateUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class deleteStateUseCaseTest {
    lateinit var stateRepository: StateRepository
    lateinit var deleteStateUseCase: DeleteStateUseCase

    @BeforeEach
    fun setUp() {
        stateRepository = mockk()
        deleteStateUseCase = DeleteStateUseCase(stateRepository)
    }


    @Test
    fun `should delete state successfully`() {
        val state = State(projectId = "1", name = "done")

        every { stateRepository.getDeleteState(state.name) } just Runs

        val result = deleteStateUseCase.deleteState(state)

        assertTrue(result)
        verify { stateRepository.getDeleteState("done") }
    }
}