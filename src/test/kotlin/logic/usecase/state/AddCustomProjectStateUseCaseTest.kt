package logic.usecase.state

import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.ProjectState
import org.example.logic.entity.auth.User
import org.example.logic.entity.auth.UserRole
import org.example.logic.repository.ProjectStateRepository
import org.example.logic.usecase.state.AddCustomProjectStateUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
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
    fun `should add custom state if user is admin `() = runTest {
        val currentUser = User(
            userId = UUID.randomUUID(),
            username = "Zinah",
            userRole = UserRole.ADMIN
        )
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        val stateName = "pending"
        val expectedState = ProjectState(projectId = projectId, stateName = stateName)

        coEvery { projectStateRepository.addProjectState(expectedState) } just Runs

        addCustomProjectStateUseCase.execute(currentUser, stateName, projectId)

        coVerify(exactly = 1) { projectStateRepository.addProjectState(expectedState) }
    }

    @Test
    fun `should throw an exception when user role is mate`() = runTest {
        val currentUser =
            User(userId = UUID.randomUUID(), username = "Zinah",  userRole = UserRole.MATE)
        val stateName = "pending"
        val projectId = UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
        assertFailsWith<IllegalAccessException> {
            addCustomProjectStateUseCase.execute(currentUser, stateName, projectId)
        }
    }

    @Test
    fun ` should throw an exception when state name is blank`() = runTest {
        val currentUser =
            User(userId = UUID.randomUUID(), username = "Zinah", userRole = UserRole.ADMIN)
        assertThrows<IllegalArgumentException> {
            addCustomProjectStateUseCase.execute(
                currentUser,
                "",
                UUID.fromString("f3b0c4a2-5d6e-4c8b-9f1e-7a2b3c4d5e6f")
            )
        }
    }

}