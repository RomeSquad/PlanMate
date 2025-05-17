package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.InvalidCredentialsException
import org.example.logic.exception.PasswordLengthException
import org.example.logic.exception.UserNameOrPasswordEmptyException
import org.example.logic.repository.AuthRepository
import org.example.logic.request.LoginRequest
import org.example.logic.usecase.auth.LoginUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test

class LoginUseCaseTest {
    private val authRepository: AuthRepository = mockk()
    private val loginUseCase = LoginUseCase(authRepository)

    @Test
    fun `should login return user when authRepository returns successful login`() = runTest {
        val username = "amr"
        val password = "password"
        val expectedUser = User(
            userId = UUID.randomUUID(),
            username = username,
            userRole = UserRole.ADMIN
        )
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } returns expectedUser

        val result = loginUseCase.login(username, password)

        Assertions.assertEquals(expectedUser, result)
    }

    @Test
    fun `should throw exception when login with empty username`() = runTest {
        val username = ""
        val password = "password"
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } throws UserNameOrPasswordEmptyException()

        assertThrows<UserNameOrPasswordEmptyException> {
            loginUseCase.login(username, password)
        }
    }

    @Test
    fun `should throw exception when login with empty password`() = runTest {
        val username = "amr"
        val password = ""
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } throws UserNameOrPasswordEmptyException()

        assertThrows<UserNameOrPasswordEmptyException> {
            authRepository.loginUser(request)
        }
    }

    @Test
    fun `should throw exception when login with password shorter than 6 characters`() = runTest {
        val username = "amr"
        val password = "pass"
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } throws PasswordLengthException()

        assertThrows<PasswordLengthException> {
            authRepository.loginUser(request)
        }
    }
    @Test
    fun `should throw InvalidCredentialsException when repository throws generic exception`() = runTest {
        val username = "amr"
        val password = "password"
        val request = LoginRequest(
            username = username,
            password = password
        )
        coEvery { authRepository.loginUser(request) } throws Exception("generic")

        assertThrows<InvalidCredentialsException> {
            loginUseCase.login(username, password)
        }
    }

}