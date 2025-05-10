package logic.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.AuthRepository
import org.example.logic.usecase.auth.InsertUserUseCase
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class InsertUserUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private val insertUserUseCase = InsertUserUseCase(authRepository)

    @Test
    fun `should insertUser return user when authRepository returns successful insertion`() = runTest {
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
        coEvery { authRepository.insertUser(username, password, userRole) } returns expectedUser

        // When
        val result = insertUserUseCase.insertUser(username, password, userRole)

        // Then

        Assertions.assertEquals(expectedUser, result)
    }
}