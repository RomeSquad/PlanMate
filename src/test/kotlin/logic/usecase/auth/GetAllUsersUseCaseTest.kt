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
import java.util.*

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
        val users = listOf(
            User(
                userId = UUID.randomUUID(),
                username = "amr",
                userRole = UserRole.MATE
            ),
            User(
                userId = UUID.randomUUID(),
                username = "nasser",
                userRole = UserRole.ADMIN
            )
        )
        coEvery { authenticationRepository.getAllUsers() } returns users

        val result = getAllUsersUseCase.getAllUsers()

        Assertions.assertEquals(users, result)
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals("amr", result[0].username)
        Assertions.assertEquals("nasser", result[1].username)
    }

    @Test
    fun `should return empty list when repository returns no users`() = runTest {
        val users = emptyList<User>()
        coEvery { authenticationRepository.getAllUsers() } returns users

        val result = getAllUsersUseCase.getAllUsers()

        Assertions.assertEquals(users, result)
        Assertions.assertTrue(result.isEmpty())
    }

    @Test
    fun `should throw exception when repository fails to fetch users`() = runTest {
        val exception = RuntimeException("Failed to fetch users")
        coEvery { authenticationRepository.getAllUsers() } throws exception

        val thrownException = assertThrows<RuntimeException> {
            getAllUsersUseCase.getAllUsers()
        }
        Assertions.assertEquals("Failed to fetch users", thrownException.message)
    }
}