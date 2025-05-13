package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.request.auth.CreateUserRequest
import logic.usecase.validator.UserCredentialsValidator
import org.example.data.datasource.mapper.toUser
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.PasswordLengthException
import org.example.logic.exception.UserNameOrPasswordEmptyException
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.InsertUserUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class InsertUserUseCaseTest {

    private val authRepository: AuthRepository = mockk(relaxed = true)
    private val userValidator: UserCredentialsValidator = mockk(relaxed = true)
    private val insertUserUseCase = InsertUserUseCase(authRepository, userValidator)

    @Test
    fun `should insertUser return user when authRepository returns successful insertion`() =
        runTest {
            // Given
            val username = "amr"
            val password = "password123"
            val userRole = UserRole.ADMIN
            val request = CreateUserRequest(
                username = username,
                password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
                userRole = userRole
            )
            val expectedUser = request.toUser()
            coEvery { authRepository.insertUser(any()) } returns expectedUser

            // When
            val result = insertUserUseCase.insertUser(username, password, userRole)

            // Then

            assertEquals(expectedUser, result)
        }

    @org.junit.jupiter.api.Test
    fun `should throw exception when insertUser with empty username`() = runTest {
        // Given
        val username = ""
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val expectedUser = request.toUser()
        coEvery { authRepository.insertUser(request) } throws UserNameOrPasswordEmptyException()

        // When/Then
        assertThrows<UserNameOrPasswordEmptyException> {
            authRepository.insertUser(request)
        }
    }

    @org.junit.jupiter.api.Test
    fun `should throw exception when insertUser with password of 5 characters`() = runTest {
        // Given
        val username = "amr"
        val password = "password123"
        val userRole = UserRole.ADMIN
        coEvery { authRepository.insertUser(any()) } throws PasswordLengthException()

        // When/Then
        assertThrows<PasswordLengthException> {
            insertUserUseCase.insertUser(username, password, userRole)
        }
    }

    @org.junit.jupiter.api.Test
    fun `should throw exception when insertUser with password shorter than 6 characters`() =
        runTest {
            // Given
            val username = "amr"
            val password = "password123"
            val userRole = UserRole.ADMIN

            coEvery { authRepository.insertUser(any()) } throws PasswordLengthException()

            // When/Then
            assertThrows<PasswordLengthException> {
                insertUserUseCase.insertUser(username, password, userRole)
            }
        }

    @org.junit.jupiter.api.Test
    fun `should throw exception when insertUser with empty password`() = runTest {
        // Given
        val username = "amr"
        val password = ""
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "", // MD5 hash of "password123"
            userRole = userRole
        )
        coEvery { authRepository.insertUser(request) } throws UserNameOrPasswordEmptyException()

        // When/Then
        val exception = assertThrows<Exception> {
            insertUserUseCase.insertUser(username, password, userRole)
        }
        Assertions.assertEquals("Username or password cannot be empty", exception.message)
    }
}