package logic.usecase.state

import io.mockk.*
import org.example.logic.entity.ProjectState
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.AddCustomProjectStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFailsWith

class AddCustomProjectProjectStateUseCaseTest {
    private lateinit var projectStateRepository: ProjectStateRepository
    private lateinit var addCustomProjectStateUseCase: AddCustomProjectStateUseCase

    @BeforeEach
    fun setup() {
        projectStateRepository = mockk(relaxed = true)
        addCustomProjectStateUseCase = AddCustomProjectStateUseCase(projectStateRepository)
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
        val stateName = "pending"
        val expectedState = ProjectState(projectId = projectId, stateName = stateName)

        every { projectStateRepository.addProjectState(expectedState) } just Runs

        addCustomProjectStateUseCase.execute(currentUser, stateName, projectId)

        verify(exactly = 1) { projectStateRepository.addProjectState(expectedState) }
    }

    @Test
    fun `should throw an exception when user role is mate`() {
        val currentUser = User(userId = 1, username = "Zinah", password = "1234", userRole = UserRole.MATE)
        val stateName = "pending"
        val projectId = 1
        assertFailsWith<IllegalAccessException> {
            addCustomProjectStateUseCase.execute(currentUser, stateName, projectId)
        }
    }

    @Test
    fun ` should throw an exception when state name is blank`() {
        val currentUser = User(userId = 1, username = "Zinah", password = "1234", userRole = UserRole.ADMIN)
        assertThrows<IllegalArgumentException> {
            addCustomProjectStateUseCase.execute(currentUser, "", 1)
        }
    }

}
