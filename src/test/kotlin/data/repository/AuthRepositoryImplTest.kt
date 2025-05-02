package data.repository

import io.mockk.every
import io.mockk.mockk
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.repository.AuthRepositoryImpl
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthRepositoryImplTest {
    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var authDataSource: AuthDataSource

    @BeforeEach
    fun setup() {
        authDataSource = mockk()
        every { authDataSource.getAllUsers() } returns Result.success(emptyList())
        authRepository = AuthRepositoryImpl(authDataSource)
    }

    // region success
    @Test
    fun `should return user info when insertUser with valid user`() {

        // Given
        val username = "amr"
        val password = "password"
        val userRole = UserRole.ADMIN
        val expectedUser =
            User(userId = 1, username = username, password = "5f4dcc3b5aa765d61d8327deb882cf99", userRole = userRole)
        every { authDataSource.saveAllUsers(any()) } returns Result.success(Unit)

        // When
        val result = authRepository.insertUser(username, password, userRole)

        // Then
        Assertions.assertEquals(expectedUser, result.getOrThrow())

    }

    @Test
    fun `should return user info when login with valid user`() {

        // Given
        val username = "amr"
        val password = "password"

        val expectedUser = User(userId = 1, username = username, password = "password", userRole = UserRole.MATE)

        every { authDataSource.getAllUsers() } returns Result.success(listOf(expectedUser))

        authRepository = AuthRepositoryImpl(authDataSource)

        // When
        val result = authRepository.loginUser(username, password)

        // Then
        Assertions.assertEquals(expectedUser, result.getOrThrow())

    }

    @Test
    fun `should return list when call getAllUsers of users`() {
        // Arrange
        val users = listOf(
            User(userId = 1, username = "amr", password = "password", userRole = UserRole.MATE),
            User(userId = 2, username = "nasser", password = "password", userRole = UserRole.ADMIN)
        )
        every { authDataSource.getAllUsers() } returns Result.success(users)

        authRepository = AuthRepositoryImpl(authDataSource)

        // When
        val result = authRepository.getAllUsers()

        // Then
        Assertions.assertEquals(users, result.getOrThrow())
    }

    // endregion

    // region failure

    @Test
    fun `should insertUser return fail when username already exists`() {
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
        every { authDataSource.getAllUsers() } returns Result.success(listOf(existingUser))


        // When
        authRepository = AuthRepositoryImpl(authDataSource)
        val result = authRepository.insertUser(username, password, userRole)

        // Then
        Assertions.assertTrue(result.isFailure)
    }


    @Test
    fun `should insertUser return fail when insert empty username`() {

        // Given
        val username = ""
        val password = "password"
        val userRole = UserRole.ADMIN

        // When
        val result = authRepository.insertUser(username, password, userRole)

        // Then
        Assertions.assertTrue(result.isFailure)


    }

    @Test
    fun `should insertUser return fail when insert empty password`() {

        // Given
        val username = "amr"
        val password = ""
        val userRole = UserRole.ADMIN

        // When
        val result = authRepository.insertUser(username, password, userRole)

        // Then
        Assertions.assertTrue(result.isFailure)


    }

    @Test
    fun `should insertUser return fail when password is shorter than 6 characters`() {
        // Given
        val username = "amr"
        val password = "pass"
        val userRole = UserRole.ADMIN

        // When
        val result = authRepository.insertUser(username, password, userRole)

        // Then
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    fun `should login return fail when insert empty username`() {

        // Given
        val username = ""
        val password = "password"


        // When
        val result = authRepository.loginUser(username, password)

        // Then
        Assertions.assertTrue(result.isFailure)


    }

    @Test
    fun `should login return fail when insert empty password`() {

        // Given
        val username = "amr"
        val password = ""

        // When
        val result = authRepository.loginUser(username, password)

        // Then
        Assertions.assertTrue(result.isFailure)


    }

    @Test
    fun `should login return fail when password is shorter than 6 characters`() {
        // Given
        val username = "amr"
        val password = "pass"


        // When
        val result = authRepository.loginUser(username, password)

        // Then
        Assertions.assertTrue(result.isFailure)
    }


    @Test
    fun `should loginUser  return user not found when username does not exist`() {
        // Given
        val username = "amr"
        val password = "password"

        // When
        val result = authRepository.loginUser(username, password)

        // Then
        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals("User not found", result.exceptionOrNull()?.message)
    }


    // endregion


}