package data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.request.CreateUserRequest
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.datasource.mapper.toUser
import org.example.data.repository.AuthRepositoryImpl
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.UserNameAlreadyExistsException
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.LoginRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class AuthRepositoryImplTest {
    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var authDataSource: AuthDataSource

    @BeforeEach
    fun setup() {
        authDataSource = mockk<AuthDataSource>(relaxed = true)
        authRepository = AuthRepositoryImpl(authDataSource)
        coEvery { authDataSource.getAllUsers() } returns emptyList()
    }

    // region insertUser
    @Test
    fun `should return user info when insertUser with valid user`() = runTest {
        // Given
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val expectedUser = request.toUser()
        coEvery { authDataSource.insertUser(request) } returns expectedUser

        // When
        val result = authRepository.insertUser(request)

        // Then
        Assertions.assertEquals(expectedUser.userId, result.userId)
        Assertions.assertEquals(expectedUser.username, result.username)
        Assertions.assertEquals(expectedUser.userRole, result.userRole)
    }


    @Test
    fun `should throw exception when insertUser with username already exists`() = runTest {
        // Given
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        coEvery { authDataSource.getAllUsers() } returns emptyList()
        coEvery { authDataSource.insertUser(request) } throws UserNameAlreadyExistsException()

        // When/Then
        val exception = assertThrows<UserNameAlreadyExistsException> {
            authRepository.insertUser(request)
        }
        Assertions.assertEquals("Username already exists", exception.message)
    }


    @Test
    fun `should return user info when insertUser with username containing special characters`() =
        runTest {
            // Given
            val username = "amr@123"
            val userRole = UserRole.ADMIN
            val request = CreateUserRequest(
                username = username,
                password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
                userRole = userRole
            )
            val expectedUser = request.toUser()
            coEvery { authDataSource.insertUser(request) } returns expectedUser

            // When
            val result = authRepository.insertUser(request)

            // Then
            Assertions.assertEquals(expectedUser.userId, result.userId)
            Assertions.assertEquals(expectedUser.username, result.username)
            Assertions.assertEquals(expectedUser.userRole, result.userRole)
        }
    // endregion

    // region loginUser
    @Test
    fun `should return user info when login with valid user`() = runTest {
        // Given
        val username = "amr"
        val userRole = UserRole.ADMIN

        val existingUser = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val resultUser = existingUser.toUser()
        coEvery { authDataSource.insertUser(existingUser) } returns resultUser
        // Given
        val request = LoginRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
        )
        coEvery { authDataSource.loginUser(request) } returns resultUser

        // When
        val result = authRepository.loginUser(request)

        // Then
        Assertions.assertEquals(resultUser, result)
    }

    @Test
    fun `should throw exception when login with incorrect password`() = runTest {
        // Given
        val username = "amr"
        val userRole = UserRole.ADMIN

        val existingUser = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val resultUser = existingUser.toUser()
        coEvery { authDataSource.insertUser(existingUser) } returns resultUser
        // Given
        val request = LoginRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf98",
        )
        coEvery { authDataSource.loginUser(request) } throws UserNotFoundException()

        // When
        assertThrows<UserNotFoundException> { authRepository.loginUser(request) }

        // Then
    }


    @Test
    fun `should throw exception when loginUser with username does not exist`() = runTest {
// Given
        val username = "amr"
        val userRole = UserRole.ADMIN

        val existingUser = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val resultUser = existingUser.toUser()
        coEvery { authDataSource.insertUser(existingUser) } returns resultUser
        // Given
        val request = LoginRequest(
            username = "mohamed",
            password = "5f4dcc3b5aa765d61d8327deb882cf98",
        )
        coEvery { authDataSource.loginUser(request) } throws UserNotFoundException()

        // When
        assertThrows<UserNotFoundException> { authRepository.loginUser(request) }

    }
    // endregion

    // region getAllUsers
    @Test
    fun `should return list when call getAllUsers of users`() = runTest {
        // Given
        val users = listOf(
            User(
                userId = UUID.randomUUID(),
                username = "amr",
                password = "5f4dcc3b5aa765d61d8327deb882cf99",
                userRole = UserRole.MATE
            ),
            User(
                userId = UUID.randomUUID(),
                username = "nasser",
                password = "e99a18c428cb38d5f260853678922e03",
                userRole = UserRole.ADMIN
            )
        )
        coEvery { authDataSource.getAllUsers() } returns users

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
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val expectedUser = request.toUser()
        coEvery { authDataSource.deleteUser(username) } returns true

        // When
        val result = authRepository.deleteUser(username)

        // Then
        Assertions.assertTrue(result)
    }

    @Test
    fun `should throw exception when deleteUser with non-existing username`() = runTest {
        // Given

        coEvery { authDataSource.deleteUser("alaa") } throws UserNotFoundException()

        // When/Then
        assertThrows<UserNotFoundException> {
            authRepository.deleteUser("alaa")
        }
    }
    // endregion

    // region editUser
    @Test
    fun `should update user when editUser with existing user`() = runTest {
        // Given
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val expectedUser = request.toUser()
        coEvery { authDataSource.insertUser(request) } returns expectedUser

        // When
        val result = authRepository.editUser(expectedUser.copy(userRole = UserRole.MATE))

        // Then
        coVerify { authDataSource.editUser(any()) }
    }


    // region getUserByUserName
    @Test
    fun `should return user when getUserByUserName with existing username`() = runTest {
        // Given
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val expectedUser = request.toUser()
        coEvery { authDataSource.getUserByUserName(username) } returns expectedUser

        // When
        val result = authRepository.getUserByUserName(username)

        // Then
        Assertions.assertEquals(expectedUser, result)
    }

    @Test
    fun `should return null when getUserByUserName with non-existing username`() = runTest {
        // Given
        val username = "amr"

        coEvery { authDataSource.getUserByUserName(username) } returns null

        // When
        val result = authRepository.getUserByUserName(username)

        // Then
        Assertions.assertNull(result)
    }
    // endregion


}