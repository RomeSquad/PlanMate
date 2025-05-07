package logic.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.LoginUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

class LoginUseCaseTest {


    private val authRepository: AuthRepository = mockk()
    private val loginUseCase = LoginUseCase(authRepository)


    @Test
    fun `should login return user when authRepository returns successful login`() = runTest {
        // Given
        val username = "amr"
        val password = "password"
        val expectedUser = User(
            userId = 1,
            username = username,
            password = "5f4dcc3b5aa765d61d8327deb882cf99",
            userRole = UserRole.ADMIN
        )
        coEvery { authRepository.loginUser(username, password) } returns expectedUser

        // When
        val result = loginUseCase.login(username, password)

        // Then
        assertEquals(expectedUser,result)
    }
}