package logic.usecase.auth


import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.GetAllUsersUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetAllUsersUseCaseTest {
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase
    private lateinit var authenticationRepository: AuthRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk<AuthRepository>()
        getAllUsersUseCase = GetAllUsersUseCase(authenticationRepository)
    }

    @Test
    fun `should return list of users when repository returns users successfully`() = runTest {
        // Given
        val users = listOf(
            User(
                userId = 1,
                username = "amr",
                password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
                userRole = UserRole.MATE
            ),
            User(
                userId = 2,
                username = "nasser",
                password = "e99a18c428cb38d5f260853678922e03", // MD5 hash of "abc123"
                userRole = UserRole.ADMIN
            )
        )
        coEvery { authenticationRepository.getAllUsers() } returns users

        // When
        val result = getAllUsersUseCase.getAllUsers()

        // Then
        Assertions.assertEquals(users, result)
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("amr", result[0].username)
        Assertions.assertEquals("nasser", result[1].username)
    }

    @Test
    fun `should return empty list when repository returns no users`() = runTest {
        // Given
        val users = emptyList<User>()
        coEvery { authenticationRepository.getAllUsers() } returns users

        // When
        val result = getAllUsersUseCase.getAllUsers()

        // Then
        Assertions.assertEquals(users, result)
        Assertions.assertTrue(result.isEmpty())
    }

    @Test
    fun `should throw exception when repository fails to fetch users`() = runTest {
        // Given
        val exception = RuntimeException("Failed to fetch users")
        coEvery { authenticationRepository.getAllUsers() } throws exception

        // When/Then
        val thrownException = assertThrows<RuntimeException> {
            getAllUsersUseCase.getAllUsers()
        }
        Assertions.assertEquals("Failed to fetch users", thrownException.message)
    }
}