package data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.repository.AuthRepositoryImpl
import org.example.logic.UserAlreadyExistsException
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
        authDataSource = mockk()
        coEvery { authDataSource.getAllUsers() } returns emptyList()
        authRepository = AuthRepositoryImpl(authDataSource)
    }

    // region success
    @Test
    fun `should return user info when insertUser with valid user`() = runTest {
        // Given
        val username = "amr"
        val password = "password"
        val userRole = UserRole.ADMIN
        val expectedUser = User(
            userId = 1,
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = userRole
        )
        coEvery { authDataSource.saveAllUsers(listOf(expectedUser)) } returns Unit

        // When
        val result = authRepository.insertUser(username, password, userRole)

        // Then
        Assertions.assertEquals(expectedUser, result)
    }

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
    fun `should return list when call getAllUsers of users`() = runTest {
        // Given
        val users = listOf(
            User(userId = 1, username = "amr", password = "password", userRole = UserRole.MATE),
            User(userId = 2, username = "nasser", password = "password", userRole = UserRole.ADMIN)
        )
        coEvery { authDataSource.getAllUsers() } returns users

        // When
        val result = authRepository.getAllUsers()

        // Then
        Assertions.assertEquals(users, result)
    }

    // endregion

    // region failure
    @Test
    fun `should throw exception when insertUser with username already exists`() = runTest {
        // Given
        val username = "amr"
        val password = "password"
        val userRole = UserRole.ADMIN
        val existingUser = User(
            userId = 1,
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.ADMIN
        )
        coEvery { authDataSource.getAllUsers() } returns listOf(existingUser)

        authRepository = AuthRepositoryImpl(authDataSource)
        // When/Then
        val exception = assertThrows<UserAlreadyExistsException> {
            authRepository.insertUser(username, password, userRole)
        }
        Assertions.assertEquals("Username $username already exists", exception.message)
    }

    @Test
    fun `should throw exception when insertUser with empty username`() = runTest {
        // Given
        val username = ""
        val password = "password"
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
        val username = "amr"
        val password = "pass"
        val userRole = UserRole.ADMIN

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.insertUser(username, password, userRole)
        }
        Assertions.assertEquals("Password must be at least 6 characters", exception.message)
    }

    @Test
    fun `should throw exception when login with empty username`() = runTest {
        // Given
        val username = ""
        val password = "password"

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
        val password = "password"

        // When/Then
        val exception = assertThrows<Exception> {
            authRepository.loginUser(username, password)
        }
        Assertions.assertEquals("User not found", exception.message)
    }

    // endregion
}