package logic.usecase.project

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.example.logic.repository.ProjectRepository
import org.example.logic.usecase.project.DeleteProjectByIdUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals

class DeleteProjectByIdUseCaseTest {
    private lateinit var useCase: DeleteProjectByIdUseCase
    private val projectRepository: ProjectRepository = mockk()

    @BeforeEach
    fun setUp() {
        useCase = DeleteProjectByIdUseCase(projectRepository)
    }

    @Test
    fun `deleteProjectById should call repository with correct id and return Unit on success`() = runTest {
        val projectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        coEvery { projectRepository.deleteProject(projectId) } returns Unit

        val result = useCase.deleteProjectById(projectId)

        coVerify(exactly = 1) { projectRepository.deleteProject(projectId) }
        assertEquals(Unit, result)
    }

    @Test
    fun `deleteProjectById should throw IllegalArgumentException when project does not exist`() = runTest {
        val projectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        val exception = IllegalArgumentException("Project with ID $projectId not found")
        coEvery { projectRepository.deleteProject(projectId) } throws exception

        val thrown = assertThrows<IllegalArgumentException> {
            useCase.deleteProjectById(projectId)
        }
        assertEquals("Project with ID $projectId not found", thrown.message)
        coVerify(exactly = 1) { projectRepository.deleteProject(projectId) }
    }

    @Test
    fun `deleteProjectById should propagate unexpected exceptions from repository`() = runTest {
        val projectId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        val exception = RuntimeException("Database error")
        coEvery { projectRepository.deleteProject(projectId) } throws exception

        val thrown = assertThrows<RuntimeException> {
            useCase.deleteProjectById(projectId)
        }
        assertEquals("Database error", thrown.message)
        coVerify(exactly = 1) { projectRepository.deleteProject(projectId) }
    }
}