package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.UserNotFoundException
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.GetCurrentUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class GetCurrentUserUseCaseTest {
    private lateinit var authenticationRepository: AuthRepository
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        getCurrentUserUseCase = GetCurrentUserUseCase(authenticationRepository)
    }

    @Test
    fun ` should getCurrentUser return current user when it exists(successful)`() = runTest {
        val user = User(userId = UUID(1, 1), username = "abanoub", userRole = UserRole.ADMIN)
        coEvery { authenticationRepository.getCurrentUser() } returns user

        val result = getCurrentUserUseCase.getCurrentUser()

        assert(result != null)
        coVerify(exactly = 1) { authenticationRepository.getCurrentUser() }
    }

    @Test
    fun `getCurrentUser should throw DtoNotFoundException when user is not found`() = runTest {
        coEvery { authenticationRepository.getCurrentUser() } throws UserNotFoundException()

        assertThrows<UserNotFoundException> {
            getCurrentUserUseCase.getCurrentUser()
        }
    }
}