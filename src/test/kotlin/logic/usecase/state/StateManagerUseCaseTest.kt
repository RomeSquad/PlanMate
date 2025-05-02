package logic.usecase.state

import io.mockk.*
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.GetAllUsersUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllUsersUseCaseTest {

    private lateinit var stateRepository: StateRepository
    private lateinit var useCase: GetAllUsersUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        useCase = GetAllUsersUseCase(stateRepository)
    }
    // TODO implement test cases
}