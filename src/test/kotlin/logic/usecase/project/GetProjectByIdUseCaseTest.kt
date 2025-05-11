package logic.usecase.project

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.GetProjectByIdUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetProjectByIdUseCaseTest {

    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var projectRepository: ProjectRepository

    private val project = Project(
        id = 1,
        name = "Test Project",
        description = "Test Description",
        state = ProjectState(projectId = 1, stateName = "Active")
    )

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `get project by valid id returns project`() = runTest {
        // Given
        coEvery { projectRepository.getProjectById(1) } returns project

        // When
        val result = getProjectByIdUseCase.getProjectById(1)

        // Then
        assertEquals(project, result)
        assertEquals(1, result.id)
        assertEquals("Test Project", result.name)
        assertEquals("Active", result.state.stateName)
    }

    @Test
    fun `get project by zero id throws IllegalArgumentException`() = runTest {
        // When/Then
        val exception = assertThrows<IllegalArgumentException> {
            getProjectByIdUseCase.getProjectById(0)
        }
        assertEquals("Project id must be greater than zero", exception.message)
    }

    @Test
    fun `get project by negative id throws IllegalArgumentException`() = runTest {
        // When/Then
        val exception = assertThrows<IllegalArgumentException> {
            getProjectByIdUseCase.getProjectById(-1)
        }
        assertEquals("Project id must be greater than zero", exception.message)
    }

    @Test
    fun `get project by non-existent id throws exception`() = runTest {
        // Given
        val invalidId = 99
        val exception = Exception("Project with id $invalidId not found")
        coEvery { projectRepository.getProjectById(invalidId) } throws exception

        // When/Then
        val thrownException = assertThrows<Exception> {
            getProjectByIdUseCase.getProjectById(invalidId)
        }
        assertEquals("Project with id $invalidId not found", thrownException.message)
    }
}