package logic.usecase.state

import io.mockk.*
import org.example.logic.entity.State
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.StateRepository
import org.example.logic.usecase.state.AddCustomProjectStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

class AddCustomProjectStateUseCaseTest {
    private lateinit var stateRepository: StateRepository
    private lateinit var addCustomStateUseCase: AddCustomProjectStateUseCase

    @BeforeEach
    fun setup() {
        stateRepository = mockk(relaxed = true)
        addCustomStateUseCase = AddCustomProjectStateUseCase(stateRepository)
    }

    @Test
    fun `should add custom state if user is admin `() {
        val currentUser = User(
            userId = 1,
            username = "Zinah",
            password = "1234",
            userRole = UserRole.ADMIN
        )
        val projectId = 4
        val stateName = "to do"
        val expectedState = State(projectId = projectId, stateName = stateName)

        every { stateRepository.addState(expectedState) } just Runs

        addCustomStateUseCase.execute(currentUser, stateName, projectId)

        verify(exactly = 1) { stateRepository.addState(expectedState) }
    }

    @Test
    fun `should throw an exception when user role is mate`() {
        val currentUser = User(userId = 1, username = "Zinah", password = "1234", userRole = UserRole.MATE)
        val stateName = "todo"
        val projectId = 1
        assertFailsWith<IllegalAccessException> {
            addCustomStateUseCase.execute(currentUser, stateName, projectId)
        }
    }

    @Test
    fun ` should throw an exception when state name is blank`() {
        val currentUser = User(userId = 1, username = "Zinah", password = "1234", userRole = UserRole.ADMIN)
        assertThrows<IllegalArgumentException> {
            addCustomStateUseCase.execute(currentUser, "", 1)
        }
    }

}
