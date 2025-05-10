package logic.usecase.project

import io.mockk.every
import io.mockk.mockk
import org.example.logic.entity.Project
import org.example.logic.entity.ProjectState
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.GetAllProjectsUseCase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetAllProjectsUseCaseTest {
    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase
    private lateinit var projectRepository: ProjectRepository

    @BeforeEach
    fun setup() {
        projectRepository = mockk<ProjectRepository>()
        getAllProjectsUseCase = GetAllProjectsUseCase(projectRepository)
    }

    @Test
    fun `should return list of projects when repository returns success`() {
        // Given
        val projects = listOf(
            Project(
                id = 1,
                name = "Project A",
                description = "A sample project",
                state = ProjectState(
                    projectId = 1,
                    stateName = "Project B",
                )
            ),
            Project(
                id = 2,
                name = "Project B",
                description = "Another sample project",
                state = ProjectState(
                    projectId = 2,
                    stateName = "Project B",
                )
            )
        )
        every { projectRepository.getAllProjects() } returns Result.success(projects)

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(projects, result.getOrNull())
    }

    @Test
    fun `should return failure when repository throws exception`() {
        // Given
        val exception = RuntimeException("Failed to fetch projects")
        every { projectRepository.getAllProjects() } returns Result.failure(exception)

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        Assertions.assertTrue(result.isFailure)
        Assertions.assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `should return empty list when repository returns success with no projects`() {
        // Given
        val projects = emptyList<Project>()
        every { projectRepository.getAllProjects() } returns Result.success(projects)

        // When
        val result = getAllProjectsUseCase.getAllProjects()

        // Then
        Assertions.assertTrue(result.isSuccess)
        Assertions.assertEquals(projects, result.getOrNull())
        Assertions.assertTrue(result.getOrNull()?.isEmpty() == true)
    }
}