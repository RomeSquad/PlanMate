package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.GetUserByUsernameUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class GetUserByUsernameUseCaseTest {
    private lateinit var getUserByUsernameUseCase: GetUserByUsernameUseCase
    private lateinit var authRepository: AuthRepository

    @BeforeEach
    fun setup() {
        authRepository = mockk<AuthRepository>()
        getUserByUsernameUseCase = GetUserByUsernameUseCase(authRepository)
    }

    @Test
    fun `should return user when repository finds user by username`() = runTest {
        // Given
        val username = "amr"
        val expectedUser = User(
            userId = UUID.randomUUID(),
            username = username,
            userRole = UserRole.MATE
        )
        coEvery { authRepository.getUserByUserName(username) } returns expectedUser

        // When
        val result = getUserByUsernameUseCase.getUserByUsername(username)

        // Then
        Assertions.assertNotNull(result)
        Assertions.assertEquals(expectedUser, result)
        Assertions.assertEquals(username, result?.username)
    }

    @Test
    fun `should return null when repository does not find user by username`() = runTest {
        // Given
        val username = "nonexistent"
        coEvery { authRepository.getUserByUserName(username) } returns null

        // When
        val result = getUserByUsernameUseCase.getUserByUsername(username)

        // Then
        Assertions.assertNull(result)
    }

    @Test
    fun `should return null when username is empty`() = runTest {
        // Given
        val username = ""
        coEvery { authRepository.getUserByUserName(username) } returns null

        // When
        val result = getUserByUsernameUseCase.getUserByUsername(username)

        // Then
        Assertions.assertNull(result)
    }
}