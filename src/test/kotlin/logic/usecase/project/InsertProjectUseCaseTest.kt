package logic.usecase.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.entity.User
import org.example.logic.entity.User.UserRole
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.InsertProjectUseCase
import org.example.logic.usecase.project.ValidationProject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class InsertProjectUseCaseTest {

    private lateinit var insertProjectUseCase: InsertProjectUseCase
    private lateinit var projectRepository: ProjectRepository
    private lateinit var validationProject: ValidationProject

    private val userId = UUID.randomUUID()
    private val projectId = UUID.randomUUID()

    private val user = User(
        userId = userId,
        username = "Mohamed",
        password = "password",
        userRole = UserRole.ADMIN

    )

    private val validProject = Project(
        projectId = projectId,
        name = "Test Project",
        description = "Test Description",
        state = ProjectState(projectId = projectId, stateName = "InProgress")
    )

    private val emptyNameProject = validProject.copy(name = "")
    private val whitespaceNameProject = validProject.copy(name = "   ")

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        validationProject = mockk()
        insertProjectUseCase = InsertProjectUseCase(projectRepository, validationProject)
    }

    @Test
    fun `insert project with valid project and user returns project ID`() = runTest {
        coEvery { validationProject.validateCreateProject(validProject, user) } returns Unit
        coEvery { projectRepository.createProject(validProject, user) } returns projectId

        val result = insertProjectUseCase.insertProject(validProject, user)

        assertEquals(projectId, result)
        coVerify { validationProject.validateCreateProject(validProject, user) }
        coVerify { projectRepository.createProject(validProject, user) }
    }

    @Test
    fun `insert project with empty name throws IllegalArgumentException`() = runTest {
        coEvery {
            validationProject.validateCreateProject(
                emptyNameProject,
                user
            )
        } throws IllegalArgumentException("Project name cannot be blank")

        val exception = assertThrows<IllegalArgumentException> {
            insertProjectUseCase.insertProject(emptyNameProject, user)
        }
        assertEquals("Project name cannot be blank", exception.message)
        coVerify { validationProject.validateCreateProject(emptyNameProject, user) }
        coVerify(exactly = 0) { projectRepository.createProject(any(), any()) }
    }

    @Test
    fun `insert project with whitespace name throws IllegalArgumentException`() = runTest {
        coEvery {
            validationProject.validateCreateProject(
                whitespaceNameProject,
                user
            )
        } throws IllegalArgumentException("Project name cannot be blank")

        val exception = assertThrows<IllegalArgumentException> {
            insertProjectUseCase.insertProject(whitespaceNameProject, user)
        }
        assertEquals("Project name cannot be blank", exception.message)
        coVerify { validationProject.validateCreateProject(whitespaceNameProject, user) }
        coVerify(exactly = 0) { projectRepository.createProject(any(), any()) }
    }

    @Test
    fun `insert project throws exception when repository fails`() = runTest {
        coEvery { validationProject.validateCreateProject(validProject, user) } returns Unit
        val repositoryException = RuntimeException("Failed to insert project")
        coEvery { projectRepository.createProject(validProject, user) } throws repositoryException

        val exception = assertThrows<RuntimeException> {
            insertProjectUseCase.insertProject(validProject, user)
        }
        assertEquals("Failed to insert project", exception.message)
        coVerify { validationProject.validateCreateProject(validProject, user) }
        coVerify { projectRepository.createProject(validProject, user) }
    }

    @Test
    fun `insert project with invalid user throws IllegalArgumentException`() = runTest {
        val invalidUser = user.copy(userId = UUID.randomUUID())
        coEvery {
            validationProject.validateCreateProject(
                validProject,
                invalidUser
            )
        } throws IllegalArgumentException("Invalid user")

        val exception = assertThrows<IllegalArgumentException> {
            insertProjectUseCase.insertProject(validProject, invalidUser)
        }
        assertEquals("Invalid user", exception.message)
        coVerify { validationProject.validateCreateProject(validProject, invalidUser) }
        coVerify(exactly = 0) { projectRepository.createProject(any(), any()) }
    }
}