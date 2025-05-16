package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import logic.usecase.validator.UserCredentialsValidator
import org.example.logic.entity.auth.UserRole
import org.example.logic.exception.PasswordLengthException
import org.example.logic.repository.AuthRepository
import org.example.logic.request.EditUserRequest
import org.example.logic.usecase.auth.EditUserUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EditUserUseCaseTest {
    private lateinit var editUserUseCase: EditUserUseCase
    private lateinit var authenticationRepository: AuthRepository
    private lateinit var validationUser: UserCredentialsValidator

    @BeforeEach
    fun setup() {
        authenticationRepository = mockk()
        validationUser = mockk(relaxed = true)
        editUserUseCase = EditUserUseCase(authenticationRepository, validationUser)
    }

    @Test
    fun `should update user when inputs are valid and repository succeeds`() = runTest {
        val request = EditUserRequest(
            username = "amr",
            password = "5f4dcc3b5aa765d61d8327deb882cf99", // MD5 hash of "password"
            userRole = UserRole.MATE
        )
        coEvery { authenticationRepository.editUser(request) } returns Unit

        editUserUseCase.editUser(request)

        coVerify(exactly = 1) { authenticationRepository.editUser(request) }
    }




    @Test
    fun `should throw EmptyPasswordException when newUser password is blank`() = runTest {
        val request = EditUserRequest(
            username = "amr_updated",
            password = " ",
            userRole = UserRole.ADMIN
        )
        every { validationUser.validatePasswordStrength(request.password) } throws PasswordLengthException()

        assertThrows<PasswordLengthException> {
            editUserUseCase.editUser(request)
        }
    }

    @Test
    fun `should throw exception when repository fails to update user`() = runTest {
        val request = EditUserRequest(
            username = "amr_updated",
            password = "e99a18c428cb38d5f260853678922e03", // MD5 hash of "abc123"
            userRole = UserRole.ADMIN
        )
        val repositoryException = Exception("User not found")
        coEvery { authenticationRepository.editUser(request) } throws repositoryException

        val thrownException = assertFailsWith<Exception> {
            editUserUseCase.editUser(request)
        }
        assertEquals("User not found", thrownException.message)
        coVerify(exactly = 1) { authenticationRepository.editUser(request) }
    }
}
