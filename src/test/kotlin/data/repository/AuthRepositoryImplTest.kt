package data.repository

import data.datasource.authentication.dto.UserDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.datasource.mapper.toUser
import org.example.data.datasource.mapper.toUserDto
import org.example.data.repository.AuthRepositoryImpl
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.UserNameAlreadyExistsException
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.CreateUserRequest
import org.example.logic.request.EditUserRequest
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

    @Test
    fun `should return user info when insertUser with valid user`() = runTest {
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val expectedUser = request.toUserDto()
        coEvery { authDataSource.insertUser(request) } returns expectedUser

        val result = authRepository.insertUser(request)

        Assertions.assertEquals(expectedUser.userId, result.userId)
        Assertions.assertEquals(expectedUser.username, result.username)
        Assertions.assertEquals(expectedUser.userRole, result.userRole)
    }


    @Test
    fun `should throw exception when insertUser with username already exists`() = runTest {
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        coEvery { authDataSource.getAllUsers() } returns emptyList()
        coEvery { authDataSource.insertUser(request) } throws UserNameAlreadyExistsException()

        val exception = assertThrows<UserNameAlreadyExistsException> {
            authRepository.insertUser(request)
        }
        Assertions.assertEquals("Username already exists", exception.message)
    }


    @Test
    fun `should return user info when insertUser with username containing special characters`() =
        runTest {
            val username = "amr@123"
            val userRole = UserRole.ADMIN
            val request = CreateUserRequest(
                username = username,
                password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
                userRole = userRole
            )
            val expectedUser = request.toUserDto()
            coEvery { authDataSource.insertUser(request) } returns expectedUser

            val result = authRepository.insertUser(request)

            Assertions.assertEquals(expectedUser.userId, result.userId)
            Assertions.assertEquals(expectedUser.username, result.username)
            Assertions.assertEquals(expectedUser.userRole, result.userRole)
        }

    @Test
    fun `should return user info when login with valid user`() = runTest {
        val username = "amr"
        val userRole = UserRole.ADMIN

        val existingUser = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val resultUser = existingUser.toUserDto()
        coEvery { authDataSource.insertUser(existingUser) } returns resultUser
        val request = LoginRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
        )
        coEvery { authDataSource.loginUser(request) } returns resultUser

        val result = authRepository.loginUser(request)

        Assertions.assertEquals(resultUser.userId, result.userId)
    }

    @Test
    fun `should throw exception when login with incorrect password`() = runTest {
        val username = "amr"
        val userRole = UserRole.ADMIN

        val existingUser = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val resultUser = existingUser.toUserDto()
        coEvery { authDataSource.insertUser(existingUser) } returns resultUser
        val request = LoginRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf98",
        )
        coEvery { authDataSource.loginUser(request) } throws UserNotFoundException()

        assertThrows<UserNotFoundException> { authRepository.loginUser(request) }

    }


    @Test
    fun `should throw exception when loginUser with username does not exist`() = runTest {
        val username = "amr"
        val userRole = UserRole.ADMIN

        val existingUser = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val resultUser = existingUser.toUserDto()
        coEvery { authDataSource.insertUser(existingUser) } returns resultUser

        val request = LoginRequest(
            username = "mohamed",
            password = "5f4dcc3b5aa765d61d8327deb882cf98",
        )
        coEvery { authDataSource.loginUser(request) } throws UserNotFoundException()

        assertThrows<UserNotFoundException> { authRepository.loginUser(request) }

    }

    @Test
    fun `should return list when call getAllUsers of users`() = runTest {
        val users = listOf(
            UserDto(
                userId = UUID.randomUUID(),
                username = "amr",
                password = "5f4dcc3b5aa765d61d8327deb882cf99",
                userRole = UserRole.MATE
            ),
            UserDto(
                userId = UUID.randomUUID(),
                username = "nasser",
                password = "e99a18c428cb38d5f260853678922e03",
                userRole = UserRole.ADMIN
            )
        )
        coEvery { authDataSource.getAllUsers() } returns users

        val result = authRepository.getAllUsers()

        Assertions.assertEquals(users.map { it.toUser() }, result)
    }

    @Test
    fun `should return true when deleteUser with existing username`() = runTest {
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        coEvery { authDataSource.deleteUser(username) } returns true

        val result = authRepository.deleteUser(username)

        Assertions.assertTrue(result)
    }

    @Test
    fun `should throw exception when deleteUser with non-existing username`() = runTest {
        coEvery { authDataSource.deleteUser("alaa") } throws UserNotFoundException()

        assertThrows<UserNotFoundException> {
            authRepository.deleteUser("alaa")
        }
    }

    @Test
    fun `should update user when editUser with existing user`() = runTest {
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val expectedUser = request.toUserDto()
        val requestEdit = EditUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = UserRole.MATE
        )
        coEvery { authDataSource.insertUser(request) } returns expectedUser

        coVerify { authDataSource.editUser(any()) }
    }

    @Test
    fun `should return user when getUserByUserName with existing username`() = runTest {
        val username = "amr"
        val userRole = UserRole.ADMIN
        val request = CreateUserRequest(
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password123"
            userRole = userRole
        )
        val expectedUser = request.toUserDto()
        coEvery { authDataSource.getUserByUserName(username) } returns expectedUser

        val result = authRepository.getUserByUserName(username)

        Assertions.assertEquals(expectedUser.userId, result?.userId)
    }

    @Test
    fun `should return null when getUserByUserName with non-existing username`() = runTest {
        val username = "amr"

        coEvery { authDataSource.getUserByUserName(username) } returns null

        val result = authRepository.getUserByUserName(username)

        Assertions.assertNull(result)
    }
}