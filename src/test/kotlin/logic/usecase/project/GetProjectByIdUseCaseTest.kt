package logic.usecase.project

import io.mockk.coEvery
import io.mockk.coVerify
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
import java.util.*

class GetProjectByIdUseCaseTest {
    private lateinit var getProjectByIdUseCase: GetProjectByIdUseCase
    private lateinit var projectRepository: ProjectRepository
    private val projectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    private val nonExistentId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")

    private val project = Project(
        projectId = projectId,
        name = "Test Project",
        description = "Test Description",
        state = ProjectState(projectId = projectId, stateName = "Active")
    )

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        getProjectByIdUseCase = GetProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `get project by valid ID returns correct project`() = runTest {
        coEvery { projectRepository.getProjectById(projectId) } returns project

        val result = getProjectByIdUseCase.getProjectById(projectId)

        assertEquals(project, result)
        assertEquals(projectId, result.projectId)
        assertEquals("Test Project", result.name)
        assertEquals("Test Description", result.description)
        assertEquals("Active", result.state.stateName)
        coVerify { projectRepository.getProjectById(projectId) }
    }

    @Test
    fun `get project by non-existent ID throws NotFoundException`() = runTest {
        val exception = Exception("Project with id $nonExistentId not found")
        coEvery { projectRepository.getProjectById(nonExistentId) } throws exception

        val thrownException = assertThrows<Exception> {
            getProjectByIdUseCase.getProjectById(nonExistentId)
        }
        assertEquals("Project with id $nonExistentId not found", thrownException.message)
        coVerify { projectRepository.getProjectById(nonExistentId) }
    }
}