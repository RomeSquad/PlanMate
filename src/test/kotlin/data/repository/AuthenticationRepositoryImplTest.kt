package data.repository

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.authentication.AuthenticationDataSource
import org.example.data.repository.AuthenticationRepositoryImpl
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class AuthenticationRepositoryImplTest {

    private lateinit var authenticationRepository: AuthenticationRepositoryImpl
    private lateinit var authDataSource: AuthenticationDataSource


    @BeforeEach
    fun setup() {
        authDataSource = mockk()
        authenticationRepository = AuthenticationRepositoryImpl(authDataSource)
    }

    // region AuthenticationRepositoryImpl Success

    @Test
    fun `should return user information when register valid user`() {
        // Given
        val username = "amr"
        val password = "password"
        val userRole = UserRole.ADMIN

        every {
            authDataSource.insertUser(username, any(), userRole)
        } returns Result.success(testUser)

        // When
        val result = authenticationRepository.registerUser(username, password, userRole)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should return user information when login valid user `() {
        val username = "amr"
        val password = "password"

        every {
            authDataSource.getUserByUsername(username)
        } returns Result.success(testUser)

        val result = authenticationRepository.loginUser(username, password)

        assertTrue(result.isSuccess)

    }

    @Test
    fun `should return user id when get user by id  `() {
        val userId = 1

        every {
            authDataSource.getUserById(userId)
        } returns Result.success(testUser)

        val result = authenticationRepository.getUserById(userId)

        assertTrue(result.isSuccess)
    }

    // endregion

    // region AuthenticationRepositoryImpl Failure

    // region Failure register
    @Test
    fun `should return failure when register user with duplicate username`() {
        // Given
        val username = "amr"
        val password = "password"
        val userRole = UserRole.ADMIN
        every {
            authDataSource.insertUser(username, any(), userRole)
        } returns Result.failure(IllegalArgumentException("Username already exists"))

        // When
        val result = authenticationRepository.registerUser(username, password, userRole)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when register user with empty username`() {
        // Given
        val username = ""
        val password = "password"
        val userRole = UserRole.ADMIN
        every {
            authDataSource.insertUser(username, any(), userRole)
        } returns Result.failure(IllegalArgumentException("Username cannot be empty"))

        // When
        val result = authenticationRepository.registerUser(username, password, userRole)

        // Then
        assertTrue(result.isFailure)
    }

    // endregion

    // region Failure login
    @Test
    fun `should return failure when login with invalid password`() {
        // Given
        val username = "amr"
        val password = "wrongpassword"
        every {
            authDataSource.getUserByUsername(username)
        } returns Result.success(testUser)

        // When
        val result = authenticationRepository.loginUser(username, password)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return failure when login with empty username`() {
        // Given
        val username = ""
        val password = "password"
        every {
            authDataSource.getUserByUsername(username)
        } returns Result.failure(IllegalArgumentException("Username cannot be empty"))

        // When
        val result = authenticationRepository.loginUser(username, password)

        // Then
        assertTrue(result.isFailure)
    }

    // endregion

    // region Failure get user by id
    @Test
    fun `should return failure when get user by negative id`() {
        // Given
        val userId = -1
        every {
            authDataSource.getUserById(userId)
        } returns Result.failure(IllegalArgumentException("Invalid user ID"))

        // When
        val result = authenticationRepository.getUserById(userId)

        // Then
        assertTrue(result.isFailure)

    }
    // endregion

    // endregion





}


private val testUser = object : User {
    override val userId: Int = 1
    override val username: String = "amr"
    override val password: String = "5f4dcc3b5aa765d61d8327deb882cf99"
    override val userRole: UserRole = UserRole.MATE
}