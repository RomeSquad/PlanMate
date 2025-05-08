package logic.usecase.state

import io.mockk.*
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.DeleteProjectStatesUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteProjectStatesUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var deleteStatesUseCase: DeleteProjectStatesUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        deleteStatesUseCase = DeleteProjectStatesUseCase(stateRepository)
    }

    @Test
    fun ` should throw exception when state name is blank`() {
        every { stateRepository.deleteState(2) } just Runs

        deleteStatesUseCase.execute(2)

        verify(exactly = 1) { stateRepository.deleteState(2) }
    }
}
