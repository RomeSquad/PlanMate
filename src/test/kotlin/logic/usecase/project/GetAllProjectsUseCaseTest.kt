package logic.usecase.project

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class GetAllProjectsUseCaseTest {

    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var projectRepository: ProjectRepository

    private val projectId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    private val projectId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")

    private val project1 = Project(
        projectId = projectId1,
        name = "Project 1",
        description = "Description 1",
        state = ProjectState(projectId = projectId1, stateName = "Active")
    )

    private val project2 = Project(
        projectId = projectId2,
        name = "Project 2",
        description = "Description 2",
        state = ProjectState(projectId = projectId2, stateName = "InProgress")
    )

    @BeforeEach
    fun setup() {
        projectRepository = mockk()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }

    @Test
    fun `get all projects returns list when repository returns projects`() = runTest {
        // Given
        val projects = listOf(project1, project2)
        coEvery { projectRepository.getAllProjects() } returns projects

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        assertEquals(projects, result)
        assertEquals(2, result.size)
        assertEquals("Project 1", result[0].name)
        assertEquals("Project 2", result[1].name)
        assertEquals("Active", result[0].state.stateName)
        assertEquals("InProgress", result[1].state.stateName)
    }

    @Test
    fun `get all projects returns empty list when repository returns no projects`() = runTest {
        // Given
        coEvery { projectRepository.getAllProjects() } returns emptyList()

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        assertEquals(emptyList<Project>(), result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `get all projects throws exception when repository fails`() = runTest {
        // Given
        val exception = RuntimeException("Failed to fetch projects")
        coEvery { projectRepository.getAllProjects() } throws exception

        // When/Then
        val thrownException = assertThrows<RuntimeException> {
            getAllProjectsUseCase.getAllProjects()
        }
        assertEquals("Failed to fetch projects", thrownException.message)
    }
}