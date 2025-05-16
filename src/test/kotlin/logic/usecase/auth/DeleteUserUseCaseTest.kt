package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.DeleteUserUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteUserUseCaseTest {
    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var authenticationRepository: AuthRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk<AuthRepository>()
        deleteUserUseCase = DeleteUserUseCase(authenticationRepository)
    }

    @Test
    fun `should return true when repository deletes user successfully`() = runTest {
        val username = "amr"
        coEvery { authenticationRepository.deleteUser(username) } returns true

        val result = deleteUserUseCase.deleteUser(username)

        Assertions.assertTrue(result)
    }

    @Test
    fun `should throw exception when repository fails to delete user`() = runTest {
        val username = "amr"
        val exception = Exception("User not found")
        coEvery { authenticationRepository.deleteUser(username) } throws exception

        val thrownException = assertThrows<Exception> {
            deleteUserUseCase.deleteUser(username)
        }
        Assertions.assertEquals("User not found", thrownException.message)
    }
}