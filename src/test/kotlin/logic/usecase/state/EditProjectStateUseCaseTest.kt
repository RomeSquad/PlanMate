package logic.usecase.state

import io.mockk.*
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.EditProjectStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class EditProjectStateUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var editStateUseCase: EditProjectStateUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        editStateUseCase = EditProjectStateUseCase(stateRepository)
    }


    @Test
    fun ` should throw exception when new state name is blank`() {
        assertThrows<IllegalArgumentException> {
            editStateUseCase.execute(1, "")
        }
    }

    @Test
    fun ` should edit state successfully`() {
        val projectId = 1
        val newStateName = "code review"

        every { stateRepository.editState(projectId, newStateName) } just Runs

        editStateUseCase.execute(1, "code review")

        verify(exactly = 1) { editStateUseCase.execute(projectId, newStateName) }
    }

}
