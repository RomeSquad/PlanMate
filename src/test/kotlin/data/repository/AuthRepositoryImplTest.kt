package data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.repository.AuthRepositoryImpl
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class AuthRepositoryImplTest {
    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var authDataSource: AuthDataSource

    @BeforeEach
    fun setup() {
        authDataSource = mockk<AuthDataSource>()
        coEvery { authDataSource.getAllUsers() } returns emptyList()
        authRepository = AuthRepositoryImpl(authDataSource)
    }

    // region insertUser
    @Test
    fun `should return user info when insertUser with valid user`() = runTest {
        // Given
        val username = "amr"
        val password = "password123"
        val userRole = UserRole.ADMIN
        val expectedUser = User(
            userId = 1,
            username = username,
            password = "hashed_password_123", // Mocked hashed password
            userRole = userRole
        )
        coEvery { authDataSource.saveAllUsers(any()) } returns Unit

        // When
        val result = authRepository.insertUser(username, password, userRole)

        // Then
        Assertions.assertEquals(expectedUser.userId, result.userId)
        Assertions.assertEquals(expectedUser.username, result.username)
        Assertions.assertEquals(expectedUser.userRole, result.userRole)
    }

    @Test
    fun `should throw exception when insertUser with username already exists`() = runTest {
        // Given
        val username = "amr"
        val password = "password123"
        val userRole = UserRole.ADMIN
        val existingUser = User(
            userId = 1,
            username = username,
            password = "hashed_password_123",
            userRole = UserRole.ADMIN
        )
        coEvery { authDataSource.getAllUsers() } returns listOf(existingUser)
        authRepository = AuthRepositoryImpl(authDataSource)

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.insertUser(username, password, userRole)
        }
        Assertions.assertEquals("Username already exists", exception.message)
    }

    @Test
    fun `should throw exception when insertUser with empty username`() = runTest {
        // Given
        val username = ""
        val password = "password123"
        val userRole = UserRole.ADMIN

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.insertUser(username, password, userRole)
        }
        Assertions.assertEquals("Username or password cannot be empty", exception.message)
    }

    @Test
    fun `should throw exception when insertUser with empty password`() = runTest {
        // Given
        val username = "amr"
        val password = ""
        val userRole = UserRole.ADMIN

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.insertUser(username, password, userRole)
        }
        Assertions.assertEquals("Username or password cannot be empty", exception.message)
    }

    @Test
    fun `should throw exception when insertUser with password shorter than 6 characters`() = runTest {
        // Given
        val username = "problèmes de connexion"
        val password = "pass"
        val userRole = UserRole.ADMIN

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.insertUser(username, password, userRole)
        }
        Assertions.assertEquals("Password must be at least 6 characters", exception.message)
    }
    // endregion

    // region loginUser
    @Test
    fun `should return user info when login with valid user`() = runTest {
        // Given
        val username = "amr"
        val password = "password"
        val expectedUser = User(
            userId = 1,
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.MATE
        )
        coEvery { authDataSource.getAllUsers() } returns listOf(expectedUser)
        authRepository = AuthRepositoryImpl(authDataSource)

        // When
        val result = authRepository.loginUser(username, password)

        // Then
        Assertions.assertEquals(expectedUser, result)
    }

    @Test
    fun `should throw exception when login with empty username`() = runTest {
        // Given
        val username = ""
        val password = "password123"

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.loginUser(username, password)
        }
        Assertions.assertEquals("Username or password cannot be empty", exception.message)
    }

    @Test
    fun `should throw exception when login with empty password`() = runTest {
        // Given
        val username = "amr"
        val password = ""

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.loginUser(username, password)
        }
        Assertions.assertEquals("Username or password cannot be empty", exception.message)
    }

    @Test
    fun `should throw exception when login with password shorter than 6 characters`() = runTest {
        // Given
        val username = "amr"
        val password = "pass"

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.loginUser(username, password)
        }
        Assertions.assertEquals("Password must be at least 6 characters", exception.message)
    }

    @Test
    fun `should throw exception when loginUser with username does not exist`() = runTest {
        // Given
        val username = "amr"
        val password = "password123"
        coEvery { authDataSource.getAllUsers() } returns emptyList()
        authRepository = AuthRepositoryImpl(authDataSource)

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.loginUser(username, password)
        }
        Assertions.assertEquals("User not found", exception.message)
    }
    // endregion

    // region getAllUsers
    @Test
    fun `should return list when call getAllUsers of users`() = runTest {
        // Given
        val users = listOf(
            User(userId = 1, username = "amr", password = "hashed_password_123", userRole = UserRole.MATE),
            User(userId = 2, username = "nasser", password = "hashed_password_456", userRole = UserRole.ADMIN)
        )
        coEvery { authDataSource.getAllUsers() } returns users
        authRepository = AuthRepositoryImpl(authDataSource)

        // When
        val result = authRepository.getAllUsers()

        // Then
        Assertions.assertEquals(users, result)
    }
    // endregion

    // region deleteUser
    @Test
    fun `should return true when deleteUser with existing username`() = runTest {
        // Given
        val username = "amr"
        val user = User(
            userId = 1,
            username = username,
            password = "hashed_password_123",
            userRole = UserRole.MATE
        )
        coEvery { authDataSource.getAllUsers() } returns listOf(user)
        coEvery { authDataSource.saveAllUsers(any()) } returns Unit
        authRepository = AuthRepositoryImpl(authDataSource)

        // When
        val result = authRepository.deleteUser(username)

        // Then
        Assertions.assertTrue(result)
    }

    @Test
    fun `should throw exception when deleteUser with non-existing username`() = runTest {
        // Given
        val username = "amr"
        coEvery { authDataSource.getAllUsers() } returns emptyList()
        authRepository = AuthRepositoryImpl(authDataSource)

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.deleteUser(username)
        }
        Assertions.assertEquals("User not found", exception.message)
    }
    // endregion

    // region editUser
    @Test
    fun `should update user when editUser with existing user`() = runTest {
        // Given
        val username = "amr"
        val existingUser = User(
            userId = 1,
            username = username,
            password = "hashed_password_123",
            userRole = UserRole.MATE
        )
        val updatedUser = User(
            userId = 1,
            username = username,
            password = "hashed_password_456",
            userRole = UserRole.ADMIN
        )
        coEvery { authDataSource.getAllUsers() } returns listOf(existingUser)
        coEvery { authDataSource.saveAllUsers(any()) } returns Unit
        authRepository = AuthRepositoryImpl(authDataSource)

        // When
        authRepository.editUser(updatedUser)

        // Then
        val result = authRepository.getUserByUserName(username)
        Assertions.assertEquals(updatedUser, result)
    }

    @Test
    fun `should throw exception when editUser with non-existing user`() = runTest {
        // Given
        val user = User(
            userId = 1,
            username = "amr",
            password = "hashed_password_123",
            userRole = UserRole.MATE
        )
        coEvery { authDataSource.getAllUsers() } returns emptyList()
        authRepository = AuthRepositoryImpl(authDataSource)

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.editUser(user)
        }
        Assertions.assertEquals("User not found", exception.message)
    }
    // endregion

    // region getUserByUserName
    @Test
    fun `should return user when getUserByUserName with existing username`() = runTest {
        // Given
        val username = "amr"
        val expectedUser = User(
            userId = 1,
            username = username,
            password = "hashed_password_123",
            userRole = UserRole.MATE
        )
        coEvery { authDataSource.getAllUsers() } returns listOf(expectedUser)
        authRepository = AuthRepositoryImpl(authDataSource)

        // When
        val result = authRepository.getUserByUserName(username)

        // Then
        Assertions.assertEquals(expectedUser, result)
    }

    @Test
    fun `should return null when getUserByUserName with non-existing username`() = runTest {
        // Given
        val username = "amr"
        coEvery { authDataSource.getAllUsers() } returns emptyList()
        authRepository = AuthRepositoryImpl(authDataSource)

        // When
        val result = authRepository.getUserByUserName(username)

        // Then
        Assertions.assertNull(result)
    }
    // endregion
}