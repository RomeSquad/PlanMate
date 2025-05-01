package logic.usecase.stateusecase

import io.mockk.*
import org.example.logic.entity.State
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.stateusecase.DeleteStateUseCase
import org.example.logic.usecase.stateusecase.EditStateUseCase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class editStateUseCaseTest {
    lateinit var stateRepository: StateRepository
    lateinit var editStateUseCase: EditStateUseCase

    @BeforeEach
    fun setUp() {
        stateRepository = mockk()
        editStateUseCase = EditStateUseCase(stateRepository)
    }

    @Test
    fun `should edit state when enter the edit state`() {
        val state = State(projectId = "2", name = "edit")
        val editState = State("1", "new state name")
        every { stateRepository.getUpdateState( state.projectId,state.name) } just Runs

        val result = editStateUseCase.editState(state)

        kotlin.test.assertEquals(result, editState)
        verify { stateRepository.getUpdateState("1", "done") }
    }
}