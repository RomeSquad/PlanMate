package data.repository

import data.datasource.authentication.dto.UserDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.data.datasource.authentication.AuthDataSource
import org.example.data.datasource.mapper.toUser
import org.example.data.repository.AuthRepositoryImpl
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.UserNameAlreadyExistsException
import org.example.logic.exception.UserNotFoundException
import org.example.logic.request.CreateUserRequest
import org.example.logic.request.EditUserRequest
import org.example.logic.request.LoginRequest
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.*

class AuthRepositoryImplTest {
    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var authDataSource: AuthDataSource
    private val userId = UUID.randomUUID()
    private val username = "amr"
    private val passwordHash = "5f4dcc3b5aa765d61d8327deb882cf99" // MD5 hash of "password123"
    private val userRole = UserRole.ADMIN

    private val createUserRequest = CreateUserRequest(
        username = username,
        password = passwordHash,
        userRole = userRole
    )

    private val userDto = UserDto(
        userId = userId,
        username = username,
        password = passwordHash,
        userRole = userRole
    )

    private val user = userDto.toUser()

    @BeforeTest
    fun setup() {
        authDataSource = mockk()
        authRepository = AuthRepositoryImpl(authDataSource)
    }

    @Test
    fun `insertUser with valid request returns user info`() = runTest {
        coEvery { authDataSource.insertUser(createUserRequest) } returns userDto

        val result = authRepository.insertUser(createUserRequest)

        assertEquals(user.userId, result.userId)
        assertEquals(user.username, result.username)
        assertEquals(user.userRole, result.userRole)
        coVerify { authDataSource.insertUser(createUserRequest) }
    }

    @Test
    fun `insertUser with existing username throws UserNameAlreadyExistsException`() = runTest {
        coEvery { authDataSource.insertUser(createUserRequest) } throws UserNameAlreadyExistsException()

        val exception = assertThrows<UserNameAlreadyExistsException> {
            authRepository.insertUser(createUserRequest)
        }
        assertEquals("Username already exists", exception.message)
        coVerify { authDataSource.insertUser(createUserRequest) }
    }

    @Test
    fun `insertUser with username containing special characters returns user info`() = runTest {
        val specialUsername = "amr@123"
        val request = createUserRequest.copy(username = specialUsername)
        val expectedUserDto = userDto.copy(username = specialUsername)
        coEvery { authDataSource.insertUser(request) } returns expectedUserDto

        val result = authRepository.insertUser(request)

        assertEquals(expectedUserDto.userId, result.userId)
        assertEquals(expectedUserDto.username, result.username)
        assertEquals(expectedUserDto.userRole, result.userRole)
        coVerify { authDataSource.insertUser(request) }
    }

    @Test
    fun `loginUser with valid credentials returns user info`() = runTest {
        val loginRequest = LoginRequest(username = username, password = passwordHash)
        coEvery { authDataSource.loginUser(loginRequest) } returns userDto

        val result = authRepository.loginUser(loginRequest)

        assertEquals(user.userId, result.userId)
        assertEquals(user.username, result.username)
        assertEquals(user.userRole, result.userRole)
        coVerify { authDataSource.loginUser(loginRequest) }
    }

    @Test
    fun `loginUser with incorrect password throws UserNotFoundException`() = runTest {
        val loginRequest = LoginRequest(username = username, password = "wrongHash")
        coEvery { authDataSource.loginUser(loginRequest) } throws UserNotFoundException()

        assertThrows<UserNotFoundException> {
            authRepository.loginUser(loginRequest)
        }
        coVerify { authDataSource.loginUser(loginRequest) }
    }

    @Test
    fun `loginUser with non-existent username throws UserNotFoundException`() = runTest {
        val loginRequest = LoginRequest(username = "nonexistent", password = passwordHash)
        coEvery { authDataSource.loginUser(loginRequest) } throws UserNotFoundException()

        assertThrows<UserNotFoundException> {
            authRepository.loginUser(loginRequest)
        }
        coVerify { authDataSource.loginUser(loginRequest) }
    }

    @Test
    fun `getAllUsers returns list of users`() = runTest {
        val users = listOf(
            userDto,
            userDto.copy(userId = UUID.randomUUID(), username = "nasser", userRole = UserRole.MATE)
        )
        coEvery { authDataSource.getAllUsers() } returns users

        val result = authRepository.getAllUsers()

        assertEquals(users.map { it.toUser() }, result)
        coVerify { authDataSource.getAllUsers() }
    }

    @Test
    fun `deleteUser with existing username returns true`() = runTest {
        coEvery { authDataSource.deleteUser(username) } returns true

        val result = authRepository.deleteUser(username)

        assertTrue(result)
        coVerify { authDataSource.deleteUser(username) }
    }

    @Test
    fun `deleteUser with non-existing username throws UserNotFoundException`() = runTest {
        val nonExistentUsername = "nonexistent"
        coEvery { authDataSource.deleteUser(nonExistentUsername) } throws UserNotFoundException()

        assertThrows<UserNotFoundException> {
            authRepository.deleteUser(nonExistentUsername)
        }
        coVerify { authDataSource.deleteUser(nonExistentUsername) }
    }

    @Test
    fun `editUser with existing user updates user info`() = runTest {
        val editRequest = EditUserRequest(
            username = username,
            password = "newHash",
            userRole = UserRole.MATE
        )
        coEvery { authDataSource.editUser(editRequest) } returns Unit

        authRepository.editUser(editRequest)

        coVerify { authDataSource.editUser(editRequest) }
    }

    @Test
    fun `getUserByUserName with existing username returns user`() = runTest {
        coEvery { authDataSource.getUserByUserName(username) } returns userDto

        val result = authRepository.getUserByUserName(username)

        assertEquals(user.userId, result?.userId)
        assertEquals(user.username, result?.username)
        assertEquals(user.userRole, result?.userRole)
        coVerify { authDataSource.getUserByUserName(username) }
    }

    @Test
    fun `getUserByUserName with non-existing username returns null`() = runTest {
        val nonExistentUsername = "nonexistent"
        coEvery { authDataSource.getUserByUserName(nonExistentUsername) } returns null

        val result = authRepository.getUserByUserName(nonExistentUsername)

        assertNull(result)
        coVerify { authDataSource.getUserByUserName(nonExistentUsername) }
    }

    @Test
    fun `getUserById with existing ID returns user`() = runTest {
        coEvery { authDataSource.getUserById(userId) } returns userDto

        val result = authRepository.getUserById(userId)

        assertEquals(user.userId, result?.userId)
        assertEquals(user.username, result?.username)
        assertEquals(user.userRole, result?.userRole)
        coVerify { authDataSource.getUserById(userId) }
    }

    @Test
    fun `getUserById with non-existing ID returns null`() = runTest {
        val nonExistentId = UUID.randomUUID()
        coEvery { authDataSource.getUserById(nonExistentId) } returns null

        val result = authRepository.getUserById(nonExistentId)

        assertNull(result)
        coVerify { authDataSource.getUserById(nonExistentId) }
    }

    @Test
    fun `getCurrentUser when user exists returns user`() = runTest {
        coEvery { authDataSource.getCurrentUser() } returns userDto

        val result = authRepository.getCurrentUser()

        assertEquals(user.userId, result?.userId)
        assertEquals(user.username, result?.username)
        assertEquals(user.userRole, result?.userRole)
        coVerify { authDataSource.getCurrentUser() }
    }

    @Test
    fun `getCurrentUser when no user is logged in returns null`() = runTest {
        coEvery { authDataSource.getCurrentUser() } returns null

        val result = authRepository.getCurrentUser()

        assertNull(result)
        coVerify { authDataSource.getCurrentUser() }
    }
}