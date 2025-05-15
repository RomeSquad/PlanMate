package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.User
import org.example.logic.entity.User.UserRole
import org.example.logic.exception.EmptyNameException
import org.example.logic.exception.EmptyPasswordException
import org.example.logic.exception.EntityNotChangedException
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.EditUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EditUserUseCaseTest {

    private lateinit var editUserUseCase: EditUserUseCase
    private lateinit var authenticationRepository: AuthRepository

    private val userId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk()
        editUserUseCase = EditUserUseCase(authenticationRepository)
    }

    @Test
    fun `should update user when inputs are valid and repository succeeds`() = runTest {
        // Given
        val oldUser = User(
            userId = userId,
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = userId,
            username = "amr_updated",
            password = "e99a18c428cb38d5f260853678922e03", // MD5 hash of "abc123"
            userRole = UserRole.ADMIN
        )
        coEvery { authenticationRepository.editUser(newUser) } returns Unit

        // When
        editUserUseCase.editUser(newUser, oldUser)

        // Then
        coVerify(exactly = 1) { authenticationRepository.editUser(newUser) }
    }

    @Test
    fun `should throw EntityNotChangedException when newUser equals oldUser`() = runTest {
        // Given
        val oldUser = User(
            userId = userId,
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
            userRole = UserRole.MATE
        )
        val newUser = oldUser.copy()

        // When/Then
        val exception = assertFailsWith<EntityNotChangedException> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        assertEquals("Entity not changed", exception.message)
        coVerify(exactly = 0) { authenticationRepository.editUser(any()) }
    }

    @Test
    fun `should throw EntityNotChangedException when newUser has same trimmed username and password`() = runTest {
        // Given
        val oldUser = User(
            userId = userId,
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = userId,
            username = " amr ",
            password = " 5f4dcc3b5aa765d61d8327deb882cf99 ",
            userRole = UserRole.MATE
        )

        // When/Then
        val exception = assertFailsWith<EntityNotChangedException> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        assertEquals("Entity not changed", exception.message)
        coVerify(exactly = 0) { authenticationRepository.editUser(any()) }
    }

    @Test
    fun `should throw EmptyNameException when newUser username is empty`() = runTest {
        // Given
        val oldUser = User(
            userId = userId,
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = userId,
            username = "",
            password = "e99a18c428cb38d5f260853678922e03", // MD5 hash of "abc123"
            userRole = UserRole.ADMIN
        )

        // When/Then
        val exception = assertFailsWith<EmptyNameException> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        assertEquals("Name cannot be empty", exception.message)
        coVerify(exactly = 0) { authenticationRepository.editUser(any()) }
    }

    @Test
    fun `should throw EmptyPasswordException when newUser password is blank`() = runTest {
        // Given
        val oldUser = User(
            userId = userId,
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = userId,
            username = "amr_updated",
            password = " ",
            userRole = UserRole.ADMIN
        )

        // When/Then
        val exception = assertFailsWith<EmptyPasswordException> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        assertEquals("Password cannot be empty", exception.message)
        coVerify(exactly = 0) { authenticationRepository.editUser(any()) }
    }

    @Test
    fun `should throw exception when repository fails to update user`() = runTest {
        // Given
        val oldUser = User(
            userId = userId,
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = userId,
            username = "amr_updated",
            password = "e99a18c428cb38d5f260853678922e03", // MD5 hash of "abc123"
            userRole = UserRole.ADMIN
        )
        val repositoryException = Exception("User not found")
        coEvery { authenticationRepository.editUser(newUser) } throws repositoryException

        // When/Then
        val thrownException = assertFailsWith<Exception> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        assertEquals("User not found", thrownException.message)
        coVerify(exactly = 1) { authenticationRepository.editUser(newUser) }
    }
}
