package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.DeleteUserUseCase
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeleteUserUseCaseTest {

    private val authenticationRepository: AuthRepository = mockk()
    private val deleteUserUseCase = DeleteUserUseCase(authenticationRepository)

    @Test
    fun `given valid username, when deleteUser is called, then return true`() = runTest {
        // Given
        val username = "testuser"
        coEvery { authenticationRepository.deleteUser(username) } returns true

        // When
        val result = deleteUserUseCase.deleteUser(username)

        // Then
        assertTrue(result)
    }

    @Test
    fun `given invalid username, when deleteUser is called, then return false`() = runTest {
        // Given
        val username = "invaliduser"
        coEvery { authenticationRepository.deleteUser(username) } returns false

        // When
        val result = deleteUserUseCase.deleteUser(username)

        // Then
        assertFalse(result)
    }
}