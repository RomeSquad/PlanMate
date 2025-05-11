package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.PasswordLengthException
import org.example.logic.exception.UserNameOrPasswordEmptyException
import org.example.logic.repository.AuthRepository
import org.example.logic.request.auth.LoginRequest
import org.example.logic.usecase.auth.LoginUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.Test

class LoginUseCaseTest {


    private val authRepository: AuthRepository = mockk()
    private val loginUseCase = LoginUseCase(authRepository)


    @Test
    fun `should login return user when authRepository returns successful login`() = runTest {
        // Given
        val username = "amr"
        val password = "password"
        val expectedUser = User(
            userId = UUID.randomUUID(),
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.ADMIN
        )
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } returns expectedUser

        // When
        val result = loginUseCase.login(username, password)

        // Then
        Assertions.assertEquals(expectedUser, result)
    }

    @org.junit.jupiter.api.Test
    fun `should throw exception when login with empty username`() = runTest {
        // Given
        val username = ""
        val password = "password"
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } throws UserNameOrPasswordEmptyException()

        // When/Then
        assertThrows<UserNameOrPasswordEmptyException> {
            loginUseCase.login(username, password)
        }
    }

    @org.junit.jupiter.api.Test
    fun `should throw exception when login with empty password`() = runTest {
        // Given
        val username = "amr"
        val password = ""
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } throws UserNameOrPasswordEmptyException()
        // When/Then
        assertThrows<UserNameOrPasswordEmptyException> {
            authRepository.loginUser(request)
        }
    }

    @org.junit.jupiter.api.Test
    fun `should throw exception when login with password shorter than 6 characters`() = runTest {
        // Given
        val username = "amr"
        val password = "pass"
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } throws PasswordLengthException()
        // When/Then
        assertThrows<PasswordLengthException> {
            authRepository.loginUser(request)
        }
    }
}