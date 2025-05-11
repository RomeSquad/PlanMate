package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.EmptyNameException
import org.example.logic.exception.EmptyPasswordException
import org.example.logic.exception.EntityNotChangedException
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.EditUserUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class EditUserUseCaseTest {
    private lateinit var editUserUseCase: EditUserUseCase
    private lateinit var authenticationRepository: AuthRepository

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk<AuthRepository>()
        editUserUseCase = EditUserUseCase(authenticationRepository)
    }

    @Test
    fun `should update user when inputs are valid and repository succeeds`() = runTest {
        // Given
        val oldUser = User(
            userId = UUID.randomUUID(),
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = UUID.randomUUID(),
            username = "amr_updated",
            password = "e99a18c428cb38d5f260853678922e03", // MD5 hash of "abc123"
            userRole = UserRole.ADMIN
        )
        coEvery { authenticationRepository.editUser(newUser) } returns Unit

        // When
        editUserUseCase.editUser(newUser, oldUser)

        // Then
        coVerify { authenticationRepository.editUser(newUser) }
    }

    @Test
    fun `should throw EntityNotChangedException when newUser equals oldUser`() = runTest {
        // Given
        val oldUser = User(
            userId = UUID.randomUUID(),
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.MATE
        )
        val newUser = oldUser // Identical to oldUser

        // When/Then
        val exception = assertThrows<EntityNotChangedException> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        Assertions.assertEquals("Entity not changed", exception.message)
    }

    @Test
    fun `should throw EmptyNameException when newUser username is empty`() = runTest {
        // Given
        val oldUser = User(
            userId = UUID.randomUUID(),
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = UUID.randomUUID(),
            username = "",
            password = "e99a18c428cb38d5f260853678922e03",
            userRole = UserRole.ADMIN
        )

        // When/Then
        val exception = assertThrows<EmptyNameException> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        Assertions.assertEquals("Name cannot be empty", exception.message)
    }

    @Test
    fun `should throw EmptyPasswordException when newUser password is blank`() = runTest {
        // Given
        val oldUser = User(
            userId = UUID.randomUUID(),
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = UUID.randomUUID(),
            username = "amr_updated",
            password = " ", // Blank password
            userRole = UserRole.ADMIN
        )

        // When/Then
        val exception = assertThrows<EmptyPasswordException> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        Assertions.assertEquals("Password cannot be empty", exception.message)
    }

    @Test
    fun `should throw exception when repository fails to update user`() = runTest {
        // Given
        val oldUser = User(
            userId = UUID.randomUUID(),
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.MATE
        )
        val newUser = User(
            userId = UUID.randomUUID(),
            username = "amr_updated",
            password = "e99a18c428cb38d5f260853678922e03",
            userRole = UserRole.ADMIN
        )
        val exception = Exception("User not found")
        coEvery { authenticationRepository.editUser(newUser) } throws exception

        // When/Then
        val thrownException = assertThrows<Exception> {
            editUserUseCase.editUser(newUser, oldUser)
        }
        Assertions.assertEquals("User not found", thrownException.message)
    }
}