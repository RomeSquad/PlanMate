package logic.usecase.project

import io.mockk.every
import io.mockk.mockk
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.DeleteProjectByIdUseCase
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteProjectByIdUseCaseTest {
    private lateinit var deleteProjectByIdUseCase: DeleteProjectByIdUseCase
    private lateinit var projectRepository: ProjectRepository

    @BeforeEach
    fun setup() {
        projectRepository = mockk<ProjectRepository>()
        deleteProjectByIdUseCase = DeleteProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `should return success when repository deletes project successfully`() {
        // Given
        val projectId = 1
        every { projectRepository.deleteProject(projectId) } returns Result.success(Unit)

        // When
        val result = deleteProjectByIdUseCase.deleteProjectById(projectId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(Unit, result.getOrNull())
    }

    @Test
    fun `should return failure when repository fails to delete project`() {
        // Given
        val projectId = 1
        val exception = RuntimeException("Project not found")
        every { projectRepository.deleteProject(projectId) } returns Result.failure(exception)

        // When
        val result = deleteProjectByIdUseCase.deleteProjectById(projectId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}