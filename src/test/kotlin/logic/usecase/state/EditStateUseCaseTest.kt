package logic.usecase.state

import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.EditStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

class EditStateUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var editStateUseCase: EditStateUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        editStateUseCase = EditStateUseCase(stateRepository)
    }

    @Test
    fun ` should throw exception when state id is blank`() {
        assertThrows<IllegalArgumentException> {
            editStateUseCase.executeEditState("", "inProgress")
        }
    }

    @Test
    fun ` should throw exception when new state name is blank`() {
        assertThrows<IllegalArgumentException> {
            editStateUseCase.executeEditState("1", "")
        }
    }

    @Test
    fun ` should add state to project successfully`() {
        every { stateRepository.editState("1", "todo") } returns true
        val result = editStateUseCase.executeEditState("1", "todo")

        assertTrue { result }
    }

}