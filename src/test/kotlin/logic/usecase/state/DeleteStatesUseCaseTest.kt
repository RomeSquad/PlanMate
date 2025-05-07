package logic.usecase.state

import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.DeleteStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class DeleteStatesUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var deleteStatesUseCase: DeleteStatesUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        deleteStatesUseCase = DeleteStatesUseCase(stateRepository)
    }

    @Test
    fun ` should throw exception when state name is blank`() {
        every { stateRepository.deleteState("2") } returns true

        val result = deleteStatesUseCase.executeDeleteState("2")

        assertTrue(result)
    }
}
